package me.austin0209.aoc;

import java.io.*;
import java.util.stream.IntStream;

public class Day1 {
    public static void solvePart1(int[] input) {
        var ans = IntStream.range(1, input.length)
                .filter(i -> input[i] > input[i - 1])
                .count();

        System.out.println("Part 1 answer: " + ans);
    }

    public static void solvePart2(int[] input) {
        var windows = IntStream.range(0, input.length - 2)
                .map(i -> input[i] + input[i + 1] + input[i + 2])
                .toArray();

        var ans = IntStream.range(1, windows.length)
                .filter(i -> windows[i] > windows[i - 1])
                .count();

        System.out.println("Part 2 answer: " + ans);
    }

    public static void main(String[] args) throws FileNotFoundException {
        var reader = new BufferedReader(new FileReader("input/day1.txt"));

        var input = reader
                .lines()
                .mapToInt(Integer::parseInt)
                .toArray();

        solvePart1(input);
        solvePart2(input);
    }
}
