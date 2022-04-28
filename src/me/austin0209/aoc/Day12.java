package me.austin0209.aoc;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;

public class Day12 {
    static class Node {
        final String id;
        final boolean isBig;

        List<Node> neighbors;

        Node(String id) {
            this.id = id;
            this.isBig = id.toUpperCase().equals(id);
            neighbors = new ArrayList<>();
        }

        @Override
        public String toString() {
            return this.id;
        }
    }

    Map<String, Node> nodes;
    List<List<Node>> paths;
    boolean usedDoubleVisit;

    Day12() {
        this.nodes = new HashMap<>();
        this.paths = new ArrayList<>();
        this.usedDoubleVisit = false;
    }

    void solvePart1Help(Node current, List<Node> path, Set<Node> visited) {
        if (current.id.equals("end")) {
            path.add(nodes.get("end"));
            paths.add(path);
            return;
        }

        if (current.neighbors.isEmpty()) {
            return;
        }

        if (visited.contains(current)) {
            return;
        }

        if (!current.isBig) {
            visited.add(current);
        }

        for (var neighbor : current.neighbors) {
            var newPath = new ArrayList<>(path);
            var newVisited = new HashSet<>(visited);

            newPath.add(current);
            solvePart1Help(neighbor, newPath, newVisited);
        }
    }

    boolean containsPath(List<Node> path) {
        for (var p : paths) {
            if (p.equals(path)) {
                return true;
            }
        }

        return false;
    }

    void solvePart1() {
        var start = nodes.get("start");
        solvePart1Help(start, new ArrayList<>(), new HashSet<>());

        System.out.println("Part 1 answer: " + this.paths.size());
    }

    void solvePart2Help(Node current, List<Node> path, Set<Node> visited) {
        if (current.id.equals("end")) {
            path.add(nodes.get("end"));

            if (!this.containsPath(path)) {
                paths.add(path);
            }

            return;
        }

        if (current.neighbors.isEmpty()) {
            return;
        }

        List<Node> pathNodesList = path.stream().filter(n -> !n.isBig).toList();
        Set<Node> pathNodes = new HashSet<>(pathNodesList);
        var hasDoubleVisited = pathNodesList.size() != pathNodes.size();

        if ((hasDoubleVisited || current.id.equals("start")) && visited.contains(current)) {
            return;
        }

        if (!current.isBig) {
            visited.add(current);
        }

        for (var neighbor : current.neighbors) {
            var newPath = new ArrayList<>(path);
            var newVisited = new HashSet<>(visited);

            newPath.add(current);
            solvePart2Help(neighbor, newPath, newVisited);
        }
    }

    void solvePart2() {
        var start = nodes.get("start");
        solvePart2Help(start, new ArrayList<>(), new HashSet<>());

        System.out.println("Part 2 answer: " + this.paths.size());
    }

    static Day12 fromInput(String filename) throws FileNotFoundException {
        var result = new Day12();
        var nodes = result.nodes;

        var reader = new BufferedReader(new FileReader(filename));
        var lines = reader.lines().toList();

        for (var line : lines) {
            var split = line.split("-");
            var fromId = split[0];
            var toId = split[1];

            Node from;
            Node to;

            if (nodes.containsKey(fromId)) {
                from = nodes.get(fromId);
            } else {
                from = new Node(fromId);
                nodes.put(fromId, from);
            }

            if (nodes.containsKey(toId)) {
                to = nodes.get(toId);
            } else {
                to = new Node(toId);
                nodes.put(toId, to);
            }

            from.neighbors.add(to);
            to.neighbors.add(from);
        }

        return result;
    }

    public static void main(String[] args) throws FileNotFoundException {
        var part1 = Day12.fromInput("input/day12.txt");
        part1.solvePart1();

        var part2 = Day12.fromInput("input/day12.txt");
        part2.solvePart2();
    }
}
