package me.collinzhang.adventofcode2017;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.IntUnaryOperator;
import java.util.stream.Collectors;

import javax.xml.bind.DatatypeConverter;

public class Day10 {

    private static final String DAY_10_TEST = "3,4,1,5";
    private static final String DAY_10 = "88,88,211,106,141,1,78,254,2,111,77,255,90,0,54,205";

    public static void main(String[] args) {
        System.out.println(day10p1(DAY_10_TEST, 5));
        System.out.println(day10p1(DAY_10, 256));

        System.out.println(day10p2(""));
        System.out.println(day10p2(DAY_10));
    }

    static String day10p2(String input) {
        int length = 256;
        byte[] numbers = new byte[256];
        for (int i = 0; i < length; i++) {
            numbers[i] = (byte) i;
        }
        input = input.trim();

        List<Byte> lengthInput = new ArrayList<>(input.length() + 5);
        for (byte c : input.getBytes(StandardCharsets.US_ASCII)) {
            lengthInput.add(c);
        }
        lengthInput.add(Byte.valueOf("17"));
        lengthInput.add(Byte.valueOf("31"));
        lengthInput.add(Byte.valueOf("73"));
        lengthInput.add(Byte.valueOf("47"));
        lengthInput.add(Byte.valueOf("23"));

        int position = 0;
        int skipSize = 0;

        for (int round = 0; round < 64; round++) {
            for (Byte len : lengthInput) {

                byte[] reversed = new byte[len];

                for (int i = 0; i < len; i++) {
                    reversed[i] = numbers[(position + len - 1 - i) % length];
                }
                for (int i = 0; i < len; i++) {
                    numbers[(position + i) % length] = reversed[i];
                }
                position = (position + len + skipSize) % length;
                skipSize++;
            }
        }

        byte[] result = new byte[16];
        for (int i = 0; i < 16; i++) {
            int start = i * 16;
            byte hash = numbers[start];
            for (int j = 1; j < 16; j++) {
                hash ^= numbers[start + j];
            }
            result[i] = hash;
        }
        return DatatypeConverter.printHexBinary(result).toLowerCase();
    }

    private static int day10p1(String input, int length) {
        int[] numbers = new int[length];
        Arrays.setAll(numbers, IntUnaryOperator.identity());

        int position = 0;
        int skipSize = 0;

        List<Integer> lengthInput = Arrays.stream(input.split(",")).map(String::trim).map(Integer::parseInt).collect(Collectors.toList());
        for (Integer len : lengthInput) {

            int[] reversed = new int[len];

            for (int i = 0; i < len; i++) {
                reversed[i] = numbers[(position + len - 1 - i) % length];
            }
            for (int i = 0; i < len; i++) {
                numbers[(position + i) % length] = reversed[i];
            }
            position = (position + len + skipSize) % length;
            skipSize++;
        }

        return numbers[0] * numbers[1];
    }
}
