package me.collinzhang.adventofcode2017;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import me.collinzhang.support.Util;

public class Day21 {

    private static final Pattern RULE_PATTERN = Pattern.compile("^(.+)=>(.+)$");

    public static void main(String[] args) {
        System.out.println(day21P1_2(Util.openScanner("/2017/day21.txt"), 5));
        System.out.println(day21P1_2(Util.openScanner("/2017/day21.txt"), 18));
    }

    private static int day21P1_2(Scanner input, int rounds) {
        Block canvas = parseBlock(".#./..#/###");
        Map<Block, Block> transformations = new HashMap<>();
        while (input.hasNext()) {
            String line = input.nextLine().replace(" ", "");
            Matcher matcher = RULE_PATTERN.matcher(line);
            if (!matcher.matches()) {
                continue;
            }

            String inputPattern = matcher.group(1);
            String outputPattern = matcher.group(2);
            Block inputBlock = parseBlock(inputPattern);
            Block outputBlock = parseBlock(outputPattern);
            transformations.put(inputBlock, outputBlock);

            for (int i = 0; i < 4; i++) {
                Block transformed = inputBlock.makeRotated();
                Block flipped = inputBlock.makeFlipped();

                transformations.put(transformed, outputBlock);
                transformations.put(flipped, outputBlock);
                inputBlock = transformed;
            }
        }

        for (int i = 0; i < rounds; i++) {
            List<Block> splitted = null;
            if (canvas.getDimension() % 2 == 0) {
                splitted = splitBlock(canvas, 2);
            } else if (canvas.getDimension() % 3 == 0) {
                splitted = splitBlock(canvas, 3);
            } else {
                throw new RuntimeException();
            }

            splitted = splitted.stream().map(transformations::get).collect(Collectors.toList());

            canvas = mergeBlocks(splitted);
        }

        int count = 0;
        for (int i = 0; i < canvas.getDimension(); i++) {
            for (int j = 0; j < canvas.getDimension(); j++) {
                if (canvas.grid[i][j]) {
                    count++;
                }
            }
        }

        return count;
    }

    private static List<Block> splitBlock(Block block, int newDimension) {
        int edgeLen = block.getDimension() / newDimension;

        List<Block> blocks = new ArrayList<>(edgeLen * edgeLen);
        for (int i = 0; i < edgeLen * edgeLen; i++) {
            Block next = new Block(newDimension);
            int xOffset = i % edgeLen * newDimension;
            int yOffset = i / edgeLen * newDimension;

            for (int x = 0; x < newDimension; x++) {
                for (int y = 0; y < newDimension; y++) {
                    next.grid[x][y] = block.grid[x + xOffset][y + yOffset];
                }
            }

            blocks.add(next);
        }

        return blocks;
    }

    private static Block mergeBlocks(List<Block> blocks) {
        int edgeLen = (int) Math.sqrt(blocks.size());
        int unitDimension = blocks.get(0).getDimension();

        Block block = new Block(edgeLen * unitDimension);
        for (int i = 0; i < blocks.size(); i++) {
            Block next = blocks.get(i);
            int xOffset = i % edgeLen * unitDimension;
            int yOffset = i / edgeLen * unitDimension;

            for (int x = 0; x < next.getDimension(); x++) {
                for (int y = 0; y < next.getDimension(); y++) {
                    block.grid[x + xOffset][y + yOffset] = next.grid[x][y];
                }
            }
        }

        return block;
    }

    private static Block parseBlock(String str) {
        String[] rows = str.split("/");
        Block block = new Block(rows.length);

        for (int i = 0; i < rows.length; i++) {
            for (int j = 0; j < rows[i].length(); j++) {
                block.grid[i][j] = rows[i].charAt(j) == '#';
            }
        }

        return block;
    }

    static class Block {

        boolean[][] grid;

        Block(int dimension) {
            this.grid = new boolean[dimension][dimension];
        }

        int getDimension() {
            return grid.length;
        }

        Block makeRotated() {
            int len = grid.length;
            boolean[][] newGrid = new boolean[len][len];
            for (int i = 0; i < len; i++) {
                for (int j = 0; j < len; j++) {
                    newGrid[i][j] = grid[len - j - 1][i];
                }
            }

            Block n = new Block(len);
            n.grid = newGrid;
            return n;
        }

        Block makeFlipped() {
            int len = grid.length;
            boolean[][] newGrid = new boolean[len][len];
            for (int i = 0; i < len; i++) {
                for (int j = 0; j < len; j++) {
                    newGrid[i][j] = grid[i][len - j - 1];
                }
            }

            Block n = new Block(len);
            n.grid = newGrid;
            return n;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o)
                return true;
            if (!(o instanceof Block))
                return false;

            Block block = (Block) o;

            return Arrays.deepEquals(grid, block.grid);
        }

        @Override
        public int hashCode() {
            return Arrays.deepHashCode(grid);
        }

        @Override
        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();
            for (int y = 0; y < grid.length; y++) {
                for (int x = 0; x < grid[y].length; x++) {
                    stringBuilder.append(grid[x][y] ? '#' : '.');
                }
                stringBuilder.append('/');
            }
            return stringBuilder.toString();
        }
    }
}

