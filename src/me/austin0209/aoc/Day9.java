package me.austin0209.aoc;

import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

record Point(int x, int y) {}

class HeightMap {
    private int[] data;
    private int rows;
    private int columns;

    private HeightMap() {
    }

    public static HeightMap fromInput(String filename) throws IOException {
        HeightMap result = new HeightMap();
        var reader = new BufferedReader(new FileReader(filename));
        var rawLines = reader.lines().toList();

        result.data = rawLines.stream()
                .flatMapToInt(s -> s.chars().map(c -> c - '0'))
                .toArray();

        result.columns = rawLines.get(0).length();
        result.rows = rawLines.size();

        return result;
    }

    public int getRows() {
        return rows;
    }

    public int getColumns() {
        return columns;
    }

    public OptionalInt get(int x, int y) {
        if (x < 0 || x >= this.columns || y < 0 || y >= this.rows)
            return OptionalInt.empty();
        return OptionalInt.of(data[x + y * this.columns]);
    }

    public List<Integer> getNeighbors(int x, int y) {
        var neighbors = new ArrayList<OptionalInt>(4);

        neighbors.add(this.get(x + 1, y));
        neighbors.add(this.get(x, y + 1));
        neighbors.add(this.get(x - 1, y));
        neighbors.add(this.get(x, y - 1));

        return neighbors.stream()
                .filter(OptionalInt::isPresent)
                .mapToInt(OptionalInt::getAsInt)
                .boxed()
                .toList();
    }

    public List<Point> getLowPoints() {
        List<Point> result = new ArrayList<>();
        for (int y = 0; y < this.getRows(); y++) {
            for (int x = 0; x < this.getColumns(); x++) {
                if (this.isLowPoint(x, y)) {
                    result.add(new Point(x, y));
                }
            }
        }

        return result;
    }

    public boolean isLowPoint(int x, int y) {
        var value = this.get(x, y);
        if (value.isEmpty()) return false;

        var height = value.getAsInt();
        return getNeighbors(x, y).stream()
                .noneMatch(neighbor_height -> height >= neighbor_height);
    }

    private int getBasinSizeHelp(int x, int y, Set<Point> visited) {
        var location = new Point(x, y);
        if (visited.contains(location)) return 0;
        visited.add(new Point(x, y));

        if (this.get(x, y).isEmpty()) return 0;
        int value = this.get(x, y).getAsInt();
        if (value == 9) return 0;

        return getBasinSizeHelp(x + 1, y, visited) +
                getBasinSizeHelp(x, y + 1, visited) +
                getBasinSizeHelp(x - 1, y, visited) +
                getBasinSizeHelp(x, y - 1, visited) + 1;
    }

    public int getBasinSize(int x, int y) {
        if (get(x, y).orElseThrow() == 9) return 0;
        Set<Point> visited = new HashSet<>();
        return getBasinSizeHelp(x, y, visited);
    }
}

public class Day9 {
    public static void main(String[] args) throws IOException {
        var map = HeightMap.fromInput("input/day9.txt");

        // Solve part 1
        int riskLevel = 0;

        for (int y = 0; y < map.getRows(); y++) {
            for (int x = 0; x < map.getColumns(); x++) {
                if (map.isLowPoint(x, y)) {
                    riskLevel += map.get(x, y).orElseThrow() + 1;
                }
            }
        }

        System.out.println("Part 1 answer: " + riskLevel);

        // Solve part 2
        var part2Answer = map.getLowPoints().stream()
                .map(p -> map.getBasinSize(p.x(), p.y()))
                .sorted(Comparator.reverseOrder())
                .limit(3)
                .reduce((a, b) -> a * b);

        System.out.println("Part 2 answer: " + part2Answer.orElseThrow());
    }

    @Test
    void testBasin() throws IOException {
        var map = HeightMap.fromInput("input/day9sample.txt");

        var size = map.getBasinSize(0, 0);
        System.out.println(size);
        assert(size == 3);

        size = map.getBasinSize(9, 0);
        System.out.println(size);
        assert(size == 9);

        size = map.getBasinSize(2, 2);
        System.out.println(size);
        assert(size == 14);

        size = map.getBasinSize(6, 4);
        System.out.println(size);
        assert(size == 9);
    }
}

