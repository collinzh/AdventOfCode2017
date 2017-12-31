package me.collinzhang.adventofcode2017;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import me.collinzhang.adventofcode2017.support.PrimeNumberDetector;
import me.collinzhang.support.Util;

public class Day23 {

    public static void main(String[] args) {
        System.out.println(day23P1(Util.openScanner("/2017/day23.txt")));
        System.out.println(day23P2());
    }

    private static long day23P2() {
        // The problematic program is actually trying to find the number of composite number(not a prime number) between 107900 and 124900
        // Step by 17 (so from 1001 numbers in total)

        PrimeNumberDetector primeNumberDetector = new PrimeNumberDetector();

        int counter = 0;
        for (int i = 107900; i <= 124900; i += 17) {
            if (!primeNumberDetector.isPrimeNumber(i)) {
                counter++;
            }
        }

        return counter;
    }

    private static int day23P1(Scanner input) {
        Instruction[] instructions = parseInstructions(input);
        Map<String, Long> registers = new HashMap<>();

        int pointer = 0;

        int mulExecuted = 0;

        while (pointer < instructions.length) {
            Instruction instruction = instructions[pointer];
            pointer++;

            String command = instruction.command;
            Long val1 = getValue(instruction.param1, registers);
            Long val2 = getValue(instruction.param2, registers);

            if ("set".equals(command)) {
                registers.put(instruction.param1, val2);

            } else if ("sub".equals(command)) {
                registers.put(instruction.param1, val1 - val2);

            } else if ("mul".equals(command)) {
                registers.put(instruction.param1, val1 * val2);
                mulExecuted++;

            } else if ("jnz".equals(command)) {
                if (val1 != 0) {
                    pointer--;
                    pointer += val2;
                }
            }
        }

        return mulExecuted;
    }

    private static Long getValue(String name, Map<String, Long> registers) {
        if (name == null) {
            return null;
        }

        try {
            return Long.parseLong(name);
        }
        catch (Exception e) {
            return registers.getOrDefault(name, 0L);
        }
    }

    private static Instruction[] parseInstructions(Scanner input) {
        List<Instruction> instructions = new LinkedList<>();
        while (input.hasNext()) {
            String line = input.nextLine();
            if (line.startsWith("//")) {
                continue;
            }
            String[] words = line.split("\\s+");
            Instruction instruction = new Instruction();
            instruction.command = words[0];
            instruction.param1 = words[1];
            if (words.length > 2) {
                instruction.param2 = words[2];
            }

            instructions.add(instruction);
        }

        return instructions.toArray(new Instruction[instructions.size()]);
    }

    static class Instruction {

        String command;
        String param1;
        String param2;
    }
}
