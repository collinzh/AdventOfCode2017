package me.collinzhang.adventofcode2017;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day8 {

    private static final Pattern INSTRUCTION_PATTERN = Pattern.compile("^([a-z]+) ([a-z]+) ([-0-9]+) if ([a-z]+) ([<>=!]+) ([-0-9]+)$");

    private static final String TEST = "b inc 5 if a > 1\n" + "a inc 1 if b < 5\n" + "c dec -10 if a >= 1\n" + "c inc -20 if c == 10";

    public static void main(String[] args) {
        System.out.println(dayEight1(new Scanner(TEST)));
        System.out.println(dayEight1(Util.openScanner("/day8.txt")));

        System.out.println(dayEight2(new Scanner(TEST)));
        System.out.println(dayEight2(Util.openScanner("/day8.txt")));
    }

    private static Integer dayEight2(Scanner input) {
        Map<String, AtomicInteger> registers = new HashMap<>();
        Map<String, BiFunction<String, Integer, Boolean>> operands = new HashMap<>();
        operands.put("==", (name, val) -> registers.getOrDefault(name, new AtomicInteger(0)).get() == val);
        operands.put("<=", (name, val) -> registers.getOrDefault(name, new AtomicInteger(0)).get() <= val);
        operands.put(">=", (name, val) -> registers.getOrDefault(name, new AtomicInteger(0)).get() >= val);
        operands.put("!=", (name, val) -> registers.getOrDefault(name, new AtomicInteger(0)).get() != val);
        operands.put(">", (name, val) -> registers.getOrDefault(name, new AtomicInteger(0)).get() > val);
        operands.put("<", (name, val) -> registers.getOrDefault(name, new AtomicInteger(0)).get() < val);

        int max = Integer.MIN_VALUE;

        while (input.hasNext()) {
            String line = input.nextLine();
            Matcher matcher = INSTRUCTION_PATTERN.matcher(line.trim());
            if (!matcher.matches()) {
                System.out.println("Unrecognized line " + line);
                continue;
            }

            String regName = matcher.group(1);
            AtomicInteger reg = registers.computeIfAbsent(regName, (n) -> new AtomicInteger(0));
            BiConsumer<AtomicInteger, Integer> instruction = "inc".equals(matcher.group(2)) ? INC : DEC;
            Integer delta = Integer.parseInt(matcher.group(3));

            String condParam = matcher.group(4);
            BiFunction<String, Integer, Boolean> operand = operands.get(matcher.group(5));
            Integer condVal = Integer.parseInt(matcher.group(6));

            if (operand.apply(condParam, condVal)) {
                instruction.accept(reg, delta);
            }

            max = Integer.max(max, reg.get());
        }

        return max;
    }

    private static Integer dayEight1(Scanner input) {
        Map<String, AtomicInteger> registers = new HashMap<>();
        Map<String, BiFunction<String, Integer, Boolean>> operands = new HashMap<>();
        operands.put("==", (name, val) -> registers.getOrDefault(name, new AtomicInteger(0)).get() == val);
        operands.put("<=", (name, val) -> registers.getOrDefault(name, new AtomicInteger(0)).get() <= val);
        operands.put(">=", (name, val) -> registers.getOrDefault(name, new AtomicInteger(0)).get() >= val);
        operands.put("!=", (name, val) -> registers.getOrDefault(name, new AtomicInteger(0)).get() != val);
        operands.put(">", (name, val) -> registers.getOrDefault(name, new AtomicInteger(0)).get() > val);
        operands.put("<", (name, val) -> registers.getOrDefault(name, new AtomicInteger(0)).get() < val);

        while (input.hasNext()) {
            String line = input.nextLine();
            Matcher matcher = INSTRUCTION_PATTERN.matcher(line.trim());
            if (!matcher.matches()) {
                System.out.println("Unrecognized line " + line);
                continue;
            }

            String regName = matcher.group(1);
            AtomicInteger reg = registers.computeIfAbsent(regName, (n) -> new AtomicInteger(0));
            BiConsumer<AtomicInteger, Integer> instruction = "inc".equals(matcher.group(2)) ? INC : DEC;

            Integer delta = Integer.parseInt(matcher.group(3));

            String condParam = matcher.group(4);
            BiFunction<String, Integer, Boolean> condOperand = operands.get(matcher.group(5));
            Integer condVal = Integer.parseInt(matcher.group(6));

            if (condOperand.apply(condParam, condVal)) {
                instruction.accept(reg, delta);
            }
        }

        return registers.values().stream().max(Comparator.comparing(AtomicInteger::get, Comparator.naturalOrder())).map(AtomicInteger::get).orElse(null);
    }

    private static final BiConsumer<AtomicInteger, Integer> INC = AtomicInteger::getAndAdd;
    private static final BiConsumer<AtomicInteger, Integer> DEC = (ai, i) -> ai.addAndGet(-i);
}
