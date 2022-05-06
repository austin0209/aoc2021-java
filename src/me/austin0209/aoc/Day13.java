package me.austin0209.aoc;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class Day13 {
    record Paper(List<List<Boolean>> rows) {
        boolean get(int row, int col) {
            return rows.get(row).get(col);
        }

        int getNumRows() {
            return rows.size();
        }

        int getNumCols() {
            return rows.get(0).size();
        }

        Paper rotatedClockwise() {
            var rotatedRows = new ArrayList<List<Boolean>>();

            var resultRows = this.getNumCols();
            var resultCols = this.getNumRows();

            for (int i = 0; i < resultRows; i++) {
                var newRow = new ArrayList<Boolean>(resultCols);
                for (int j = 0; j < resultCols; j++) {
                    newRow.add(this.get(this.getNumRows() - j - 1, i));
                }

                rotatedRows.add(newRow);
            }

            return new Paper(rotatedRows);
        }

        Paper rotatedCounterClockwise() {
            var rotatedRows = new ArrayList<List<Boolean>>();

            var resultRows = this.getNumCols();
            var resultCols = this.getNumRows();

            for (int i = 0; i < resultRows; i++) {
                var newRow = new ArrayList<Boolean>(resultCols);
                for (int j = 0; j < resultCols; j++) {
                    newRow.add(this.get(j, this.getNumCols() - i - 1));
                }

                rotatedRows.add(newRow);
            }

            return new Paper(rotatedRows);
        }

        Paper foldedHorizontal(String instruction) {
            var crease = Integer.parseInt(instruction.split("=")[1]);

            List<List<Boolean>> topRows = new ArrayList<>();
            for (int i = 0; i < crease; i++) {
                topRows.add(rows.get(i));
            }

            List<List<Boolean>> bottomRows = new LinkedList<>();
            for (int i = this.getNumRows() - 1; i > crease; i--) {
                bottomRows.add(rows.get(i));
            }

            var sizeDifference = topRows.size() - bottomRows.size();

            if (sizeDifference > 0) {
                // pad top of bottom rows
                for (int i = 0; i < sizeDifference; i++) {
                    List<Boolean> newRow = new ArrayList<>();
                    while (newRow.size() < bottomRows.get(0).size()) newRow.add(false);
                    bottomRows.add(0, newRow);
                }
            } else if (sizeDifference < 0) {
                // pad bottom of top rows
                var sizeDifferenceAbs = Math.abs(sizeDifference);
                for (int i = 0; i < sizeDifferenceAbs; i++) {
                    List<Boolean> newRow = new ArrayList<>();
                    while (newRow.size() < topRows.get(0).size()) newRow.add(false);
                    topRows.add(newRow);
                }
            }

            assert(topRows.size() == bottomRows.size());

            var topIter = topRows.iterator();
            var bottomIter = bottomRows.iterator();

            var resultRows = new ArrayList<List<Boolean>>();

            while (topIter.hasNext())
            {
                var topRow = topIter.next();
                var bottomRow = bottomIter.next();

                var resultRow = new ArrayList<Boolean>();
                for (int i = 0; i < topRow.size(); i++) {
                    var top = topRow.get(i);
                    var bottom = bottomRow.get(i);
                    resultRow.add(top || bottom);
                }

                resultRows.add(resultRow);
            }

            return new Paper(resultRows);
        }

        Paper foldedVertical(String instruction) {
            return this.rotatedClockwise()
                    .foldedHorizontal(instruction)
                    .rotatedCounterClockwise();
        }

        @Override
        public String toString() {
            var sb = new StringBuilder();

            for (var row : rows) {
                for (var active : row) {
                    sb.append(active ? '#' : '.');
                }
                sb.append('\n');
            }

            return sb.toString();
        }
    }

    Paper current;
    List<String> instructions;

    void solvePart1() {
        var type = instructions.get(0).split("=")[0];

        if (type.equals("fold along y")) {
            current = current.foldedHorizontal(instructions.get(0));
        } else {
            current = current.foldedVertical(instructions.get(0));
        }

        var answer = current.rows.stream()
                .flatMapToInt(l -> l.stream().mapToInt(b -> b ? 1 : 0))
                .filter(i -> i == 1)
                .count();

        System.out.println("Part 1 answer: " + answer);
    }

    void solvePart2() {
        for (var i : this.instructions) {
            var type = i.split("=")[0];

            if (type.equals("fold along y")) {
                current = current.foldedHorizontal(i);
            } else {
                current = current.foldedVertical(i);
            }
        }

        System.out.println("Part 2 answer:");
        System.out.println(current);
    }

    static Day13 fromInput(String filename) throws IOException {
        var result = new Day13();

        var file = Files.readString(Path.of(filename)).split("\\n\\n");

        var pointsInput = file[0].split("\\n");

        var totalRows = Arrays.stream(pointsInput)
                .mapToInt(s -> Integer.parseInt(s.split(",")[1]))
                .max()
                .orElseThrow() + 1;

        var totalCols = Arrays.stream(pointsInput)
                .mapToInt(s -> Integer.parseInt(s.split(",")[0]))
                .max()
                .orElseThrow() + 1;

        List<List<Boolean>> points = new ArrayList<>(totalRows);

        for (int i = 0; i < totalRows; i++) {
            points.add(new ArrayList<>());
            for (int j = 0; j < totalCols; j++) {
                points.get(i).add(false);
            }
        }

        for (var p : pointsInput) {
            var point = p.split(",");
            int x = Integer.parseInt(point[0]);
            int y = Integer.parseInt(point[1]);

            points.get(y).set(x, true);
        }

        var instructionsInput = file[1];
        result.instructions = Arrays.stream(instructionsInput.split("\\n")).toList();

        result.current = new Paper(points);
        return result;
    }

    public static void main(String[] args) throws IOException {
        var part1 = Day13.fromInput("input/day13.txt");
        part1.solvePart1();

        var part2 = Day13.fromInput("input/day13.txt");
        part2.solvePart2();
    }
}
