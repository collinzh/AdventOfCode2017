package me.collinzhang.adventofcode2017;

import java.util.LinkedList;
import java.util.ListIterator;

public class Day17 {

    public static void main(String[] args) {
        System.out.println(day17P1(3));
        System.out.println(day17P1(337));

        System.out.println(day17P2(337));
    }

    private static int day17P2(int input) {

        int currentPosition = 0;
        int totalLength = 1;
        int number = 0;

        for (int i = 0; i < 50_000_000; i++) {
            currentPosition += input;
            currentPosition %= totalLength;
            if (currentPosition == 0) {
                number = i;
            }

            totalLength++;
            currentPosition++;

        }

        return number;
    }

    private static int day17P1(int input) {
        LinkedList<Integer> spinlock = new LinkedList<>();
        spinlock.add(0);

        ListIterator<Integer> iterator = spinlock.listIterator(0);

        for (int toInset = 1; toInset <= 2017; toInset++) {
            for (int i = 0; i < input; i++) {
                iterator.next();
                if (!iterator.hasNext() && i != input - 1) {
                    iterator = spinlock.listIterator(0);
                }
            }
            iterator.add(toInset);
            if (!iterator.hasNext()) {
                iterator = spinlock.listIterator(0);
            }
        }
        return iterator.hasNext() ? iterator.next() : spinlock.get(0);
    }
}
