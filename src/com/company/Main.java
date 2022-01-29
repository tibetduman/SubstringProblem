package com.company;

import java.io.File;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static com.company.Utils.*;

public class Main {
    /** The current working directory. */
    public static final File CWD = new File("/Users/tibetduman/Desktop/code_projects/SubstringProblem");
    /** The directory with the files to search. */
    public static final File FILES = join(CWD, "files");

    public static void main(String[] args) {
        // write your code here
        Instant timeBefore = Instant.now();
        SuffixTree test = new SuffixTree("");
        //readFromFilesAsBytes();
        ArrayList<String> allData = readFromFiles();
        for (String data: allData) {
            //placeholder to remove the new line character
            data = truncate(data);
            for (int i = 0; i < data.length(); i += 125) {
                test.addStringToTree(data.substring(i, Math.min(i + 125, data.length() - 1)));
            }
        }
        //test.printSuffixTree();
        System.out.println("Number of nodes is: " + test.getNodeNum());
        Instant timeAfter = Instant.now();
        Duration duration = Duration.between(timeBefore, timeAfter);
        System.out.println(duration);
    }

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

    public static String truncate(String s) {
        if (s.charAt(s.length()-1) == '\n') {
            s = s.substring(0, s.length() - 1);
        }
        return s;
    }

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
