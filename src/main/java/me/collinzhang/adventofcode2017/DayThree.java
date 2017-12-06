package me.collinzhang.adventofcode2017;

import java.util.HashMap;
import java.util.Map;

public class DayThree {

    public static void main(String[] args) {
        System.out.println(dayThree1(325489));
        System.out.println(dayThree2(325489));
    }

    private static long dayThree1(int input) {
        int upperBound = 0;
        int lowerBound = 0;
        int leftBound = 0;
        int rightBound = 0;

        int currentX = 0;
        int currentY = 0;

        Direction direction = Direction.RIGHT;

        int currentNumber = 1;

        while (input > currentNumber) {
            currentNumber++;
            switch (direction) {
            case UP:
                currentX++;
                if (currentX == upperBound + 1) {
                    direction = Direction.LEFT;
                    upperBound++;
                }
                break;
            case DOWN:
                currentX--;
                if (currentX == lowerBound - 1) {
                    direction = Direction.RIGHT;
                    lowerBound--;
                }
                break;
            case LEFT:
                currentY--;
                if (currentY == leftBound - 1) {
                    direction = Direction.DOWN;
                    leftBound--;
                }
                break;
            case RIGHT:
                currentY++;
                if (currentY == rightBound + 1) {
                    direction = Direction.UP;
                    rightBound++;
                }
                break;
            }

        }

        return Math.abs(currentX) + Math.abs(currentY);
    }

    private static long dayThree2(int input) {
        int upperBound = 0;
        int lowerBound = 0;
        int leftBound = 0;
        int rightBound = 0;

        int currentX = 0;
        int currentY = 0;

        Direction direction = Direction.RIGHT;

        long currentNumber = 1;

        Map<Position, Long> values = new HashMap<>();
        values.put(new Position(0, 0), 1L);

        while (input >= currentNumber) {
            if (!(currentX == 0 && currentY == 0)) {
                currentNumber = 0;
                for (int i = -1; i < 2; i++) {
                    for (int j = -1; j < 2; j++) {
                        if (i == 0 && j == 0) {
                            continue;
                        }
                        Position checkingPos = new Position(currentX + i, currentY + j);
                        //                        System.out.println(String.format("For {%d, %d}, checking %s, got %d", currentX, currentY, checkingPos.toString(), values.getOrDefault(checkingPos, 0L)));
                        currentNumber += values.getOrDefault(checkingPos, 0L);
                    }
                }
            } else {
                //                System.out.println(String.format("Location %d, %d, number %d", currentX, currentY, currentNumber));
            }

            //            System.out.println(String.format("Location %d, %d, number %d", currentX, currentY, currentNumber));
            values.put(new Position(currentX, currentY), currentNumber);

            switch (direction) {
            case UP:
                currentX++;
                if (currentX == upperBound + 1) {
                    direction = Direction.LEFT;
                    upperBound++;
                }
                break;
            case DOWN:
                currentX--;
                if (currentX == lowerBound - 1) {
                    direction = Direction.RIGHT;
                    lowerBound--;
                }
                break;
            case LEFT:
                currentY--;
                if (currentY == leftBound - 1) {
                    direction = Direction.DOWN;
                    leftBound--;
                }
                break;
            case RIGHT:
                currentY++;
                if (currentY == rightBound + 1) {
                    direction = Direction.UP;
                    rightBound++;
                }
                break;
            }
        }

        return currentNumber;
    }

    static class Position {

        int x;
        int y;

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

    enum Direction {
        UP,
        DOWN,
        LEFT,
        RIGHT
    }
}
