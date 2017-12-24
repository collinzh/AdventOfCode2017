package me.collinzhang.adventofcode2017.support;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Detect if a number is a prime number, and cache all prime number found to sped-up future searches
 */
public class PrimeNumberDetector {

    private Set<Integer> foundPrime = new LinkedHashSet<>();
    private int largest = 12;

    public PrimeNumberDetector() {
        foundPrime.add(2);
        foundPrime.add(3);
        foundPrime.add(5);
        foundPrime.add(7);
    }

    public boolean isPrimeNumber(int number) {

        if (number <= largest) {
            return foundPrime.contains(number);
        }

        for (int i = largest + 1; i <= number; i++) {
            final int cur = i;

            // If the number is not evenly divisible by all known prime number
            if (foundPrime.stream().noneMatch(p -> (cur % p) == 0)) {
                foundPrime.add(i);
            }
        }

        largest = number;
        return foundPrime.contains(number);
    }
}
