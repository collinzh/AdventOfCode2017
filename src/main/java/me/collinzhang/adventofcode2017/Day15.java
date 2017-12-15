package me.collinzhang.adventofcode2017;

public class Day15 {

    public static void main(String[] args) {
        System.out.println(day15P1(65, 8921));
        System.out.println(day15P1(512, 191));

        System.out.println(day15P2(65, 8921));
        System.out.println(day15P2(512, 191));
    }

    private static long day15P2(long genA, long genB) {
        long count = 0;
        for (int i = 0; i < 5_000_000; i++) {
            do {
                genA = Math.multiplyExact(genA, 16807) % 2147483647;
            } while (genA % 4 != 0);

            do {
                genB = Math.multiplyExact(genB, 48271) % 2147483647;
            } while (genB % 8 != 0);

            if ((genA & 0xffff) == (genB & 0xffff)) {
                count++;
            }
        }
        return count;
    }

    private static long day15P1(long genA, long genB) {
        long count = 0;
        for (int i = 0; i < 40_000_000; i++) {
            genA = Math.multiplyExact(genA, 16807) % 2147483647;
            genB = Math.multiplyExact(genB, 48271) % 2147483647;

            if ((genA & 0xffff) == (genB & 0xffff)) {
                count++;
            }
        }
        return count;
    }
}
