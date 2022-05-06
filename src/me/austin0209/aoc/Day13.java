package me.austin0209.aoc;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class Day13 {
    record Paper(List<List<Boolean>> rows) {
        boolean get(int x, int y) {
            return rows.get(y).get(x);
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

    static Paper foldedHorizontal(Paper paper, String instruction) {
        var crease = Integer.parseInt(instruction.split("=")[1]);

        List<List<Boolean>> topRows = new ArrayList<>();
        for (int i = 0; i < crease; i++) {
            topRows.add(paper.rows.get(i));
        }

        List<List<Boolean>> bottomRows = new LinkedList<>();
        for (int i = paper.rows.size() - 1; i > crease; i--) {
            bottomRows.add(paper.rows.get(i));
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
        var test = Day13.fromInput("input/day13sample.txt");
        var temp = foldedHorizontal(test.current, test.instructions.get(0));
        System.out.println(temp);
    }
}
