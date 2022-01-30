package com.company;

import java.util.*;

public class SuffixTree {
    private ArrayList<String> wholeString;
    private int longestSS;
    private Node root;
    private int nodeNum;
    private HashMap<String, HashSet<Integer>> candidates;
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
        HashMap<String, HashSet<Integer>> placeHolder = new HashMap<>(candidates);
        for (String s: placeHolder.keySet()) {
            if (s.length() < longestSS / 2) {
                candidates.remove(s);
            }
        }
        System.out.println("The remaining candidates are:");
        for (String s : candidates.keySet()) {
            System.out.println(s);
        }
    }

    public String findLongestSS(ArrayList<String> data) {
        String longestSubString = "";
        for (String s: candidates.keySet()) {
            ArrayList<String> dataOfInterest = new ArrayList<>();
            for (Integer i: candidates.get(s)) {
                dataOfInterest.add(data.get(i));
            }
            String sExtended = furtherCheck(s, dataOfInterest);
            if (sExtended.length() > longestSubString.length()) {
                longestSubString = sExtended;
            }
        }
        return longestSubString;
    }

    public String furtherCheck(String s, ArrayList<String> dataOfInterest) {
        String longestExtension = s;
        for (int i = 0; i < dataOfInterest.size() - 1; i ++) {
            String first = dataOfInterest.get(i);
            for (int j = i + 1; j < dataOfInterest.size(); j++) {
                String second = dataOfInterest.get(j);
                int sf1 = first.indexOf(s);
                int ef1 = sf1 + s.length();
                int sf2 = second.indexOf(s);
                int ef2 = ef1 + s.length();
                while (sf1 > 0 && sf2 > 0 && first.charAt(sf1) == second.charAt(sf2)) {
                    sf1--;
                    sf2--;
                }
                while (ef1 < first.length() && ef2 < second.length() && first.charAt(ef1) == second.charAt(ef2)) {
                    ef1++;
                    ef2++;
                }
                if (ef1 - sf1 > longestExtension.length()) {
                    longestExtension = dataOfInterest.get(i).substring(sf1 + 1, ef1 + 1);
                }
            }
        }
        return longestExtension;
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
                candidates.put(whole.substring(1), this.fNums);
                longestSS = Math.max(whole.length(), longestSS);
            }
            for (Node n: children) {
                n.printNode(whole);
            }
        }
    }
}
