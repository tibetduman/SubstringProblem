package com.company;

import java.util.*;

public class SuffixTree {
    private ArrayList<String> wholeString;
    private int longestSS;
    private Node root;
    private int nodeNum;
    private HashMap<String, Integer> candidates;
    public SuffixTree() {
        wholeString = new ArrayList<>();
        nodeNum = 0;
        longestSS = 0;
        root = new Node(" ", -1);
        candidates = new HashMap<>();
    }

    public void fillSuffixTree(String s, int fNum) {
        for (int i = 1; i <= s.length(); i++) {
            String subS = s.substring(0, i);
            addSuffix(subS, fNum);
        }
    }

    public void addStringToTree(String s, int fileNum) {
        fillSuffixTree(s, fileNum);
        wholeString.add(s);
    }

    public void addSuffix(String s, int fNum) {
        for (Node n: root.children) {
            if (n.value.equals(s)) {
                n.fNums.add(fNum);
                return;
            }
            if (n.value.startsWith(s) || s.startsWith(n.value)) {
                findDeepestNode(root, n, s, fNum);
                if (s.length() > 1) {
                    addSuffix(s.substring(1), fNum);
                }
                return;
            }
        }
        root.addChildren(new Node(s, fNum));
        if (s.length() > 1) {
            addSuffix(s.substring(1), fNum);
        }
    }

    public void branch(Node parent, Node node, String s, int fNum) {
        Node splittedNode = node.splitNode(s, fNum);
        parent.removeChildren(node);
        parent.addChildren(splittedNode);
    }

    public void findDeepestNode(Node parent, Node child, String s, int fNum) {
        int index = 0;
        while (index < s.length() &&  index < child.value.length()) {
            if (s.charAt(index) != child.value.charAt(index)) {
                index += 1;
                break;
            }
            index +=1;
        }
        String remaining = s.substring(index);
        for (Node n: child.children) {
            if (n.value.startsWith(remaining) || remaining.startsWith(n.value)) {
                findDeepestNode(child, n, remaining, fNum); //have to somehow kill this frame then open a new 0ne
                return;
            }
        }
        branch(parent, child, s, fNum);
    }

    public void printSuffixTree() {
        System.out.println("Total number of nodes is: " + nodeNum);
        root.printNode("");
    }

    public int getNodeNum() {
        return nodeNum;
    }

    public void updatePrintCandidates() {
        HashMap<String, Integer> placeHolder = new HashMap<>(candidates);
        for (Map.Entry<String, Integer> entry: placeHolder.entrySet()) {
            if (entry.getValue() < longestSS / 2) {
                candidates.remove(entry.getKey());
            }
        }
        System.out.println("The remaining candidates are:");
        for (String s : candidates.keySet()) {
            System.out.println(s);
        }
    }

    private class Node{
        private String value;
        private LinkedList<Node> children;
        private HashSet<Integer> fNums;

        private Node(String s, int f) {
            value = s;
            children = new LinkedList<>();
            fNums = new HashSet<>();
            fNums.add(f);
        }

        private void addChildren(Node n) {
            this.children.add(n);
            nodeNum++;
        }

        private void removeChildren(Node n) {
            this.children.remove(n);
            nodeNum--;
        }

        private int getOccurrence() {
            return this.fNums.size();
        }

        private Node splitNode(String s, int fNum) {
            int index = 0;
            while (index < s.length() &&  index < value.length()) {
                if (s.charAt(index) != this.value.charAt(index)) {
                    index += 1;
                    break;
                }
                index +=1;
            }
            if (index == value.length() && s.length() > value.length()) {
                if (this.getOccurrence() == 1 && fNums.contains(fNum)) {
                    value = s;
                } else {
                    fNums.add(fNum);
                    this.addChildren(new Node(s.substring(index), fNum));
                }
                return this;
            }
            return split(this, value.substring(0, index),
                    value.substring(index), s.substring(index), fNum);
        }

        private Node split(Node n, String common, String previous, String remaining, int fileNum) {
            Node splitted = new Node(common, -1);
            Node prev = new Node(previous, -1);
            splitted.fNums = new HashSet<>(n.fNums);
            splitted.fNums.add(fileNum);
            prev.fNums = new HashSet<>(n.fNums);
            splitted.addChildren(prev);
            splitted.addChildren(new Node(remaining, fileNum));
            return splitted;
        }

        private void printNode(String soFar) {
            if (value.equals("")) {
                return;
            }
            String whole = soFar + value;
            if (this.getOccurrence() > 1 && whole.length() > longestSS / 2) {
                candidates.put(whole, whole.length());
                longestSS = Math.max(whole.length(), longestSS);
                System.out.println(soFar + value + "    " + this.getOccurrence() + " ");
            }
            for (Node n: children) {
                n.printNode(whole);
            }
        }
    }
}
