package me.austin0209.aoc;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;
import java.util.function.Predicate;

public class Day03 {
    public static void solvePart1(List<String> input) {
        int totalBits = input.get(0).length();
        int[] counts = new int[totalBits];

        for (int i = 0; i < totalBits; i++) {
            for (String bitString : input) {
                if (bitString.charAt(i) == '1') {
                    counts[i] += 1;
                } else {
                    counts[i] -= 1;
                }
            }
        }

        int gamma = 0;
        for (int i = 0; i < totalBits; i++) {
            if (counts[i] > 1) {
                gamma += 1 << (totalBits - i - 1);
            }
        }

        int epsilon = 0;
        for (int i = 0; i < totalBits; i++) {
            if (counts[i] < 1) {
                epsilon += 1 << (totalBits - i - 1);
            }
        }

        System.out.println(gamma * epsilon);
    }

    private static char mostCommonBit(List<String> input, int position) {
        return input.stream()
                .mapToInt(bitString -> bitString.charAt(position))
                .map(i -> i == '0' ? -1 : 1)
                .sum() >= 0 ? '1' : '0';
    }

    private record BitComparePair(char bit, char mostCommon) {}

    private static int getAnswer(List<String> input, Predicate<BitComparePair> predicate) {
        int currentIndex = 0;
        while (input.size() > 1) {
            final int index = currentIndex;
            final char mostCommon = mostCommonBit(input, index);
            input = input.stream()
                    .filter(bits -> predicate.test(new BitComparePair(bits.charAt(index), mostCommon)))
                    .toList();
            currentIndex++;
        }

        return Integer.parseInt(input.get(0), 2);
    }

    public static void solvePart2(List<String> input) {
        var oxygenNumber = getAnswer(input, p -> p.bit == p.mostCommon);
        var co2Number = getAnswer(input, p -> p.bit != p.mostCommon);

        System.out.println(oxygenNumber * co2Number);
    }

    public static void main(String[] args) throws FileNotFoundException {
        var reader = new BufferedReader(new FileReader("input/day3.txt"));

        var input = reader.lines()
                .map(String::toString)
                .toList();

        solvePart1(input);
        solvePart2(input);
    }
}
