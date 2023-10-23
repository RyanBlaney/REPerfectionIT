/**
 * Level loads the word list and messages for each level based on the number in the file names.
 * 
 * @author Ryan Blaney
 * @version 12/1/2022
 */

 package com.example.application.views.reliatype.classes;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Level {
    private int level;
    private int levelNumber;

    /**
     * Level Constructor to initialize the level object
     * 
     */
    public Level(int levelNum) {
        levelNumber = levelNum;
        level = levelNumber;
    }

    /**
     * getLevel() returns the file of the level.txt word list
     * 
     */
    public File getLevel() {
        return new File("src/main/java/com/example/application/views/reliatype/word_lists/level" + level + ".txt");
    }

    /**
     * getWinMessage loads the message .txt file in the messages folder and returns
     * the first line
     * 
     * @return
     */
    public String getWinMessage() throws FileNotFoundException {
        Scanner scan = new Scanner(new FileInputStream("src/messages/level" + level + ".txt"));
        scan.nextLine();
        String message = scan.nextLine();
        scan.close();
        return message;
    }

    /**
     * getLevelNumber() returns the level number
     * 
     */
    public int getLevelNumber() {
        return levelNumber;
    }

    /**
     * getMaxDeaths returns the maximum allowed deaths for a level. Is the first
     * line of a messages .txt file. 0 means there is no limit
     * 
     * @return the maximum allowed deaths
     * @throws FileNotFoundException
     */
    public int getWPMThreshold() throws FileNotFoundException {
        Scanner scan = new Scanner(new FileInputStream("src/messages/level" + level + ".txt"));
        return scan.nextInt();
    }

    /**
     * getLoseMessage() returns a random message from the second line of the message
     * file to the last line of the file
     * 
     * @return
     * @throws FileNotFoundException
     */
    public String getLoseMessage() throws FileNotFoundException {
        Scanner scan = new Scanner(new FileInputStream("src/messages/level" + level + ".txt"));
        ArrayList<String> messageList = new ArrayList<>();
        while (scan.hasNextLine()) {
            messageList.add(scan.nextLine());
        }
        scan.close();
        int random = new Random().ints(1, 2, messageList.size()).findFirst().getAsInt();
        String message = messageList.get(random);
        return message;
    }
}
