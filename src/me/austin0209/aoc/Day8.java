package me.austin0209.aoc;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

public class Day8 {

    public static void main(String[] args) throws FileNotFoundException {
        // Solve part 1
        var outputs = new BufferedReader(new FileReader("input/day8.txt"))
                .lines()
                .map(s -> s.split(" \\| ")[1].split(" "))
                .flatMap(Arrays::stream)
                .toList();

        var answerPart1 = outputs.stream()
                .filter(s -> s.length() == 2 || s.length() == 3 || s.length() == 4 || s.length() == 7)
                .count();

        System.out.println(answerPart1);
    }
}
