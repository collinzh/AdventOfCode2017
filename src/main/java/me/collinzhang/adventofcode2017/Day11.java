package me.collinzhang.adventofcode2017;

import java.util.Scanner;

public class Day11 {

    public static void main(String[] args) {
        System.out.println(day11p1(new Scanner("se,sw,se,sw,sw")));
        System.out.println(day11p1(Util.openScanner("/day11.txt")));

        System.out.println(day11p2(Util.openScanner("/day11.txt")));
    }

    private static int day11p2(Scanner input) {
        input.useDelimiter(",");
        int x = 0;
        int y = 0;
        int max = 0;

        while (input.hasNext()) {
            String next = input.next().trim();

            if ("n".equals(next)) {
                y += 2;
            } else if ("ne".equals(next)) {
                x++;
                y++;
            } else if ("se".equals(next)) {
                x++;
                y--;
            } else if ("s".equals(next)) {
                y -= 2;
            } else if ("sw".equals(next)) {
                x--;
                y--;
            } else if ("nw".equals(next)) {
                x--;
                y++;
            }

            int dist = getDistance(x, y);
            max = Math.max(dist, max);
        }

        return max;
    }

    private static int getDistance(int x, int y) {
        x = Math.abs(x);
        y = Math.abs(y);

        int sum = x;
        y -= x;
        sum += y / 2;

        return sum;
    }

    private static int day11p1(Scanner input) {
        input.useDelimiter(",");
        int x = 0;
        int y = 0;
        while (input.hasNext()) {
            String next = input.next().trim();

            if ("n".equals(next)) {
                y += 2;
            } else if ("ne".equals(next)) {
                x++;
                y++;
            } else if ("se".equals(next)) {
                x++;
                y--;
            } else if ("s".equals(next)) {
                y -= 2;
            } else if ("sw".equals(next)) {
                x--;
                y--;
            } else if ("nw".equals(next)) {
                x--;
                y++;
            }
        }

        return getDistance(x, y);
    }
}
