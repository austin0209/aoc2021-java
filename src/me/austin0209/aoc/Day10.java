package me.austin0209.aoc;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;

public class Day10 {
    record Token(Character type, int nesting) {}

    Deque<Token> parensStack;
    Deque<Token> squareStack;
    Deque<Token> curlyStack;
    Deque<Token> pointyStack;

    List<String> lines;

    Day10() {
        parensStack = new ArrayDeque<>();
        squareStack = new ArrayDeque<>();
        curlyStack = new ArrayDeque<>();
        pointyStack = new ArrayDeque<>();
    }

    private void push(Token t) {
        switch (t.type) {
            case '(' -> parensStack.push(t);
            case '[' -> squareStack.push(t);
            case '{' -> curlyStack.push(t);
            case '<' -> pointyStack.push(t);
            default -> throw new RuntimeException("Invalid character! " + t.type);
        }
    }

    private Token pop(char c) throws NoSuchElementException {
        return switch (c) {
            case ')' -> parensStack.pop();
            case ']' -> squareStack.pop();
            case '}' -> curlyStack.pop();
            case '>' -> pointyStack.pop();
            default -> throw new RuntimeException("Invalid character! " + c);
        };
    }

    private int getScore(char c) {
        return switch (c) {
            case ')' -> 3;
            case ']' -> 57;
            case '}' -> 1197;
            case '>' -> 25137;
            default -> throw new RuntimeException("Invalid character! " + c);
        };
    }

    private void reset() {
        parensStack.clear();
        squareStack.clear();
        curlyStack.clear();
        pointyStack.clear();
    }

    void solvePart1() {
        int score = 0;
        lineIteration: for (var line : lines) {
            this.reset();
            var chars = line.chars().mapToObj(i -> (char)i).toList();
            int currentNesting = 0;
            for (var c : chars) {
                switch (c) {
                    case '(', '[', '{', '<' -> this.push(new Token(c, currentNesting++));
                    case ')', ']', '}', '>' -> {
                        try {
                            Token popped = this.pop(c);
                            if (popped.nesting != --currentNesting) {
                                throw new NoSuchElementException();
                            }
                        } catch (NoSuchElementException e) {
                            score += this.getScore(c);
                            continue lineIteration;
                        }
                    }
                    default -> throw new RuntimeException("Invalid character! " + c);
                }
            }
        }

        System.out.println("Part 1 answer: " + score);
    }

    static Day10 fromInput(String filename) throws FileNotFoundException {
        var result = new Day10();

        var reader = new BufferedReader(new FileReader(filename));
        result.lines = reader.lines().toList();

        return result;
    }

    public static void main(String[] args) throws FileNotFoundException {
        var part1 = Day10.fromInput("input/day10.txt");
        part1.solvePart1();
    }
}
