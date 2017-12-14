package me.collinzhang.adventofcode2017;

import java.math.BigInteger;
import java.util.Deque;
import java.util.LinkedList;

public class Day14 {

    public static void main(String[] args) {
        System.out.println(day14P1("flqrgnkx"));
        System.out.println(day14P1("stpzcrnm"));

        System.out.println(day14P2("flqrgnkx"));
        System.out.println(day14P2("stpzcrnm"));
    }

    private static int day14P2(String input) {
        boolean[][] grid = new boolean[128][128];
        for (int i = 0; i < 128; i++) {
            String hash = Day10.day10p2(input + "-" + i);

            BigInteger bigInteger = new BigInteger(hash, 16);
            for (int j = 127; j >= 0; j--) {
                grid[i][127 - j] = bigInteger.testBit(j);
            }
        }

        int sum = 0;
        for (int i = 0; i < 128; i++) {
            for (int j = 0; j < 128; j++) {
                if (grid[i][j]) {
                    sum++;
                    Deque<Position> toCheck = new LinkedList<>();
                    toCheck.addLast(new Position(i, j));
                    while (!toCheck.isEmpty()) {
                        Position next = toCheck.removeFirst();
                        if (grid[next.x][next.y]) {
                            grid[next.x][next.y] = false;
                            if (isPositionValid(next.x + 1, next.y))
                                toCheck.addLast(new Position(next.x + 1, next.y));
                            if (isPositionValid(next.x - 1, next.y))
                                toCheck.addLast(new Position(next.x - 1, next.y));
                            if (isPositionValid(next.x, next.y + 1))
                                toCheck.addLast(new Position(next.x, next.y + 1));
                            if (isPositionValid(next.x, next.y - 1))
                                toCheck.addLast(new Position(next.x, next.y - 1));
                        }
                    }
                }
            }
        }
        return sum;
    }

    private static boolean isPositionValid(int x, int y) {
        return x >= 0 && x < 128 && y >= 0 && y < 128;
    }

    static class Position {

        int x, y;

        public Position(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    private static int day14P1(String input) {
        int sum = 0;
        for (int i = 0; i < 128; i++) {
            String hash = Day10.day10p2(input + "-" + i);

            BigInteger bigInteger = new BigInteger(hash, 16);
            for (int j = 127; j >= 0; j--) {
                if (bigInteger.testBit(j)) {
                    sum++;
                }
            }
        }

        return sum;
    }
}
