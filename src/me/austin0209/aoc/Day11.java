package me.austin0209.aoc;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class Day11 {

    ArrayList<Integer> data;
    final int rows;
    final int cols;

    public Day11(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
    }

    static Day11 fromInput(String filename) throws FileNotFoundException {
        var reader = new BufferedReader(new FileReader(filename));

        var lines = reader.lines().toList();

        Day11 result = new Day11(lines.size(), lines.get(0).length());

        result.data = lines.stream()
                .flatMapToInt(l -> l.chars().map(i -> i - '0'))
                .boxed()
                .collect(Collectors.toCollection(ArrayList::new));

        return result;
    }

    void increment(int x, int y) {
        if (x < 0 || x >= cols || y < 0 || y >= rows) return;

        var index = x + y * cols;
        if (data.get(index) == 0) return;

        this.data.set(index, data.get(index) + 1);
    }

    // returns total flashes done in current state and performs all flashes
    int doFlashes() {
        var flashesDone = 0;

        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < cols; x++) {
                var index = x + y * cols;
                if (data.get(index) > 9) {
                    this.increment(x - 1, y - 1);
                    this.increment(x, y - 1);
                    this.increment(x + 1, y - 1);
                    this.increment(x + 1, y);
                    this.increment(x + 1, y + 1);
                    this.increment(x, y + 1);
                    this.increment(x - 1, y + 1);
                    this.increment(x - 1, y);

                    data.set(index, 0);
                    flashesDone++;
                }
            }
        }

        return flashesDone;
    }

    void solvePart1(int numSteps) {
        int totalFlashes = 0;

        for (int i = 0; i < numSteps; i++) {
            int flashes;
            this.data = this.data.stream().map(e -> e + 1).collect(Collectors.toCollection(ArrayList::new));
            do {
                flashes = this.doFlashes();
                totalFlashes += flashes;
            } while (flashes != 0);
        }

        System.out.println(totalFlashes);
    }


    void solvePart2() {
        int stepCount = 0;

        while (this.data.stream().anyMatch(i -> i != 0))
        {
            int flashes;
            this.data = this.data.stream().map(e -> e + 1).collect(Collectors.toCollection(ArrayList::new));
            do {
                flashes = this.doFlashes();
            } while (flashes != 0);

            stepCount++;
        }

        System.out.println(stepCount);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < cols; x++) {
                sb.append(data.get(x + y * cols));
            }
            sb.append('\n');
        }

        return sb.toString();
    }

    public static void main(String[] args) throws FileNotFoundException {
        Day11 part1 = Day11.fromInput("input/day11.txt");
        part1.solvePart1(100);

        Day11 part2 = Day11.fromInput("input/day11.txt");
        part2.solvePart2();
    }
}
