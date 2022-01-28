package com.company;

import java.util.ArrayList;
import java.util.LinkedList;

public class SuffixTree {
    private ArrayList<String> wholeString;
    private int longestSS;
    private Node root;
    private int nodeNum;
    public SuffixTree(String s) {
        wholeString = new ArrayList<>();
        nodeNum = 0;
        longestSS = 0;
        wholeString.add(s);
        root = new Node("root");
        fillSuffixTree(s);
    }

    public void fillSuffixTree(String s) {
        for (int i = 1; i <= s.length(); i++) {
            String subS = s.substring(0, i);
            addSuffix(subS);
        }
    }

    public void addStringToTree(String s) {
        fillSuffixTree(s);
        wholeString.add(s);
    }

    public void addSuffix(String s) {
        Node node = root;
        for (Node n: node.children) {
            if (n.value.equals(s)) {
                n.increaseOccurrence();
                return;
            }
            if (n.value.startsWith(s) || s.startsWith(n.value)) {
                findDeepestNode(node, n, s);
                if (s.length() > 1) {
                    addSuffix(s.substring(1));
                }
                return;
            }
        }
        node.addChildren(new Node(s));
        if (s.length() > 1) {
            addSuffix(s.substring(1));
        }
    }

    public void branch(Node parent, Node node, String s) {
        Node splittedNode = node.splitNode(s);
        parent.removeChildren(node);
        parent.addChildren(splittedNode);
    }

    public void findDeepestNode(Node parent, Node child, String s) {
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
                findDeepestNode(child, n, remaining); //have to somehow kill this frame then open a new 0ne
                return;
            }
        }
        branch(parent, child, s);
    }

    public void printSuffixTree() {
        System.out.println("Total number of nodes is: " + nodeNum);
        for (Node n: root.getChildren()) {
            n.printNode("");
        }
    }

    private class Node{
        private String value;
        private LinkedList<Node> children;
        private int occurrence;

        private Node(String s) {
            value = s;
            children = new LinkedList<>();
            occurrence = 1;
        }

        private LinkedList<Node> getChildren() {
            return children;
        }

        private void addChildren(Node n) {
            this.children.add(n);
            nodeNum++;
        }

        private void removeChildren(Node n) {
            this.children.remove(n);
            nodeNum--;
        }

        private void setOccurrence(int occurrence) {
            this.occurrence = occurrence;
        }

        private void increaseOccurrence() {
            this.occurrence += 1;
        }

        private Node splitNode(String s) {
            int index = 0;
            while (index < s.length() &&  index < value.length()) {
                if (s.charAt(index) != this.value.charAt(index)) {
                    index += 1;
                    break;
                }
                index +=1;
            }
            if (index == value.length() && s.length() > value.length()) {
                value = s;
                return this;
            }
            String longest = value.substring(0, index);
            Node splitted = new Node(longest);
            splitted.setOccurrence(2);
            splitted.addChildren(new Node(value.substring(index)));
            splitted.addChildren(new Node(s.substring(index)));
            return splitted;
        }

        private void printNode(String soFar) {
            if (value.equals("")) {
                return;
            }
            System.out.println(soFar + value + "    " + occurrence);
            for (Node n: children) {
                n.printNode(soFar + value);
            }
        }
    }
}
