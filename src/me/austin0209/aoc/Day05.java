package me.austin0209.aoc;

import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Day05 {
    static int worldWidth = 1500;
    static int worldHeight = 1500;

    record Point(int x, int y) {}

    record Line(Point start, Point end) {
        boolean isHorizontal() { return start.y == end.y; }
        boolean isVertical() { return start.x == end.x; }
        boolean isDiagonal() { return Math.abs(start.x - end.x) == Math.abs(start.y - end.y); }

        List<Point> getPointRange() {
            if (isHorizontal()) {
                int first = Math.min(start.x, end.x);
                int last = Math.max(start.x, end.x);
                return IntStream.rangeClosed(first, last)
                        .mapToObj(i -> new Point(i, end.y))
                        .toList();
            } else if (isVertical()) {
                int first = Math.min(start.y, end.y);
                int last = Math.max(start.y, end.y);
                return IntStream.rangeClosed(first, last)
                        .mapToObj(i -> new Point(start.x, i))
                        .toList();
            } else if (isDiagonal()) {
                var topLeft = new Point(Math.min(start.x, end.x), Math.min(start.y, end.y));
                var bottomRight = new Point(Math.max(start.x, end.x), Math.max(start.y, end.y));

                var xRange = IntStream.rangeClosed(topLeft.x, bottomRight.x).toArray();
                var yRange = IntStream.rangeClosed(topLeft.y, bottomRight.y).toArray();

                assert(xRange.length == yRange.length);

                boolean backSlash = start.equals(topLeft) || end.equals(topLeft);
                if (!backSlash) {
                    var reversed = new ArrayList<Integer>();
                    for (int i = xRange.length - 1; i >= 0; i--) {
                        reversed.add(xRange[i]);
                    }
                    xRange = reversed.stream().mapToInt(Integer::intValue).toArray();
                }

                List<Point> points = new ArrayList<>();

                for (int i = 0; i < xRange.length; i++) {
                    points.add(new Point(xRange[i], yRange[i]));
                }

                return points;
            }

            throw new RuntimeException("This line is not horizontal, vertical, or diagonal...");
        }
    }

    static Line stringToLine(String string) {
        var rawPoints = string.split(" -> ");

        var startPointData = Stream.of(rawPoints[0].split(","))
                .mapToInt(Integer::parseInt)
                .toArray();

        var endPointData = Stream.of(rawPoints[1].split(","))
                .mapToInt(Integer::parseInt)
                .toArray();

        var startPoint = new Point(startPointData[0], startPointData[1]);
        var endPoint = new Point(endPointData[0], endPointData[1]);

        return new Line(startPoint, endPoint);
    }

    static List<Line> parseInput(String fileName) throws FileNotFoundException {
        var rawLines = new BufferedReader(new FileReader(fileName)).lines();
        return rawLines.map(Day05::stringToLine).toList();
    }

    static long solvePart1(List<Line> input) {
        List<Line> lines = input.stream()
                .filter(l -> l.isVertical() || l.isHorizontal())
                .toList();

        List<Integer> world = new ArrayList<>(worldWidth * worldHeight);
        while(world.size() < worldWidth * worldHeight) world.add(0);

        for (var line : lines) {
            var points = line.getPointRange();
            for (Point point : points) {
                var index = point.x + point.y * worldWidth;
                world.set(index, world.get(index) + 1);
            }
        }

        return world.stream()
                .filter(i -> i >= 2)
                .count();
    }

    static long solvePart2(List<Line> input) {
        List<Line> lines = input.stream()
                .filter(l -> l.isVertical() || l.isHorizontal() || l.isDiagonal())
                .toList();

        List<Integer> world = new ArrayList<>(worldWidth * worldHeight);
        while(world.size() < worldWidth * worldHeight) world.add(0);

        for (var line : lines) {
            var points = line.getPointRange();
            for (Point point : points) {
                var index = point.x + point.y * worldWidth;
                world.set(index, world.get(index) + 1);
            }
        }

        return world.stream()
                .filter(i -> i >= 2)
                .count();
    }

    public static void main(String[] args) throws FileNotFoundException {
        System.out.println(solvePart1(parseInput("input/day5.txt")));
        System.out.println(solvePart2(parseInput("input/day5.txt")));
    }

    @Test
    void testParseInput() throws FileNotFoundException {
        var correctLines = List.of(new Line[] {
                new Line(new Point(0, 9), new Point(5, 9)),
                new Line(new Point(8, 0), new Point(0, 8)),
                new Line(new Point(9, 4), new Point(3, 4)),
                new Line(new Point(2, 2), new Point(2, 1)),
                new Line(new Point(7, 0), new Point(7, 4)),
                new Line(new Point(6, 4), new Point(2, 0)),
                new Line(new Point(0, 9), new Point(2, 9)),
                new Line(new Point(3, 4), new Point(1, 4)),
                new Line(new Point(0, 0), new Point(8, 8)),
                new Line(new Point(5, 5), new Point(8, 2)),
        });

        var myLines = parseInput("input/day5sample.txt");

        assert correctLines.equals(myLines);
    }

    @Test
    void testHorizontal() {
        var line1 = new Line(new Point(0, 4), new Point(4, 4));
        var line2 = new Line(new Point(0, 4), new Point(4, 5));

        assert(line1.isHorizontal());
        assert(!line2.isHorizontal());
    }

    @Test
    void testVertical() {
        var line1 = new Line(new Point(0, 4), new Point(0, 8));
        var line2 = new Line(new Point(0, 4), new Point(4, 5));

        assert(line1.isVertical());
        assert(!line2.isVertical());
    }

    @Test
    void testPart1() throws FileNotFoundException {
        System.out.println(solvePart1(parseInput("input/day5sample.txt")));
        assert solvePart1(parseInput("input/day5sample.txt")) == 5;
    }

    @Test
    void testPart2() throws FileNotFoundException {
        System.out.println(solvePart2(parseInput("input/day5sample.txt")));
        assert solvePart2(parseInput("input/day5sample.txt")) == 12;
    }
}
