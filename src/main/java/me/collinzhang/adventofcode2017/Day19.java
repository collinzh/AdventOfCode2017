package me.collinzhang.adventofcode2017;

import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class Day19 {

    private static final String TEST =
        "     |          \n" + "     |  +--+    \n" + "     A  |  C    \n" + " F---|----E|--+ \n" + "     |  |  |  D \n" + "     +B-+  +--+ \n" + "\n";

    public static void main(String[] args) {
        System.out.println(day19P1(new Scanner(TEST)));
        System.out.println(day19P1(Util.openScanner("/day19.txt")));
    }

    private static String day19P1(Scanner input) {
        Character[][] maze = parseMazeGrid(input);

        int startX = -1;
        for (int i = 0; i < maze[0].length; i++) {
            if (maze[0][i] == '|') {
                startX = i;
                break;
            }
        }

        int currentX = startX;
        int currentY = 0;
        Direction currentDirection = Direction.DOWN;
        StringBuilder seenLetters = new StringBuilder();

        for (; ; ) {
            char currentChar = maze[currentY][currentX];
            if (Character.isLetter(currentChar)) {
                seenLetters.append(currentChar);
            }

            int nextX = currentX;
            int nextY = currentY;
            switch (currentDirection) {
            case UP:
                nextY--;
                break;
            case DOWN:
                nextY++;
                break;
            case LEFT:
                nextX--;
                break;
            case RIGHT:
                nextX++;
                break;
            }

            if (nextY < 0 || nextY >= maze.length) {
                break;
            } else if (nextX < 0 || nextX >= maze[nextY].length) {
                break;
            }

            char nextChar = maze[nextY][nextX];
            if (nextChar == '+') {
                try {
                    for (int x = nextX - 1; x <= nextX + 1; x++) {
                        for (int y = nextY - 1; y <= nextY + 1; y++) {
                            if (y < 0 || y >= maze.length) {
                                continue;
                            }
                            if (x < 0 || x >= maze[y].length) {
                                continue;
                            }
                            if (nextX == x && nextY == y) {
                                continue;
                            }
                            if (currentX == x && currentY == y) {
                                continue;
                            }

                            if (maze[y][x] != ' ') {
                                if (x < nextX) {
                                    currentDirection = Direction.LEFT;
                                } else if (x > nextX) {
                                    currentDirection = Direction.RIGHT;
                                } else if (y > nextY) {
                                    currentDirection = Direction.DOWN;
                                } else if (y < nextY) {
                                    currentDirection = Direction.UP;
                                }
                                throw new NextStepFoundException();
                            }
                        }
                    }

                    break;
                }
                catch (NextStepFoundException ignore) {

                }

            }

            currentX = nextX;
            currentY = nextY;
        }

        return seenLetters.toString();
    }

    private static class NextStepFoundException extends Exception {

        private static final long serialVersionUID = -5526984271198848340L;
    }

    enum Direction {
        UP,
        DOWN,
        LEFT,
        RIGHT
    }

    /**
     * Returns maze map in Character[y][x]
     */
    private static Character[][] parseMazeGrid(Scanner input) {
        List<Character[]> parsedLines = new LinkedList<>();
        while (input.hasNext()) {
            String line = input.nextLine();
            List<Character> chars = new LinkedList<>();
            for (char c : line.toCharArray()) {
                chars.add(c);
            }
            parsedLines.add(chars.toArray(new Character[1]));
        }
        return parsedLines.toArray(new Character[1][1]);
    }
}
