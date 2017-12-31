package me.collinzhang.adventofcode2017;

import java.util.Scanner;

import me.collinzhang.support.Util;

public class Day9 {

    public static void main(String[] args) {
        System.out.println(day9p1(new Scanner("{{<!!>},{<!!>},{<!!>},{<!!>}}")));
        System.out.println(day9p1(Util.openScanner("/2017/day9.txt")));

        System.out.println(day9p2(new Scanner("<{o\"i!a,<{i<a>")));
        System.out.println(day9p2(Util.openScanner("/2017/day9.txt")));
    }

    private static long day9p2(Scanner input) {
        long sum = 0;
        input.useDelimiter("");

        boolean inCancel = false;
        boolean inGarbage = false;

        while (input.hasNext()) {
            char c = input.next().charAt(0);

            if (inCancel) {
                inCancel = false;
            } else if ('!' == c) {
                inCancel = true;
            } else if (inGarbage) {
                if ('>' == c) {
                    inGarbage = false;
                } else {
                    sum++;
                }
            } else if ('<' == c) {
                inGarbage = true;
            }
        }

        return sum;
    }

    private static long day9p1(Scanner input) {
        long sum = 0;
        input.useDelimiter("");

        boolean inCancel = false;
        boolean inGarbage = false;
        int groupDepth = 0;

        while (input.hasNext()) {
            char c = input.next().charAt(0);

            if (inCancel) {
                inCancel = false;
            } else if ('!' == c) {
                inCancel = true;
            } else if (inGarbage) {
                if ('>' == c) {
                    inGarbage = false;
                }
            } else if ('<' == c) {
                inGarbage = true;
            } else if ('{' == c) {
                groupDepth++;
            } else if ('}' == c) {
                sum += groupDepth;
                groupDepth--;
            }
        }

        return sum;
    }
}
