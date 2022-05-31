package me.austin0209.aoc;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;
import java.util.function.BiFunction;

public class Day15 {
    record SearchNode(int x, int y, float totalCost, float hValue, SearchNode predecessor) {
        static SearchNode of(
                int x,
                int y,
                float cost,
                BiFunction<Integer, Integer, Float> hFunction,
                SearchNode predecessor) {
            float totalCost;

            if (predecessor == null) {
                totalCost = cost;
            } else {
                totalCost = predecessor.totalCost + cost;
            }

            return new SearchNode(x, y, totalCost, hFunction.apply(x, y), predecessor);
        }

        public float fValue() {
            return this.hValue + this.totalCost;
        }

        @Override
        public boolean equals(Object o) {
            if (!(o instanceof SearchNode other)) return false;

            return this.x == other.x && this.y == other.y;
        }

        @Override
        public int hashCode() {
            return ("" + this.x + this.y).hashCode();
        }

        @Override
        public String toString() {
            return "" + this.x + " " + this.y;
        }
    }

    List<List<Integer>> riskLevels;

    OptionalInt get(int x, int y) {
        try {
            return OptionalInt.of(riskLevels.get(y).get(x));
        } catch (Exception e) {
            return OptionalInt.empty();
        }
    }

    float h(int x, int y) {
        int xDest = riskLevels.get(0).size() - 1;
        int yDest = riskLevels.size() - 1;

        int a = Math.abs(x - xDest);
        int b = Math.abs(y - yDest);

        return (float) Math.sqrt(a * a + b * b);
    }

    boolean inBounds(int x, int y) {
        return x >= 0 && x < riskLevels.get(0).size() && y >= 0 && y < riskLevels.size();
    }

    void solvePart1() {
        Queue<SearchNode> fringe =
                new PriorityQueue<>(100, (s1, s2) -> Float.compare(s1.fValue(), s2.fValue()));
        Set<SearchNode> searched = new HashSet<>();

        var current =
                SearchNode.of(0, 0, this.get(0, 0).orElseThrow(), this::h, null);

        int xDest = riskLevels.get(0).size() - 1;
        int yDest = riskLevels.size() - 1;

        while (current.x != xDest || current.y != yDest) {
            searched.add(current);

            if (inBounds(current.x + 1, current.y)) {
                var cost = this.get(current.x + 1, current.y).orElseThrow();
                var node = SearchNode.of(current.x + 1, current.y, cost, this::h, current);
                if (!searched.contains(node)) fringe.add(node);
            }

            if (inBounds(current.x, current.y + 1)) {
                var cost = this.get(current.x, current.y + 1).orElseThrow();
                var node = SearchNode.of(current.x, current.y + 1, cost, this::h, current);
                if (!searched.contains(node)) fringe.add(node);
            }

            if (inBounds(current.x - 1, current.y)) {
                var cost = this.get(current.x - 1, current.y).orElseThrow();
                var node = SearchNode.of(current.x - 1, current.y, cost, this::h, current);
                if (!searched.contains(node)) fringe.add(node);
            }

            if (inBounds(current.x, current.y - 1)) {
                var cost = this.get(current.x, current.y - 1).orElseThrow();
                var node = SearchNode.of(current.x, current.y - 1, cost, this::h, current);
                if (!searched.contains(node)) fringe.add(node);
            }

            current = fringe.remove();
        }

        int answer = 0;


        while (current.predecessor != null) {
            answer += this.get(current.x, current.y).orElseThrow();
            current = current.predecessor;
        }

        System.out.println("Part 1 answer: " + answer);
    }

    static Day15 fromInput(String filename) throws FileNotFoundException {
        var result = new Day15();
        result.riskLevels = new ArrayList<>();
        var reader = new BufferedReader(new FileReader(filename));
        var lines = reader.lines().toList();

        for (var line : lines) {
            var row = line.chars()
                    .map(i -> i - '0')
                    .boxed()
                    .toList();

            result.riskLevels.add(row);
        }

        return result;
    }

    public static void main(String[] args) throws FileNotFoundException {
        var part1 = Day15.fromInput("input/day15.txt");
        part1.solvePart1();
    }
}
