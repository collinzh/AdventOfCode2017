package me.collinzhang.adventofcode2016;

import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

import me.collinzhang.support.Direction;
import me.collinzhang.support.Position;
import me.collinzhang.support.Util;

public class Day1 {

    public static void main(String[] args) {
        System.out.println(day1P1(Util.openScanner("/2016/day1.txt")));
        System.out.println(day1P2(Util.openScanner("/2016/day1.txt")));
    }

    private static int day1P2(Scanner input) {
        String line = input.nextLine().replace(" ", "");
        String[] words = line.split(",");
        Set<Position> visited = new HashSet<>();

        Direction direction = Direction.UP;
        Position position = new Position(0, 0);
        for (String next : words) {

            if (next.charAt(0) == 'L') {
                direction = Direction.leftOf(direction);
            } else {
                direction = Direction.rightOf(direction);
            }
            int len = Integer.parseInt(next.substring(1));

            for (int i = 0; i < len; i++) {
                if (visited.contains(position)) {
                    return Math.abs(position.x) + Math.abs(position.y);
                }
                visited.add(position);
                position = position.move(direction);
            }
        }

        return 0;
    }

    private static int day1P1(Scanner input) {
        String line = input.nextLine().replace(" ", "");
        String[] words = line.split(",");
        Direction direction = Direction.UP;
        int x = 0;
        int y = 0;
        for (String next : words) {
            if (next.charAt(0) == 'L') {
                direction = Direction.leftOf(direction);
            } else {
                direction = Direction.rightOf(direction);
            }
            int len = Integer.parseInt(next.substring(1));

            switch (direction) {

            case UP:
                y -= len;
                break;
            case DOWN:
                y += len;
                break;
            case LEFT:
                x -= len;
                break;
            case RIGHT:
                x += len;
                break;
            }
        }

        return Math.abs(x) + Math.abs(y);
    }
}
