package me.collinzhang.adventofcode2017;

import static me.collinzhang.support.Direction.leftOf;
import static me.collinzhang.support.Direction.reverseOf;
import static me.collinzhang.support.Direction.rightOf;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import me.collinzhang.support.Direction;
import me.collinzhang.support.Util;

public class Day22 {

    private static final String TEST = "..#\n" + "#..\n" + "...\n";

    public static void main(String[] args) {
        System.out.println(day22P1(new Scanner(TEST)));
        System.out.println(day22P1(Util.openScanner("/2017/day22.txt")));

        System.out.println(day22P2(new Scanner(TEST)));
        System.out.println(day22P2(Util.openScanner("/2017/day22.txt")));
    }

    private static int day22P1(Scanner input) {
        Map<Position, Boolean> map = new HashMap<>();
        int row = 0;
        for (; input.hasNext(); row++) {
            String line = input.nextLine().replace(" ", "");
            char[] chars = line.toCharArray();
            for (int i = 0; i < chars.length; i++) {
                Position position = new Position(i, row);
                map.put(position, chars[i] == '#');
            }
        }
        Direction direction = Direction.UP;
        int currentX = row / 2;
        int currentY = row / 2;
        int bursts = 0;

        for (int i = 0; i < 10000; i++) {
            Position currentPos = new Position(currentX, currentY);

            if (map.getOrDefault(currentPos, false)) {
                direction = rightOf(direction);
                map.put(currentPos, false);
            } else {
                direction = leftOf(direction);
                map.put(currentPos, true);
                bursts++;
            }

            switch (direction) {

            case UP:
                currentY--;
                break;
            case DOWN:
                currentY++;
                break;
            case LEFT:
                currentX--;
                break;
            case RIGHT:
                currentX++;
                break;
            }
        }

        return bursts;
    }

    private static int day22P2(Scanner input) {
        Map<Position, Character> map = new HashMap<>();
        int row = 0;
        for (; input.hasNext(); row++) {
            String line = input.nextLine().replace(" ", "");
            char[] chars = line.toCharArray();
            for (int i = 0; i < chars.length; i++) {
                Position position = new Position(i, row);
                map.put(position, chars[i] == '#' ? 'I' : 'C');
            }
        }

        Direction direction = Direction.UP;
        int currentX = row / 2;
        int currentY = row / 2;
        int bursts = 0;

        for (int i = 0; i < 10000000; i++) {
            Position currentPos = new Position(currentX, currentY);

            Character state = map.getOrDefault(currentPos, 'C');
            if (state == 'C') {
                direction = leftOf(direction);
                map.put(currentPos, 'W');
            } else if (state == 'W') {
                map.put(currentPos, 'I');
                bursts++;
            } else if (state == 'I') {
                direction = rightOf(direction);
                map.put(currentPos, 'F');
            } else if (state == 'F') {
                direction = reverseOf(direction);
                map.put(currentPos, 'C');
            } else {
                throw new RuntimeException();
            }

            switch (direction) {

            case UP:
                currentY--;
                break;
            case DOWN:
                currentY++;
                break;
            case LEFT:
                currentX--;
                break;
            case RIGHT:
                currentX++;
                break;
            }
        }

        return bursts;
    }

    static class Position {

        int x, y;

        public Position(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o)
                return true;
            if (!(o instanceof Position))
                return false;

            Position position = (Position) o;

            if (x != position.x)
                return false;
            return y == position.y;
        }

        @Override
        public int hashCode() {
            int result = x;
            result = 31 * result + y;
            return result;
        }

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder("Position{");
            sb.append("x=").append(x);
            sb.append(", y=").append(y);
            sb.append('}');
            return sb.toString();
        }
    }
}
