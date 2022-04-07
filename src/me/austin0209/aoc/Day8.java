package me.austin0209.aoc;

import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;

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

    static long patternIntersectionCount(String pattern1, String pattern2) {
        Set<Integer> charSetPattern1 = new HashSet<>(pattern1.chars().boxed().toList());
        Set<Integer> charSetPattern2 = new HashSet<>(pattern2.chars().boxed().toList());

        charSetPattern1.retainAll(charSetPattern2);

        return charSetPattern1.size();
    }

    // Returns true if pattern 1 contains pattern 2.
    static boolean containsPattern(String pattern1, String pattern2) {
        Set<Integer> charSetPattern1 = new HashSet<>(pattern1.chars().boxed().toList());
        return charSetPattern1.containsAll(pattern2.chars().boxed().toList());
    }

    static List<String> getSortedPatterns(List<String> patterns) {
        return patterns.stream()
                .map(s -> s.chars().sorted().mapToObj(i -> String.valueOf((char) i)))
                .map(ss -> ss.reduce((a, b) -> a + b).orElseThrow())
                .toList();
    }

    static int decodeLine(PatternsOutputPair patternsOutputPair) {
        HashMap<Integer, String> intToPattern = new HashMap<>();
        HashMap<String, Integer> patternToInt = new HashMap<>();

        var patterns = getSortedPatterns(patternsOutputPair.patterns);
        var outputs = getSortedPatterns(patternsOutputPair.output);

        // Translate 1 4 7 and 8
        for (String pattern : patterns) {
            var patternValue = switch (pattern) {
                case (String s && s.length() == 2) -> 1;
                case (String s && s.length() == 4) -> 4;
                case (String s && s.length() == 3) -> 7;
                case (String s && s.length() == 7) -> 8;
                default -> -1;
            };

            if (patternValue != -1) {
                intToPattern.put(patternValue, pattern);
                patternToInt.put(pattern, patternValue);
            }
        }

        for (String pattern : patterns) {
            var patternValue = switch (pattern) {
                case (String s && s.length() == 6 &&
                        containsPattern(s, intToPattern.get(1)) &&
                        containsPattern(s, intToPattern.get(4))) -> 9;
                case (String s && s.length() == 6 && containsPattern(s, intToPattern.get(1))) -> 0;
                case (String s && s.length() == 6) -> 6;
                case (String s && s.length() == 5 && containsPattern(s, intToPattern.get(1))) -> 3;
                case (String s && s.length() == 5 &&
                        containsPattern(s, intToPattern.get(1)) &&
                        patternIntersectionCount(s, intToPattern.get(4)) == 2) -> 2;
                case (String s && s.length() == 5) -> 5;
                default -> -1;
            };

            if (patternValue != -1) patternToInt.put(pattern, patternValue);
        }

        int answer = 0;
        int placeValue = 1000;
        for (var pattern : outputs) {
            answer += patternToInt.get(pattern) * placeValue;
            placeValue /= 10;
        }

        return answer;
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

        // Solve part 2
        var input = parseInput("input/day8.txt");

        var answerPart2 = 0;
        for (var pair : input) {
            answerPart2 += decodeLine(pair);
        }

        System.out.println(answerPart2);
    }

    @Test
    void testParseInput() throws FileNotFoundException {
        var answer = parseInput("input/day8sample.txt").get(0);

        var patterns = List.of("acedgfb", "cdfbe", "gcdfa", "fbcad", "dab", "cefabd", "cdfgeb", "eafb", "cagedb", "ab");
        var output = List.of("cdfeb", "fcadb", "cdfeb", "cdbaf");

        System.out.println(answer.patterns);
        System.out.println(answer.output);

        assert (answer.patterns.equals(patterns) && answer.output.equals(output));
    }

    @Test
    void testPatternContains() {
        var str1 = "abcde";
        var str2 = "cde";
        assert (containsPattern(str1, str2));

        str2 = "ed";
        assert (containsPattern(str1, str2));
    }

    @Test
    void testPart2() throws FileNotFoundException {
        var input = parseInput("input/day8sample.txt").get(0);
        var answer = decodeLine(input);

        System.out.println(answer);
        assert (answer == 5353);
    }
}
