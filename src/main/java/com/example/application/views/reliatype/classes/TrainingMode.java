package com.example.application.views.reliatype.classes;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

import com.example.application.views.MainLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@PageTitle("Reliatype")
@Route(value = "reliatype/training", layout = MainLayout.class)
public class TrainingMode extends GameLayout {
    private int numOfWordsToGenerate;

    private File wordListFile;
    private String textToDisplay;

    public TrainingMode() {
        super();

        // Initialize Level
        numOfWordsToGenerate = 10;

        TextField wordCountInput = new TextField();
        wordCountInput.setAllowedCharPattern("[0-9]");
        wordCountInput.setLabel("Number of Words to Type: ");
        wordCountInput.setValue("10");
        wordCountInput.setHelperText("Words");
        wordCountInput.addValueChangeListener(e -> {
            numOfWordsToGenerate = Integer.parseInt(wordCountInput.getValue());
        });

        TextField wpmThresholdInput = new TextField();
        wpmThresholdInput.setAllowedCharPattern("[0-9]");
        wpmThresholdInput.setLabel("Minimum WPM: ");
        wpmThresholdInput.setValue("30");
        wpmThresholdInput.setHelperText("WPM");
        wpmThresholdInput.addValueChangeListener(e -> {
            setWpmThreshold(Integer.parseInt(wpmThresholdInput.getValue()));
        });

        TextField secondsTillDeathInput = new TextField();
        secondsTillDeathInput.setAllowedCharPattern("[0-9]");
        secondsTillDeathInput.setLabel("Seconds until death: ");
        secondsTillDeathInput.setValue("4");
        secondsTillDeathInput.setHelperText("Seconds");
        secondsTillDeathInput.addValueChangeListener(e -> {
            setSecondsTillDeath(Integer.parseInt(secondsTillDeathInput.getValue()));
        });

        super.add(wordCountInput, wpmThresholdInput, secondsTillDeathInput);

        loadLevel();
    }

    public void youLose() {
        this.resetCount();
        loadLevel();
    }

    public void youWin() {
        this.resetCount();
        resetInputField();
        loadLevel();
    }

    public void loadLevel() {
        // level = new Level(levelNumber);
        // wordListFile = level.getLevel();
        // timerSet = false;

        // // Read list
        // try {
        //     Scanner scan = new Scanner(wordListFile);

        //     textToDisplay = "";
        //     while (scan.hasNextLine()) {
        //         textToDisplay = textToDisplay + scan.nextLine() + "\n";
        //     }
        //     beforeCursor.setText("");
        //     cursor.setText("");
        //     afterCursor.setText(textToDisplay);
        //     textToType = textToDisplay;
        //     numberOfCharsToType = textToDisplay.length() - 1;

        //     scan.close();

        // } catch (IOException e) {
        //     System.err.println(e.getMessage());
        // }


        timerSet = false;

        try {
            functionality.initializeNewList();
            functionality.createGameText(numOfWordsToGenerate);
        } catch (IOException e2) {
            e2.printStackTrace();
        }

        wordListFile = new File(functionality.randomListPath);
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

        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        }

        inputText.setVisible(true);

        
    
        //loadLevelData();

        wpmCounter.setClassName("text-normal");
        countWords(textToDisplay);
    }

}
