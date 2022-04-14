package me.austin0209.aoc;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;

public class Day02 {
    private enum Command {
        FORWARD,
        DOWN,
        UP,
    }

    private record Instruction(Command command, int units) {}

    private static Instruction parseCommand(String input) {
        String[] pair = input.split(" ");

        var command = switch (pair[0]) {
            case "forward" -> Command.FORWARD;
            case "down" -> Command.DOWN;
            case "up" -> Command.UP;
            default -> throw new IllegalArgumentException("Invalid command.");
        };

        var units = Integer.parseInt(pair[1]);

        return new Instruction(command, units);
    }

    public static void solvePart1(List<Instruction> input) {
        int depth = 0;
        int horizontal = 0;

        for (Instruction i : input) {
            switch (i.command) {
                case FORWARD -> horizontal += i.units;
                case DOWN -> depth += i.units;
                case UP -> depth -= i.units;
                default -> throw new IllegalArgumentException("Invalid command.");
            }
        }

        System.out.println(depth * horizontal);
    }

    public static void solvePart2(List<Instruction> input) {
        int depth = 0;
        int horizontal = 0;
        int aim = 0;

        for (Instruction i : input) {
            switch (i.command) {
                case FORWARD -> {
                    horizontal += i.units;
                    depth += aim * i.units;
                }
                case DOWN -> aim += i.units;
                case UP -> aim -= i.units;
                default -> throw new IllegalArgumentException("Invalid command.");
            }
        }

        System.out.println(depth * horizontal);
    }

    public static void main(String[] args) throws FileNotFoundException {
        var reader = new BufferedReader(new FileReader("input/day2.txt"));

        var input = reader
                .lines()
                .map(Day02::parseCommand)
                .toList();

        solvePart2(input);
    }
}
