package me.austin0209.aoc;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;

public class Day15 {
    record Vector2(int x, int y) {}

    static class SearchNode {
        float hValue;
        float gValue;
        Point predecessor;

        SearchNode() {
            this.hValue = Float.MAX_VALUE;
            this.gValue = Float.MAX_VALUE;
            this.predecessor = null;
        }

        float getFValue() {
            if (Float.compare(this.hValue, Float.MAX_VALUE) == 0 || Float.compare(this.gValue, Float.MAX_VALUE) == 0) {
                return Float.MAX_VALUE;
            }

            return hValue + gValue;
        }
    }

    List<List<Integer>> riskLevels;
    Map<Point, SearchNode> nodes;

    OptionalInt get(int x, int y) {
        try {
            return OptionalInt.of(riskLevels.get(y).get(x));
        } catch (Exception e) {
            return OptionalInt.empty();
        }
    }

    OptionalInt get(Point p) { return get(p.x(), p.y()); }

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

    int fringeComparator(Point a, Point b) {
        var fA = this.nodes.get(a).getFValue();
        var fB = this.nodes.get(b).getFValue();
        return Float.compare(fA, fB);
    }

    void addToFringe(Point current, Point p, Queue<Point> fringe) {
        if (!inBounds(p.x(), p.y())) return;

        var cost = this.get(p).orElseThrow();
        var node = this.nodes.get(p);
        var currentNode = this.nodes.get(current);
        assert(node != null && currentNode != null);

        var tentativeG = currentNode.gValue + cost;
        if (tentativeG < node.gValue) {
            node.predecessor = current;
            node.gValue = tentativeG;
            node.hValue = h(p.x(), p.y());
            if (!fringe.contains(p)) {
                fringe.add(p);
            }
        }
    }

    void solvePart1() {
        Queue<Point> fringe = new PriorityQueue<>(100, this::fringeComparator);
        var start = new Point(0, 0);
        this.nodes.get(start).gValue = 0;
        this.nodes.get(start).hValue = this.h(0, 0);
        fringe.add(start);

        int xDest = riskLevels.get(0).size() - 1;
        int yDest = riskLevels.size() - 1;
        var destination = new Point(xDest, yDest);

        Point current = null;

        while (!fringe.isEmpty()) {
            current = fringe.remove();
            if (current.equals(destination)) {
                break;
            }

            addToFringe(current, new Point(current.x() + 1, current.y()), fringe);
            addToFringe(current, new Point(current.x(), current.y() + 1), fringe);
            addToFringe(current, new Point(current.x() - 1, current.y()), fringe);
            addToFringe(current, new Point(current.x(), current.y() - 1), fringe);
        }

        int answer = 0;
        assert(current != null);
        var currentNode = this.nodes.get(current);
        while (currentNode.predecessor != null) {
            answer += this.get(current.x(), current.y()).orElseThrow();
            current = currentNode.predecessor;
            currentNode = this.nodes.get(current);
        }

        System.out.println("Part 1 answer: " + answer);
    }

    static Day15 fromInput(String filename) throws FileNotFoundException {
        var result = new Day15();

        result.nodes = new HashMap<>();
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

        for (int y = 0; y < result.riskLevels.size(); y++) {
            for (int x = 0; x < result.riskLevels.get(0).size(); x++) {
                result.nodes.put(new Point(x, y), new SearchNode());
            }
        }

        return result;
    }

    public static void main(String[] args) throws FileNotFoundException {
        var part1 = Day15.fromInput("input/day15.txt");
        part1.solvePart1();
    }
}
