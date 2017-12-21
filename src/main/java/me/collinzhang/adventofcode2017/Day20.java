package me.collinzhang.adventofcode2017;

import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Day20 {

    private static final Pattern LINE_PATTERN = Pattern.compile("p=<(.*)>,v=<(.*)>,a=<(.*)>");
    private static final Pattern POSITION_PATTERN = Pattern.compile("([-0-9]+),([-0-9]+),([-0-9]+)");

    private static final String TEST = "p=< 3,0,0>, v=< 2,0,0>, a=<-1,0,0>\n" + "p=< 4,0,0>, v=< 0,0,0>, a=<-2,0,0>";
    private static final String TEST2 = "p=<-6,0,0>, v=< 3,0,0>, a=< 0,0,0>\n" + "p=<-4,0,0>, v=< 2,0,0>, a=< 0,0,0>\n" + "p=<-2,0,0>, v=< 1,0,0>, a=< 0,0,0>\n"
        + "p=< 3,0,0>, v=<-1,0,0>, a=< 0,0,0>";

    private static final Logger LOGGER = Logger.getAnonymousLogger();

    public static void main(String[] args) {
        System.out.println(day20P1(new Scanner(TEST)));
        System.out.println(day20P1(Util.openScanner("/day20.txt")));

        System.out.println(day20P2(new Scanner(TEST2), 10));
        System.out.println(day20P2(Util.openScanner("/day20.txt"), 100_000_000L));
    }

    private static int day20P2(Scanner input, long rounds) {
        Set<Particle> particles = new LinkedHashSet<>();

        while (input.hasNext()) {
            String line = input.nextLine().replace(" ", "");
            Matcher lineMatcher = LINE_PATTERN.matcher(line);
            if (!lineMatcher.matches()) {
                continue;
            }

            Matcher pMatcher = POSITION_PATTERN.matcher(lineMatcher.group(1));
            if (!pMatcher.matches()) {
                continue;
            }

            Matcher vMatcher = POSITION_PATTERN.matcher(lineMatcher.group(2));
            if (!vMatcher.matches()) {
                continue;
            }

            Matcher aMatcher = POSITION_PATTERN.matcher(lineMatcher.group(3));
            if (!aMatcher.matches()) {
                continue;
            }

            particles.add(new Particle(matcherToCoordinate(pMatcher), matcherToCoordinate(vMatcher), matcherToCoordinate(aMatcher)));
        }

        int lastSampled = 0;
        int stabledRounds = 0;
        for (long i = 0; i < rounds; i++) {
            Set<Particle> toRemove = new HashSet<>();
            Map<Coordinate, Particle> currentPositions = new HashMap<>();

            for (Particle particle : particles) {
                if (currentPositions.containsKey(particle.position)) {
                    toRemove.add(currentPositions.get(particle.position));
                    toRemove.add(particle);
                    continue;
                }

                currentPositions.put(particle.position, particle);
            }

            // Rebuild HashSet to make sure the hashes are updated after particles are moved
            particles = particles.stream().filter(p -> !toRemove.contains(p)).peek(Particle::move).collect(Collectors.toSet());

            // Take a sample every 1000 rounds
            if (i % 1000 == 0) {
                if (lastSampled != particles.size()) {
                    lastSampled = particles.size();
                    stabledRounds = 0;
                } else {
                    stabledRounds++;
                    // If sampled result hasn't changed for 5 times, break the loop
                    if (stabledRounds == 5) {
                        break;
                    }
                }
            }
        }

        return particles.size();
    }

    private static Coordinate matcherToCoordinate(Matcher matcher) {
        return new Coordinate(Integer.parseInt(matcher.group(1)), Integer.parseInt(matcher.group(2)), Integer.parseInt(matcher.group(3)));
    }

    private static int day20P1(Scanner scanner) {
        List<Coordinate> accelerations = new LinkedList<>();
        int id = 0;
        while (scanner.hasNext()) {
            String line = scanner.nextLine().replace(" ", "");
            Matcher lineMatcher = LINE_PATTERN.matcher(line);
            if (!lineMatcher.matches()) {
                continue;
            }
            Matcher posMatcher = POSITION_PATTERN.matcher(lineMatcher.group(3));
            if (!posMatcher.matches()) {
                continue;
            }

            Coordinate coordinate = new Coordinate(id, Integer.parseInt(posMatcher.group(1)), Integer.parseInt(posMatcher.group(2)),
                Integer.parseInt(posMatcher.group(3)));
            accelerations.add(coordinate);
            id++;
        }

        accelerations = accelerations.stream().sorted(Comparator.comparingInt(Coordinate::absoluteEdgeSum)).collect(Collectors.toList());
        return accelerations.get(0).id;
    }

    static class Particle {

        Coordinate position;
        Coordinate velocity;
        Coordinate acceleration;

        Particle(Coordinate position, Coordinate velocity, Coordinate acceleration) {
            this.position = position;
            this.velocity = velocity;
            this.acceleration = acceleration;
        }

        void move() {
            velocity.add(acceleration);
            position.add(velocity);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o)
                return true;
            if (!(o instanceof Particle))
                return false;

            Particle particle = (Particle) o;

            if (position != null ? !position.equals(particle.position) : particle.position != null)
                return false;
            if (velocity != null ? !velocity.equals(particle.velocity) : particle.velocity != null)
                return false;
            return acceleration != null ? acceleration.equals(particle.acceleration) : particle.acceleration == null;
        }

        @Override
        public int hashCode() {
            int result = position != null ? position.hashCode() : 0;
            result = 31 * result + (velocity != null ? velocity.hashCode() : 0);
            result = 31 * result + (acceleration != null ? acceleration.hashCode() : 0);
            return result;
        }
    }

    static class Coordinate {

        int id;
        int x;
        int y;
        int z;

        Coordinate(int x, int y, int z) {
            this(-1, x, y, z);
        }

        Coordinate(int id, int x, int y, int z) {
            this.id = id;
            this.x = x;
            this.y = y;
            this.z = z;
        }

        void add(Coordinate coordinate) {
            this.x += coordinate.x;
            this.y += coordinate.y;
            this.z += coordinate.z;
        }

        int absoluteEdgeSum() {
            return Math.abs(x) + Math.abs(y) + Math.abs(z);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o)
                return true;
            if (!(o instanceof Coordinate))
                return false;

            Coordinate that = (Coordinate) o;

            if (x != that.x)
                return false;
            if (y != that.y)
                return false;
            return z == that.z;
        }

        @Override
        public int hashCode() {
            int result = x;
            result = 31 * result + y;
            result = 31 * result + z;
            return result;
        }
    }
}
