package com.example.application.views.reliatype.classes;

/**
 * This is the back-end functionality of the program, which creates the word list based on the dataset provided in 'word_list.txt'
 * 
 * FEATURES: 5, 2, 3
 * 
 * @author Ryan Blaney
 * @updated 10/27/2022
 */

import java.util.Scanner;
import java.util.ArrayList;
import java.io.*;
import java.nio.file.*;
import java.util.*;

public class functionality {
    // User Settings
    public static int charsInLine = 5000;
    public static int chanceOfCaps = 5;

    public static int charIndex = 0; // Number of chars in the line
    public static int charCount = 0; // Number of chars in the word
    public static String currentWord = "";
    public static int maxAvailableWords = 0;
    public static ArrayList<String> wordArrayList = new ArrayList<String>();
    public static File textToType;

    public static String randomListPath = "src/main/java/com/example/application/views/reliatype/files/new_word_list.txt";
    public static String wordListPath = "src/main/java/com/example/application/views/reliatype/files/word_list.txt";

    public static void initializeNewList() throws IOException {
        // Open and Read File
        File wordList = new File(wordListPath);
        Scanner scan = new Scanner(wordList);

        // Create and clean the new word list
        BufferedWriter newWordList = Files
                .newBufferedWriter(Paths.get(randomListPath));
        newWordList.write("");
        newWordList.flush();

        // Go through word_list and add to list array
        maxAvailableWords = 0;
        while (scan.hasNextLine()) {
            wordArrayList.add(scan.nextLine());
            maxAvailableWords++;
        }
        scan.close();

        // Create and organize the text to be typed
        textToType = new File(randomListPath);
        // createGameText(100);

    }

    /**
     * addToNewList
     * Adds an input word to the text file 'new_word_list.txt'
     * 
     * @param word
     * @throws IOException
     */
    public static void addToNewList(String word) throws IOException {
        BufferedWriter newWordList = new BufferedWriter(
                new FileWriter(randomListPath, true));

        newWordList.write(word + "\n");
        newWordList.close();
    }

    /**
     * createGameText
     * Picks a random int from 0 to the maximum number of words available in
     * 'word_list.txt'
     * and adds the respective word to the 'new_word_list.txt'
     * 
     * @param wordCount
     * @throws IOException
     */
    public static void createGameText(int wordCount) throws IOException {
        Random random = new Random();
        for (int i = 0; i <= wordCount; i++) {
            addToNewList(wordArrayList.get(random.ints(0, maxAvailableWords).findFirst().getAsInt()));
        }
        organizeNewList();
    }

    /**
     * organizeNewList
     * Breaks the line whenever it hits the 'charsInALine' variable limit
     * 
     * @throws IOException
     * 
     */
    public static void organizeNewList() throws IOException {
        // Settup scanner and string list
        Scanner scan = new Scanner(textToType);
        ArrayList<String> backupArrayList = new ArrayList<String>();

        // Backup list contents
        int maxNums = 0;
        while (scan.hasNextLine()) {
            backupArrayList.add(scan.nextLine());
            maxNums++;
        }

        // Capitalize list
        if (chanceOfCaps != 0) {
            String cappedWord = "";
            Random capChance = new Random();
            for (int i = 0; i < backupArrayList.size(); i++) {
                backupArrayList.get(i).toLowerCase();
                for (int j = 0; j < backupArrayList.get(i).length(); j++) {
                    if (capChance.ints(1, chanceOfCaps).findFirst().getAsInt() == 1) {
                        if (j != 0) {
                            cappedWord = backupArrayList.get(i).substring(0, j)
                                    + backupArrayList.get(i).substring(j, j + 1).toUpperCase()
                                    + backupArrayList.get(i).substring(j + 1, backupArrayList.get(i).length());
                        } else {
                            cappedWord = backupArrayList.get(i).substring(0, 1).toUpperCase()
                                    + backupArrayList.get(i).substring(1, backupArrayList.get(i).length());
                        }
                        backupArrayList.set(i, cappedWord);
                    }
                }
            }
        }

        // Clean list
        BufferedWriter newWordList = Files
                .newBufferedWriter(Paths.get(randomListPath));
        newWordList.write("");
        newWordList.flush();

        charCount = 0;
        charIndex = 0;
        for (int index = 0; index < maxNums; index++) {
            for (int i = 0; i < backupArrayList.get(index).length(); i++) {
                if (backupArrayList.get(index).charAt(i) != ' ')
                    charCount++;
            }
            charIndex += charCount;
            if (charIndex < charsInLine) {
                newWordList.write(backupArrayList.get(index) + " ");
                newWordList.flush();
                charCount = 0;

            } else {
                // Print word and reset the counter
                newWordList.write("\n" + backupArrayList.get(index) + " ");
                newWordList.flush();
                charIndex = 0;
                charCount = 0;
            }
        }
        scan.close();
    }

    /**
     * Humiliation Station generates and returns a random word from the
     * 'insult_list.txt'
     * 
     * @throws FileNotFoundException
     * @return String insult
     */
    public static String humiliationStation() throws FileNotFoundException {
        File insult_list = new File("src/files/insult_list.txt");
        Scanner scanInsult = new Scanner(insult_list);

        ArrayList<String> insultList = new ArrayList<>();

        // Add to an ArrayList
        while (scanInsult.hasNextLine()) {
            insultList.add(scanInsult.nextLine());
        }
        scanInsult.close();

        // Randomize and return insult
        Random random = new Random();
        return insultList.get(random.ints(0, insultList.size()).findFirst().getAsInt());
    }

}
