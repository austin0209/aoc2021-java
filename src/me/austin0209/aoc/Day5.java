package me.austin0209.aoc;

import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Day5 {
    record Point(int x, int y) {}

    record Line(Point start, Point end) {
        boolean isHorizontal() { return start.y == end.y; }
        boolean isVertical() { return start.x == end.x; }

        List<Point> getPointRangeHorizontalOrVertical() {
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
            }

            throw new RuntimeException("This line is not horizontal or vertical...");
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
        return rawLines.map(Day5::stringToLine).toList();
    }

    static int getIntersectionCountHorizontalOrVertical(Line line1, Line line2) {
        var line1Points = line1.getPointRangeHorizontalOrVertical();
        var line2Points = line2.getPointRangeHorizontalOrVertical();

        assert line1Points != null;
        assert line2Points != null;

        int count = 0;
        for (var p1 : line1Points) {
            for (var p2 : line2Points) {
                if (p1.equals(p2)) count++;
            }
        }

        return count;
    }

    static int solveDay1(List<Line> input) {
        List<Line> lines = input.stream()
                .filter(l -> l.isVertical() || l.isHorizontal())
                .toList();

        int count = 0;
        // TODO: consider the condition where AT LEAST 2 lines must overlap.
        // (Probably something with double counting going wrong here)
        for (int i = 0; i < lines.size() - 1; i++) {
            for (int j = i + 1; j < lines.size(); j++) {
                count += getIntersectionCountHorizontalOrVertical(lines.get(i), lines.get(j));
            }
        }

        return count;
    }

    public static void main(String[] args) throws FileNotFoundException {
        System.out.println(solveDay1(parseInput("input/day5.txt")));
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
        assert(solveDay1(parseInput("input/day5sample.txt")) == 5);
    }
}
