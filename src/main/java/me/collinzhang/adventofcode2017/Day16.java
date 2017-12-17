package me.collinzhang.adventofcode2017;

import java.util.Scanner;

public class Day16 {

    private static final String SIMPLE_NAMES = "abcde";
    private static final String NAMES = "abcdefghijklmnop";

    private static final String TEST = "s1,x3/4,pe/b";

    public static void main(String[] args) {
        System.out.println(day16P1(new Scanner(TEST), SIMPLE_NAMES));
        System.out.println(day16P1(Util.openScanner("/day16.txt"), NAMES));

        System.out.println(day16P2(new Scanner(TEST), SIMPLE_NAMES));
        System.out.println(day16P2(Util.openScanner("/day16.txt"), NAMES));
    }

    private static String day16P2(Scanner input, String allDancers) {
        String original = allDancers;
        String moves = input.nextLine();
        int count = 0;
        do {
            Result result = runMoves(new Scanner(moves), allDancers);
            allDancers = joinFrom(result.dancers, result.zeroPosition);
            count++;
        } while (!original.equals(allDancers));

        String[] dancers = new String[allDancers.length()];

        for (int i = 0; i < dancers.length; i++) {
            dancers[i] = (allDancers.substring(i, i + 1));
        }

        int times = 1_000_000_000 % count;
        Result result = new Result(dancers, 0);
        allDancers = original;
        for (int i = 0; i < times; i++) {
            result = runMoves(new Scanner(moves), allDancers);
            allDancers = joinFrom(result.dancers, result.zeroPosition);
        }

        return joinFrom(result.dancers, result.zeroPosition);
    }

    private static String day16P1(Scanner input, String allDancers) {
        Result result = runMoves(input, allDancers);
        return joinFrom(result.dancers, result.zeroPosition);
    }

    private static Result runMoves(Scanner input, String allDancers) {
        input.useDelimiter(",");

        String[] dancers = new String[allDancers.length()];

        for (int i = 0; i < dancers.length; i++) {
            dancers[i] = (allDancers.substring(i, i + 1));
        }

        // Instead of actually shifting the array, we assume a different index as the "start" of the array
        int zeroPosition = 0;

        while (input.hasNext()) {
            String next = input.next();
            String action = next.substring(0, 1);
            String param = next.substring(1);

            if ("s".equals(action)) {
                int steps = Integer.parseInt(param);

                zeroPosition = zeroPosition - steps + dancers.length;
                zeroPosition %= dancers.length;

            } else if ("x".equals(action)) {
                String[] params = param.split("/");
                int nA = Integer.parseInt(params[0]);
                int nB = Integer.parseInt(params[1]);

                String temp = dancers[(zeroPosition + nA) % dancers.length];
                dancers[(zeroPosition + nA) % dancers.length] = dancers[(zeroPosition + nB) % dancers.length];
                dancers[(zeroPosition + nB) % dancers.length] = temp;

            } else if ("p".equals(action)) {
                String[] params = param.split("/");
                int nA = indexOf(dancers, params[0], zeroPosition);
                int nB = indexOf(dancers, params[1], zeroPosition);

                String temp = dancers[(zeroPosition + nA) % dancers.length];
                dancers[(zeroPosition + nA) % dancers.length] = dancers[(zeroPosition + nB) % dancers.length];
                dancers[(zeroPosition + nB) % dancers.length] = temp;
            }

        }

        return new Result(dancers, zeroPosition);
    }

    static class Result {

        String[] dancers;
        int zeroPosition;

        public Result(String[] dancers, int zeroPosition) {
            this.dancers = dancers;
            this.zeroPosition = zeroPosition;
        }
    }

    private static String joinFrom(String[] dancers, int zeroPosition) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < dancers.length; i++) {
            stringBuilder.append(dancers[(zeroPosition + i) % dancers.length]);
        }
        return stringBuilder.toString();
    }

    private static int indexOf(String[] array, String str, int zeroOffset) {
        for (int i = 0; i < array.length; i++) {
            if (str.equals(array[(zeroOffset + i) % array.length])) {
                return i;
            }
        }
        throw new RuntimeException(str);
    }
}
