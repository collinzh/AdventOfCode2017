package me.collinzhang.adventofcode2017;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.StringJoiner;

public class DaySix {

    private static final String DAY_SIX = "0\t5\t10\t0\t11\t14\t13\t4\t11\t8\t8\t7\t1\t4\t12\t11";

    public static void main(String[] args) {
        System.out.println(daySix1(DAY_SIX));
        System.out.println(daySix2(DAY_SIX));
    }

    private static long daySix2(String input) {
        List<Integer> integerList = new LinkedList<>();
        Scanner scanner = new Scanner(input);
        while (scanner.hasNext()) {
            integerList.add(scanner.nextInt());
        }
        Integer[] banks = integerList.toArray(new Integer[] {});

        Map<String, Long> seen = new HashMap<>();
        String current = "";
        long counter = 0;

        while (!seen.containsKey(current)) {
            seen.put(current, counter);

            counter++;
            int maxBankIdx = -1;
            int maxBlocks = Integer.MIN_VALUE;
            for (int i = 0; i < banks.length; i++) {
                if (banks[i] > maxBlocks) {
                    maxBlocks = banks[i];
                    maxBankIdx = i;
                }
            }

            banks[maxBankIdx] = 0;
            for (int i = 1; i <= maxBlocks; i++) {
                banks[(maxBankIdx + i) % banks.length]++;
            }

            StringJoiner stringJoiner = new StringJoiner(" ");
            Arrays.stream(banks).forEach(b -> stringJoiner.add(b.toString()));
            current = stringJoiner.toString();
        }

        return counter - seen.get(current);
    }

    private static long daySix1(String input) {
        List<Integer> integerList = new LinkedList<>();
        Scanner scanner = new Scanner(input);
        while (scanner.hasNext()) {
            integerList.add(scanner.nextInt());
        }
        Integer[] banks = integerList.toArray(new Integer[] {});

        Set<String> seen = new HashSet<>();
        String current = "";
        long counter = 0;

        while (!seen.contains(current)) {
            seen.add(current);

            counter++;
            int maxBankIdx = -1;
            int maxBlocks = Integer.MIN_VALUE;
            for (int i = 0; i < banks.length; i++) {
                if (banks[i] > maxBlocks) {
                    maxBlocks = banks[i];
                    maxBankIdx = i;
                }
            }

            banks[maxBankIdx] = 0;
            for (int i = 1; i <= maxBlocks; i++) {
                banks[(maxBankIdx + i) % banks.length]++;
            }

            StringJoiner stringJoiner = new StringJoiner(" ");
            Arrays.stream(banks).forEach(b -> stringJoiner.add(b.toString()));
            current = stringJoiner.toString();
        }

        return counter;
    }
}
