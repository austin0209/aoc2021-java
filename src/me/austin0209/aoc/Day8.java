package me.austin0209.aoc;

import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Arrays;
import java.util.List;

public class Day8 {
    record PatternsOutputPair(List<String> patterns, List<String> output) {
    }

    static List<PatternsOutputPair> parseInput(String filename) throws FileNotFoundException {
        return new BufferedReader(new FileReader(filename))
                .lines()
                .map(s -> {
                    var split = s.split(" \\| ");
                    var patterns = Arrays.stream(split[0].split(" ")).toList();
                    var output = Arrays.stream(split[1].split(" ")).toList();
                    return new PatternsOutputPair(patterns, output);
                })
                .toList();
    }

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

    @Test
    void testParseInput() throws FileNotFoundException {
        var answer = parseInput("input/day8sample.txt").get(0);

        var patterns = List.of("acedgfb", "cdfbe", "gcdfa", "fbcad", "dab", "cefabd", "cdfgeb", "eafb", "cagedb", "ab");
        var output = List.of("cdfeb", "fcadb", "cdfeb", "cdbaf");

        System.out.println(answer.patterns);
        System.out.println(answer.output);

        assert(answer.patterns.equals(patterns) && answer.output.equals(output));
    }

}
