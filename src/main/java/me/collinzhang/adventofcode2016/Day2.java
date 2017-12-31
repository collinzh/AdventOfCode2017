package me.collinzhang.adventofcode2016;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import me.collinzhang.support.Position;
import me.collinzhang.support.Util;

public class Day2 {

    private static final String TEST = "ULL\n" + "RRDDD\n" + "LURDL\n" + "UUUUD";

    public static void main(String[] args) {
        System.out.println(day2P1(new Scanner(TEST)));
        System.out.println(day2P1(Util.openScanner("/2016/day2.txt")));

        System.out.println(day2P2(new Scanner(TEST)));
        System.out.println(day2P2(Util.openScanner("/2016/day2.txt")));
    }

    private static String day2P2(Scanner input) {
        int actualX = 0;
        int actualY = 2;

        StringBuilder code = new StringBuilder();
        Map<Position, String> keypad = getP2KeyPad();

        while (input.hasNext()) {
            String line = input.nextLine();

            for (char c : line.toCharArray()) {
                int x = actualX;
                int y = actualY;

                switch (c) {
                case 'U':
                    y--;
                    break;
                case 'D':
                    y++;
                    break;
                case 'L':
                    x--;
                    break;
                case 'R':
                    x++;
                    break;
                default:
                    throw new RuntimeException();
                }

                if (keypad.containsKey(new Position(x, y))) {
                    actualX = x;
                    actualY = y;
                }
            }

            code.append(keypad.get(new Position(actualX, actualY)));
        }

        return code.toString();
    }

    private static String day2P1(Scanner input) {
        int x = 1;
        int y = 1;

        StringBuilder code = new StringBuilder();
        Map<Position, Integer> keypad = getP1KeyPad();

        while (input.hasNext()) {
            String line = input.nextLine();

            for (char c : line.toCharArray()) {
                switch (c) {
                case 'U':
                    y--;
                    break;
                case 'D':
                    y++;
                    break;
                case 'L':
                    x--;
                    break;
                case 'R':
                    x++;
                }
                y = Math.max(0, Math.min(2, y));
                x = Math.max(0, Math.min(2, x));
            }

            code.append(keypad.get(new Position(x, y)));
        }

        return code.toString();
    }

    private static Map<Position, Integer> getP1KeyPad() {
        Map<Position, Integer> keypad = new HashMap<>();
        keypad.put(new Position(0, 0), 1);
        keypad.put(new Position(1, 0), 2);
        keypad.put(new Position(2, 0), 3);
        keypad.put(new Position(0, 1), 4);
        keypad.put(new Position(1, 1), 5);
        keypad.put(new Position(2, 1), 6);
        keypad.put(new Position(0, 2), 7);
        keypad.put(new Position(1, 2), 8);
        keypad.put(new Position(2, 2), 9);
        return keypad;
    }

    private static Map<Position, String> getP2KeyPad() {
        Map<Position, String> keypad = new HashMap<>();
        keypad.put(new Position(2, 0), "1");
        keypad.put(new Position(1, 1), "2");
        keypad.put(new Position(2, 1), "3");
        keypad.put(new Position(3, 1), "4");
        keypad.put(new Position(0, 2), "5");
        keypad.put(new Position(1, 2), "6");
        keypad.put(new Position(2, 2), "7");
        keypad.put(new Position(3, 2), "8");
        keypad.put(new Position(4, 2), "9");
        keypad.put(new Position(1, 3), "A");
        keypad.put(new Position(2, 3), "B");
        keypad.put(new Position(3, 3), "C");
        keypad.put(new Position(2, 4), "D");
        return keypad;
    }
}
