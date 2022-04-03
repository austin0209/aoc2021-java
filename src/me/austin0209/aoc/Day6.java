package me.austin0209.aoc;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.stream.Stream;

public class Day6 {
    static long[] parseInput(String filename) throws IOException {
        var reader = new BufferedReader(new FileReader(filename));
        String[] line = reader.readLine().split(",");

        var input = Stream.of(line)
                .map(Integer::parseInt)
                .toList();

        long[] population = new long[9];
        for (var i : input) {
            population[i] += 1;
        }

        return population;
    }

    static void simulate(long[] population) {
        long temp = population[0];
        for (int i = 0; i < population.length - 1; i++) {
            population[i] = population[i + 1];
        }

        population[8] = temp;
        population[6] += temp;
    }

    public static void main(String[] args) throws IOException {
        long[] population = parseInput("input/day6.txt");

        for (int i = 0; i < 256; i++) {
            simulate(population);
        }

        long fishTotal = Arrays.stream(population).sum();
        System.out.println("Answer: " + fishTotal);
    }

}
