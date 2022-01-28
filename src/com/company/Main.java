package com.company;

import java.time.Duration;
import java.time.Instant;

public class Main {

    public static void main(String[] args) {
        // write your code here
        Instant timeBefore = Instant.now();
        SuffixTree test = new SuffixTree("tibet");
        test.addStringToTree("tilki");
        test.addStringToTree("best");
        test.printSuffixTree();
        Instant timeAfter = Instant.now();
        Duration duration = Duration.between(timeBefore, timeAfter);
        System.out.println(duration);
    }
}
