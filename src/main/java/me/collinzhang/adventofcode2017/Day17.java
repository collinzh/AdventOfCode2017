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
        LinkedList<Integer> spinlock = new LinkedList<>();
        spinlock.add(0);

        ListIterator<Integer> iterator = spinlock.listIterator(0);

        for (int toInset = 1; toInset <= 50_000_000; toInset++) {
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

        iterator = spinlock.listIterator(0);
        for (; ; ) {
            if (iterator.next() == 0) {
                break;
            }
        }

        return iterator.hasNext() ? iterator.next() : spinlock.get(0);
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
