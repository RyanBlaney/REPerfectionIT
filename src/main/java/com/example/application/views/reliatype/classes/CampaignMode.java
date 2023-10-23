package com.example.application.views.reliatype.classes;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import com.example.application.views.MainLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@PageTitle("Reliatype")
@Route(value = "reliatype/campaign", layout = MainLayout.class)
public class CampaignMode extends GameLayout {
    private int levelNumber;
    private Level level;

    private File wordListFile;
    private String textToDisplay;

    public CampaignMode() {
        super();

        // Initialize Level
        levelNumber = 1;
        loadLevel();
    }

    public void youLose() {
        this.resetCount();
        loadLevel();
    }

    public void youWin() {
        this.resetCount();
        resetInputField();
        this.levelNumber++;
        loadLevel();
    }

    public void loadLevel() {
        level = new Level(levelNumber);
        wordListFile = level.getLevel();
        timerSet = false;

        // Read list
        try {
            Scanner scan = new Scanner(wordListFile);

            textToDisplay = "";
            while (scan.hasNextLine()) {
                textToDisplay = textToDisplay + scan.nextLine() + "\n";
            }
            beforeCursor.setText("");
            cursor.setText("");
            afterCursor.setText(textToDisplay);
            textToType = textToDisplay;
            numberOfCharsToType = textToDisplay.length() - 1;

            scan.close();

        } catch (IOException e) {
            System.err.println(e.getMessage());
        }


        //loadLevelData();

        wpmCounter.setClassName("text-normal");
        countWords(textToDisplay);
    }

}
