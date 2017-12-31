package me.collinzhang.adventofcode2017;

import java.util.Arrays;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import me.collinzhang.support.Util;

public class Day12 {

    private static final String TEST = "0 <-> 2\n" + "1 <-> 1\n" + "2 <-> 0, 3, 4\n" + "3 <-> 2, 4\n" + "4 <-> 2, 3, 6\n" + "5 <-> 6\n" + "6 <-> 4, 5";

    private static final Pattern REGEX = Pattern.compile("([0-9]+)<->([0-9,]+)");

    public static void main(String[] args) {
        System.out.println(day12P1(new Scanner(TEST)));
        System.out.println(day12P1(Util.openScanner("/2017/day12.txt")));

        System.out.println(day12P2(new Scanner(TEST)));
        System.out.println(day12P2(Util.openScanner("/2017/day12.txt")));
    }

    private static int day12P2(Scanner input) {
        Map<Integer, Set<Integer>> pipes = parsePipes(input);

        Set<Integer> found = new HashSet<>();
        int groups = 0;

        for (Integer from : pipes.keySet()) {
            if (!found.contains(from)) {
                found.addAll(findReachable(pipes, from));
                groups++;
            }
        }
        return groups;
    }

    private static int day12P1(Scanner input) {
        Map<Integer, Set<Integer>> pipes = parsePipes(input);
        return findReachable(pipes, 0).size();
    }

    private static Set<Integer> findReachable(Map<Integer, Set<Integer>> pipes, int from) {
        Deque<Integer> toCheck = new LinkedList<>();
        Set<Integer> reachables = new HashSet<>();

        toCheck.add(from);
        reachables.add(from);

        while (!toCheck.isEmpty()) {
            Integer curr = toCheck.removeFirst();
            pipes.get(curr).stream().filter(reachables::add).forEach(toCheck::addLast);
        }
        return reachables;
    }

    private static Map<Integer, Set<Integer>> parsePipes(Scanner input) {
        Map<Integer, Set<Integer>> pipes = new HashMap<>();

        while (input.hasNext()) {
            String line = input.nextLine().replace(" ", "");
            Matcher matcher = REGEX.matcher(line);
            if (!matcher.matches()) {
                continue;
            }

            int prog = Integer.parseInt(matcher.group(1));
            Set<Integer> conns = Arrays.stream(matcher.group(2).split(",")).map(Integer::parseInt).collect(Collectors.toSet());
            pipes.put(prog, conns);
        }

        return pipes;
    }
}
