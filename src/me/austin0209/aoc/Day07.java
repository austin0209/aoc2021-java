package me.austin0209.aoc;

import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Day07 {
    static int fuelCostPart1(List<Integer> positions, int align) {
        return positions.stream()
                .mapToInt((i) -> Math.abs(i - align))
                .sum();
    }

    static int fuelCostPart2(List<Integer> positions, int align) {
        return positions.stream()
                .mapToInt((i) -> IntStream.rangeClosed(1, Math.abs(i - align)).sum())
                .sum();
    }

    static List<Integer> parseInput(String filename) throws IOException {
        var reader = new BufferedReader(new FileReader(filename));
        String[] line = reader.readLine().split(",");
        return Stream.of(line)
                .map(Integer::parseInt)
                .toList();
    }

    public static void main(String[] args) throws IOException {
        // Solve part 1
        var positions = parseInput("input/day7.txt");
        int min = positions.stream().min(Integer::compare).orElseThrow();
        int max = positions.stream().max(Integer::compare).orElseThrow();

        int answer = IntStream.rangeClosed(min, max)
                .map((i) -> fuelCostPart1(positions, i))
                .min().orElseThrow();

        System.out.println("Part 1 answer: " + answer);

        // Solve part 2
        int answer2 = IntStream.rangeClosed(min, max)
                .map((i) -> fuelCostPart2(positions, i))
                .min().orElseThrow();

        System.out.println("Part 2 answer: " + answer2);
    }

    @Test
    void testPart2Cost() throws IOException {
        var positions = parseInput("input/day7sample.txt");
        int answer = fuelCostPart2(positions, 2);

        System.out.println("The answer is: " + answer);
        assert(answer == 206);
    }
}
