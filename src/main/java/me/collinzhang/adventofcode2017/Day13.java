package me.collinzhang.adventofcode2017;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import me.collinzhang.support.Util;

public class Day13 {

    private static final String TEST = "0: 3\n" + "1: 2\n" + "4: 4\n" + "6: 4";

    public static void main(String[] args) {
        System.out.println(day13P1(new Scanner(TEST)));
        System.out.println(day13P1(Util.openScanner("/2017/day13.txt")));

        System.out.println(day13P2(new Scanner(TEST)));
        System.out.println(day13P2(Util.openScanner("/2017/day13.txt")));
    }

    private static int day13P2(Scanner input) {
        Map<Integer, Integer> layers = parseLayerRange(input);
        int maxLayer = layers.keySet().stream().max(Comparator.naturalOrder()).get();

        int delay = 0;

        outer:
        for (; ; delay++) {
            for (int i = 0; i <= maxLayer; i++) {
                Integer range = layers.get(i);
                if (range == null) {
                    continue;
                }

                if (calculateOffset(i + delay, range) == 0) {
                    continue outer;
                }
            }
            break;
        }

        return delay;
    }

    private static int day13P1(Scanner input) {
        int severity = 0;

        Map<Integer, Integer> layers = parseLayerRange(input);
        int maxLayer = layers.keySet().stream().max(Comparator.naturalOrder()).get();

        for (int i = 0; i <= maxLayer; i++) {
            Integer range = layers.get(i);
            if (range == null) {
                continue;
            }

            if (calculateOffset(i, range) == 0) {
                severity += range * i;
            }
        }

        return severity;
    }

    private static Map<Integer, Integer> parseLayerRange(Scanner input) {
        Map<Integer, Integer> layers = new HashMap<>();
        while (input.hasNext()) {
            String line = input.nextLine().replace(" ", "");
            String[] parts = line.split(":");
            layers.put(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]));
        }
        return layers;
    }

    private static int calculateOffset(int steps, int range) {
        if (range == 1) {
            return 0;
        } else if (range == 2) {
            return steps % 2;
        }

        steps %= range * 2 - 2;
        if (steps > range - 1) {
            return range * 2 - 2 - steps;
        }

        return steps;
    }
}
