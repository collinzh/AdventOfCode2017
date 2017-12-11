package me.collinzhang.adventofcode2017;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;
import java.util.concurrent.atomic.AtomicLong;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day7 {

    private static final Pattern LINE_PATTERN = Pattern.compile("([a-z]+)\\(([0-9]+)\\)(->([a-z,]+))?");

    private static final String TEST =
        "pbga (66)\n" + "xhth (57)\n" + "ebii (61)\n" + "havc (66)\n" + "ktlj (57)\n" + "fwft (72) -> ktlj, cntj, xhth\n" + "qoyq (66)\n"
            + "padx (45) -> pbga, havc, qoyq\n" + "tknk (41) -> ugml, padx, fwft\n" + "jptl (61)\n" + "ugml (68) -> gyxo, ebii, jptl\n" + "gyxo (61)\n"
            + "cntj (57)";

    public static void main(String[] args) {
        System.out.println(daySeven1(new Scanner(TEST)));
        System.out.println(daySeven1(new Scanner(Day7.class.getResourceAsStream("/day7.txt"))));

        // Got root name from previous step
        System.out.println(daySeven2(new Scanner(TEST), "tknk"));
        System.out.println(daySeven2(new Scanner(Day7.class.getResourceAsStream("/day7.txt")), "vvsvez"));
    }

    private static String daySeven1(Scanner input) {
        Set<String> nodes = new LinkedHashSet<>();
        Set<String> isChild = new LinkedHashSet<>();

        while (input.hasNext()) {
            String line = input.nextLine().replace(" ", "");
            Matcher matcher = LINE_PATTERN.matcher(line);

            if (!matcher.matches()) {
                System.out.println("Unrecognized line " + line);
                continue;
            }

            // Extract node name
            String name = matcher.group(1);
            nodes.add(name);

            // Extract children
            String childrenStr = matcher.group(4);
            if (childrenStr != null && !childrenStr.isEmpty()) {
                isChild.addAll(Arrays.asList(childrenStr.split(",")));
            }
        }

        // Find the node that's not a child of any other nodes
        for (String name : nodes) {
            if (!isChild.contains(name)) {
                return name;
            }
        }
        return null;
    }

    private static Long daySeven2(Scanner input, String rootName) {
        Map<String, Node> nodeMap = new HashMap<>();
        Map<String, List<String>> childMap = new HashMap<>();

        // Parse and build the tree
        while (input.hasNext()) {
            String line = input.nextLine().replace(" ", "");
            Matcher matcher = LINE_PATTERN.matcher(line);

            if (!matcher.matches()) {
                System.out.println("Unrecognized line " + line);
                continue;
            }

            String name = matcher.group(1);
            int weight = Integer.parseInt(matcher.group(2));

            Node node = new Node();
            node.name = name;
            node.weight = weight;

            nodeMap.put(name, node);

            // Extract children
            String childrenStr = matcher.group(4);
            if (childrenStr != null && !childrenStr.isEmpty()) {
                childMap.put(name, Arrays.asList(childrenStr.split(",")));
            }
        }

        // Construct the tree
        for (Map.Entry<String, List<String>> next : childMap.entrySet()) {
            Node parent = nodeMap.get(next.getKey());
            next.getValue().forEach(n -> parent.children.add(nodeMap.get(n)));
        }

        Node root = nodeMap.get(rootName);

        // Use a ForkJoinPool to search recursively
        ForkJoinPool forkJoinPool = new ForkJoinPool(4);

        // The root task
        UnbalancedNodeSearcher searcher = new UnbalancedNodeSearcher(root);

        SearchResponse searchResponse = forkJoinPool.invoke(searcher);

        return searchResponse.foundResult;
    }

    static class UnbalancedNodeSearcher extends RecursiveTask<SearchResponse> {

        private static final long serialVersionUID = 5012706786264493386L;

        Node searchRoot;

        UnbalancedNodeSearcher(Node searchRoot) {
            this.searchRoot = searchRoot;
        }

        @Override
        protected SearchResponse compute() {
            // Simply return weight of this node of terminal nodes
            if (this.searchRoot.children == null || this.searchRoot.children.isEmpty()) {
                return new SearchResponse(searchRoot.weight, null);
            }

            // Search child nodes recursively
            List<UnbalancedNodeSearcher> subtasks = new ArrayList<>(searchRoot.children.size());
            for (Node n : searchRoot.children) {
                UnbalancedNodeSearcher subtask = new UnbalancedNodeSearcher(n);
                subtasks.add(subtask);
                subtask.fork();
            }

            // Use a map to vote for the "wrong"(i.e. minority) disc weight
            Map<Long, AtomicLong> totalWeight = new HashMap<>();

            // To find out who reported the "wrong" weight
            Map<Long, Node> resultNodeMap = new HashMap<>();
            for (UnbalancedNodeSearcher searcher : subtasks) {
                SearchResponse response = searcher.join();

                // The search is over
                if (response.foundResult != null) {
                    return response;
                }

                resultNodeMap.put(response.subSum, searcher.searchRoot);

                totalWeight.computeIfAbsent(response.subSum, (i) -> new AtomicLong(0L)).incrementAndGet();
            }

            if (totalWeight.size() == 1) {
                // Since there's only one
                long subtotal = totalWeight.keySet().iterator().next();
                long total = subtotal * subtasks.size() + searchRoot.weight;

                return new SearchResponse(total, null);
            } else if (totalWeight.size() == 2) {
                long correctWeight = -1;
                long wrongWeight = -1;
                for (Map.Entry<Long, AtomicLong> entry : totalWeight.entrySet()) {
                    if (entry.getValue().get() != 1L) {
                        correctWeight = entry.getKey();
                    } else {
                        wrongWeight = entry.getKey();
                    }
                }

                return new SearchResponse(null, resultNodeMap.get(wrongWeight).weight - (wrongWeight - correctWeight));
            } else {
                throw new RuntimeException("More than one incorrect nodes");
            }
        }
    }

    static class SearchResponse implements Serializable {

        private static final long serialVersionUID = 7029426530004979273L;

        // Used to return total weight when we need to continue the search
        Long subSum;

        // Used to return the result once the unbalanced node is found
        Long foundResult;

        SearchResponse(Long subSum, Long foundResult) {
            this.subSum = subSum;
            this.foundResult = foundResult;
        }
    }

    static class Node implements Serializable {

        private static final long serialVersionUID = 7653544488822279740L;
        String name;

        long weight;

        List<Node> children = new ArrayList<>();

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder("Node{");
            sb.append("name='").append(name).append('\'');
            sb.append(", weight=").append(weight);
            sb.append('}');
            return sb.toString();
        }
    }
}
