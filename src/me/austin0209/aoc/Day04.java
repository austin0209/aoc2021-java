package me.austin0209.aoc;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Stream;

public class Day04 {
    record BoardSpace(int value, boolean marked) {
    }

    record Board(List<BoardSpace> boardSpaces, int columns, int rows) {
        BoardSpace get(int x, int y) {
            return boardSpaces.get(x + y * columns);
        }

        Stream<BoardSpace> stream() {
            return boardSpaces.stream();
        }
    }

    record Win(int finalScore, int drawIndex) {
    }

    static class GameSession {
        int lastDrawnIndex;
        List<Board> currentBoards;

        GameSession(List<Board> players) {
            this.currentBoards = players;
            lastDrawnIndex = -1;
        }
    }

    record Bingo(List<Board> initialBoards, List<Integer> draws) {
        // returns false if game is finished, else updates state
        boolean drawNumberStep(GameSession session) {
            if (session.lastDrawnIndex == draws.size() - 1) return false;

            var number = draws.get(session.lastDrawnIndex + 1);
            var newBoards = new ArrayList<Board>();
            for (Board board : session.currentBoards) {
                List<BoardSpace> spaces = board.stream()
                        .map(space -> {
                            if (space.value == number) return new BoardSpace(space.value, true);
                            else return space;
                        }).toList();
                newBoards.add(new Board(spaces, board.columns, board.rows));
            }

            session.currentBoards = newBoards;
            session.lastDrawnIndex++;
            return true;
        }

        boolean hasWon(Board board) {
            List<List<BoardSpace>> rows = new ArrayList<>();
            while (rows.size() < board.rows) rows.add(new ArrayList<>());
            List<List<BoardSpace>> columns = new ArrayList<>();
            while (columns.size() < board.columns) columns.add(new ArrayList<>());

            for (int y = 0; y < board.rows; y++) {
                for (int x = 0; x < board.columns; x++) {
                    rows.get(y).add(board.get(x, y));
                    columns.get(x).add(board.get(x, y));
                }
            }

            Function<List<List<BoardSpace>>, Boolean> checkLineWin = (lines) ->
                    lines.stream()
                            .map(r -> r.stream()
                                    .mapToInt(bs -> bs.marked ? 1 : 0)
                                    .sum())
                            .anyMatch(total -> total == lines.get(0).size());

            return checkLineWin.apply(rows) || checkLineWin.apply(columns);
        }

        Win getWin(Board board, GameSession session) {
            if (hasWon(board)) {
                int sumOfUnmarked = board.stream()
                        .filter(bs -> !bs.marked)
                        .mapToInt(bs -> bs.value)
                        .sum();
                return new Win(draws.get(session.lastDrawnIndex) * sumOfUnmarked, session.lastDrawnIndex);
            }

            return null;
        }

        Win playSingleUntilWin(Board board) {
            var session = new GameSession(List.of(board));
            while (this.drawNumberStep(session)) {
                var win = this.getWin(session.currentBoards.get(0), session);
                if (win != null) return win;
            }

            return null;
        }

        int playPart1() {
            var session = new GameSession(new ArrayList<>(initialBoards));
            while (this.drawNumberStep(session)) {
                for (Board board : session.currentBoards) {
                    var win = this.getWin(board, session);
                    if (win != null) {
                        return win.finalScore;
                    }
                }
            }

            throw new RuntimeException("Nobody won...");
        }

        int playPart2() {
            var lastWin = initialBoards.stream()
                    .map(this::playSingleUntilWin)
                    .filter(Objects::nonNull)
                    .max(Comparator.comparingInt(w -> w.drawIndex))
                    .orElseThrow(() -> new RuntimeException("Nobody won..."));

            return lastWin.finalScore;
        }
    }

    static Bingo parseInput(String filename) throws IOException {
        String[] input = Files.readString(Path.of(filename)).split("\n\n");

        List<Integer> draws = Stream.of(input[0].split(","))
                .map(Integer::parseInt)
                .toList();

        List<Board> players = new ArrayList<>();
        for (int i = 1; i < input.length; i++) {
            var boardStr = input[i];
            var rows = Stream.of(boardStr.split("\n"))
                    .map(rowStr -> Stream.of(rowStr.split("\\s+"))
                            .filter(s -> s.length() > 0)
                            .map(Integer::parseInt)
                            .toList())
                    .toList();

            var boardSpaces = new ArrayList<BoardSpace>();
            for (var row : rows) {
                for (var value : row) {
                    boardSpaces.add(new BoardSpace(value, false));
                }
            }

            players.add(new Board(boardSpaces, rows.get(0).size(), rows.size()));
        }

        return new Bingo(players, draws);
    }

    public static void main(String[] args) throws IOException {
        Bingo bingo = parseInput("input/day4.txt");
        System.out.println(bingo.playPart2());
    }

    @Test
    void testInputParsing() throws IOException {
        Bingo bingo = parseInput("input/day4sample.txt");

        List<Integer> draws = List.of(7, 4, 9, 5, 11, 17, 23, 2, 0, 14, 21, 24, 10,
                16, 13, 6, 15, 25, 12, 22, 18, 20, 8, 19, 3, 26, 1);

        List<BoardSpace> p1Board = Stream.of(
                22, 13, 17, 11, 0,
                8, 2, 23, 4, 24,
                21, 9, 14, 16, 7,
                6, 10, 3, 18, 5,
                1, 12, 20, 15, 19
        ).map(i -> new BoardSpace(i, false)).toList();

        List<BoardSpace> p2Board = Stream.of(
                3, 15, 0, 2, 22,
                9, 18, 13, 17, 5,
                19, 8, 7, 25, 23,
                20, 11, 10, 24, 4,
                14, 21, 16, 12, 6
        ).map(i -> new BoardSpace(i, false)).toList();

        List<BoardSpace> p3Board = Stream.of(
                14, 21, 17, 24, 4,
                10, 16, 15, 9, 19,
                18, 8, 23, 26, 20,
                22, 11, 13, 6, 5,
                2, 0, 12, 3, 7
        ).map(i -> new BoardSpace(i, false)).toList();

        List<Board> players = Stream.of(p1Board, p2Board, p3Board)
                .map(b -> new Board(b, 5, 5))
                .toList();

        assert (players.size() == bingo.initialBoards.size());
        assert (draws.size() == bingo.draws.size());

        for (int i = 0; i < players.size(); i++) {
            assert (players.get(i).equals(bingo.initialBoards.get(i)));
        }

        for (int i = 0; i < draws.size(); i++) {
            assert (draws.get(i).equals(bingo.draws.get(i)));
        }
    }

    @Test
    void testWinDetectionHorizontal() {
        List<BoardSpace> spaces = Stream.of(
                22, 13, 17, 11, 0,
                8, 2, 23, 4, 24,
                21, 9, 14, 16, 7,
                6, 10, 3, 18, 5,
                1, 12, 20, 15, 19
        ).map(i -> {
            if (List.of(21, 9, 14, 16, 7).contains(i))
                return new BoardSpace(i, true);
            return new BoardSpace(i, false);
        }).toList();

        Board board = new Board(spaces, 5, 5);

        Bingo bingo = new Bingo(List.of(board), new ArrayList<>());
        boolean winnerExists = bingo.hasWon(board);

        assert (winnerExists);
    }

    @Test
    void testWinDetectionVertical() {
        List<BoardSpace> spaces = Stream.of(
                22, 13, 17, 11, 0,
                8, 2, 23, 4, 24,
                21, 9, 14, 16, 7,
                6, 10, 3, 18, 5,
                1, 12, 20, 15, 19
        ).map(i -> {
            if (List.of(17, 23, 14, 3, 20).contains(i))
                return new BoardSpace(i, true);
            return new BoardSpace(i, false);
        }).toList();

        Board board = new Board(spaces, 5, 5);

        Bingo bingo = new Bingo(List.of(board), new ArrayList<>());
        boolean winnerExists = bingo.hasWon(board);

        assert (winnerExists);
    }

    @Test
    void testWinDetectionFalse() {
        List<BoardSpace> spaces = Stream.of(
                22, 13, 17, 11, 0,
                8, 2, 23, 4, 24,
                21, 9, 14, 16, 7,
                6, 10, 3, 18, 5,
                1, 12, 20, 15, 19
        ).map(i -> {
            if (i % 2 == 0)
                return new BoardSpace(i, true);
            return new BoardSpace(i, false);
        }).toList();

        Board board = new Board(spaces, 5, 5);

        Bingo bingo = new Bingo(List.of(board), new ArrayList<>());
        boolean winnerExists = bingo.hasWon(board);

        assert (!winnerExists);
    }

    @Test
    void testPart1() throws IOException {
        Bingo bingo = parseInput("input/day4sample.txt");
        var score = bingo.playPart1();
        assert (score == 4512);
    }

    @Test
    void testPart2() throws IOException {
        Bingo bingo = parseInput("input/day4sample.txt");
        var score = bingo.playPart2();
        assert (score == 1924);
    }
}
