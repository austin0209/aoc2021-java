package me.austin0209.aoc;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Day7 {
    static int fuelCost(List<Integer> positions, int align) {
        return positions.stream()
                .mapToInt((i) -> Math.abs(i - align))
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
        var positions = parseInput("input/day7.txt");
        int min = positions.stream().min(Integer::compare).orElseThrow();
        int max = positions.stream().max(Integer::compare).orElseThrow();

        int answer = IntStream.rangeClosed(min, max)
                .map((i) -> fuelCost(positions, i))
                .min().orElseThrow();

        System.out.println(answer);
    }
}
