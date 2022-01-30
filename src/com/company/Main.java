package com.company;

import java.io.File;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static com.company.Utils.*;

public class Main {
    /** The directory of the solution. Please replace it with where you're program's absolute path is. */
    public static final File CWD = new File("/Users/tibetduman/Desktop/code_projects/SubstringProblem");
    /** The directory with the files to search. Similarly may want to adjust this path.*/
    public static final File FILES = join(CWD, "files");

    public static void main(String[] args) {
        Instant timeBefore = Instant.now();
        performTest();
        Instant timeAfter = Instant.now();
        Duration duration = Duration.between(timeBefore, timeAfter);
        System.out.println(duration);
    }

    /**
     * Simple test to check if the program does indeed return the longest substring amongst the files.
     */
    public static void performTest() {
        SuffixTree test = new SuffixTree();
        ArrayList<String> allData = readFromFiles();
        int fileNum = 0;
        for (String data: allData) {
            data = truncate(data);
            for (int i = 0; i < data.length(); i += 125) {
                test.addStringToTree(data.substring(i, Math.min(i + 125, data.length() - 1)), fileNum);
            }
            fileNum++;
        }
        test.printSuffixTree();
        test.updatePrintCandidates();
        System.out.println("The longest substring is: " + test.findLongestSS(allData));
        System.out.println("Number of nodes is: " + test.getNodeNum());
    }

    /**
     * Reads from the files indicated in CWD and FILES,
     * @return the contents as strings stored in an ArrayList.
     */
    public static ArrayList<String> readFromFiles() {
        List<String> fileNames = plainFilenamesIn(FILES);
        ArrayList<String> allStrings = new ArrayList<>();
        if (fileNames != null) {
            for (String fileName : fileNames) {
                allStrings.add(readContentsAsString(join(FILES, fileName)));
            }
        }
        return allStrings;
    }

    /**
     * @param s is the string to check for a new line character,
     * @return the string s without the new line character at the end, if there were one
     * in the first place.
     */
    public static String truncate(String s) {
        if (s.charAt(s.length()-1) == '\n') {
            s = s.substring(0, s.length() - 1);
        }
        return s;
    }

    /**
     * This function is the starting point to change the program to be
     * compatible with binary files as well.
     * It returns the data read from the files in the directory as bytes stored
     * in an ArrayList.
     */
    public static ArrayList<byte[]> readFromFilesAsBytes() {
        ArrayList<byte[]> allBytes = new ArrayList<>();
        List<String> fileNames = plainFilenamesIn(FILES);
        if (fileNames != null) {
            for (String fileName : fileNames) {
                allBytes.add(readContents(join(FILES, fileName)));
            }
        }
        for (byte[] b: allBytes) {
            System.out.println("There is a file with " + b.length + "bytes");
        }
        return allBytes;
    }
}
