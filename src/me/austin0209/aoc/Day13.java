package me.austin0209.aoc;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
        System.out.println(result.current);
        return result;
    }

    public static void main(String[] args) throws IOException {
        var test = Day13.fromInput("input/day13sample.txt");
    }
}
