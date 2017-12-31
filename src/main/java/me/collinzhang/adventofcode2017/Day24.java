package me.collinzhang.adventofcode2017;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;
import java.util.function.BiFunction;

import me.collinzhang.support.Util;

public class Day24 {

    private static final String TEST = "0/2\n" + "2/2\n" + "2/3\n" + "3/4\n" + "3/5\n" + "0/1\n" + "10/1\n" + "9/10";

    public static void main(String[] args) {
        System.out.println(day24P1(new Scanner(TEST)));
        System.out.println(day24P1(Util.openScanner("/2017/day24.txt")));

        System.out.println(day24P2(Util.openScanner("/2017/day24.txt")));
    }

    private static int day24P1(Scanner input) {
        CURRENT_COMPARATOR = COMPARATOR_P1;
        return doSearch(input);
    }

    private static int day24P2(Scanner input) {
        CURRENT_COMPARATOR = COMPARATOR_P2;
        return doSearch(input);
    }

    private static int doSearch(Scanner input) {
        List<Pipe> pipes = parsePipes(input);
        Map<Integer, List<Pipe>> pipeMap = new HashMap<>();
        for (Pipe p : pipes) {
            pipeMap.computeIfAbsent(p.port1, (k) -> new ArrayList<>()).add(p);
            pipeMap.computeIfAbsent(p.port2, (k) -> new ArrayList<>()).add(p);
        }

        RecursiveBuilderContext initContext = new RecursiveBuilderContext();
        initContext.pipeMap = pipeMap;
        initContext.requiresPort = 0;
        initContext.usedPipes = new HashSet<>();

        BridgeBuilder initBuilder = new BridgeBuilder(initContext);
        ForkJoinPool forkJoinPool = new ForkJoinPool(4);
        return forkJoinPool.invoke(initBuilder).totalStrength;
    }

    private static final BiFunction<Bridge, Bridge, Bridge> COMPARATOR_P1 = (b1, b2) -> b1.totalStrength > b2.totalStrength ? b1 : b2;
    private static final BiFunction<Bridge, Bridge, Bridge> COMPARATOR_P2 = (b1, b2) -> {
        if (b1.usedPipes.size() == b2.usedPipes.size()) {
            return COMPARATOR_P1.apply(b1, b2);
        }
        return b1.usedPipes.size() > b2.usedPipes.size() ? b1 : b2;
    };

    private static BiFunction<Bridge, Bridge, Bridge> CURRENT_COMPARATOR = COMPARATOR_P1;

    /**
     * BFS search to enumerate all possible bridge configurations.
     * Doesn't really matter BFS or DFS because we have to find all bridges anyways
     */
    static class BridgeBuilder extends RecursiveTask<Bridge> {

        private static final long serialVersionUID = -8293899472740126624L;
        private RecursiveBuilderContext recursiveBuilderContext;

        BridgeBuilder(RecursiveBuilderContext recursiveBuilderContext) {
            this.recursiveBuilderContext = recursiveBuilderContext;
        }

        @Override
        protected Bridge compute() {
            List<Pipe> candidates = recursiveBuilderContext.pipeMap.getOrDefault(recursiveBuilderContext.requiresPort, Collections.emptyList());
            List<BridgeBuilder> subtasks = new ArrayList<>(candidates.size());

            // Enumerate every possibility
            for (Pipe p : candidates) {
                if (recursiveBuilderContext.usedPipes.contains(p)) {
                    continue;
                }

                Set<Pipe> newUsed = new HashSet<>(recursiveBuilderContext.usedPipes);
                newUsed.add(p);
                RecursiveBuilderContext newContext = new RecursiveBuilderContext();
                newContext.usedPipes = newUsed;
                newContext.requiresPort = p.port1 == recursiveBuilderContext.requiresPort ? p.port2 : p.port1;
                newContext.pipeMap = recursiveBuilderContext.pipeMap;

                BridgeBuilder bridgeBuilder = new BridgeBuilder(newContext);
                bridgeBuilder.fork();
                subtasks.add(bridgeBuilder);
            }

            if (subtasks.isEmpty()) {
                // Out of usable pipes, build and return the bridge

                Bridge bridge = new Bridge();
                bridge.usedPipes = recursiveBuilderContext.usedPipes;
                int strength = 0;
                for (Pipe p : bridge.usedPipes) {
                    strength += p.getStrength();
                }
                bridge.totalStrength = strength;
                return bridge;
            }

            Bridge res = null;

            // Use CURRENT_COMPARATOR to find the bridge we want
            for (BridgeBuilder bridgeBuilder : subtasks) {
                try {
                    Bridge bridge = bridgeBuilder.get();
                    if (res == null) {
                        res = bridge;
                    } else {
                        res = CURRENT_COMPARATOR.apply(res, bridge);
                    }
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }

            return res;
        }
    }

    private static List<Pipe> parsePipes(Scanner input) {
        List<Pipe> pipes = new LinkedList<>();
        while (input.hasNext()) {
            String line = input.nextLine().replace(" ", "");
            String[] words = line.split("/");
            pipes.add(new Pipe(Integer.parseInt(words[0]), Integer.parseInt(words[1])));
        }
        return pipes;
    }

    static class RecursiveBuilderContext implements Serializable {

        private static final long serialVersionUID = -8732841692260040549L;
        Map<Integer, List<Pipe>> pipeMap;
        int requiresPort;
        Set<Pipe> usedPipes;
    }

    static class Bridge {

        Set<Pipe> usedPipes;
        int totalStrength;
    }

    static class Pipe implements Serializable {

        private static final long serialVersionUID = 8214726637105911512L;
        int port1;
        int port2;

        Pipe(int port1, int port2) {
            this.port1 = port1;
            this.port2 = port2;
        }

        int getStrength() {
            return port1 + port2;
        }
    }
}
