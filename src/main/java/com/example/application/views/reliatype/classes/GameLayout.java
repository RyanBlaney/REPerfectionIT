package com.example.application.views.reliatype.classes;

import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.data.value.ValueChangeMode;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.Timer;

import com.example.application.views.MainLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.shared.Registration;

@PageTitle("Reliatype")
@Route(value = "reliatype/game", layout = MainLayout.class)
public abstract class GameLayout extends VerticalLayout {
    protected HorizontalLayout gameLayout;
    protected Scroller displayTextScrollPane;

    // HTML that breaks up text
    protected Paragraph wordListContainer;
    protected Span beforeLevel;
    protected Span beforeCursor;
    protected Span afterCursor;
    protected Span cursor;
    protected Span afterLevel;

    protected TextArea inputText;
    protected String completedText; // The text to diplay before the current level
    protected String previewText; // The text after level that previews the next level
    protected String textToType; // The current level text

    protected Div statsContainer;
    protected H3 secondCounter;
    protected H3 wpmCounter;
    protected H3 wordCounter;

    protected Timer timer;

    protected ArrayList<String> wordsInText;

    protected int charCount; // The number of chars currently type relative to the LEVEL
    protected int charCountContext; // The number of chars typed BEFORE the current level 
    protected int numberOfCharsToType;

    protected boolean timerSet; // Has the user started typing
    protected int secondsPassed; // How much time has passed
    protected int wpm; // Current Words per Minute
    protected int wpmThreshold; // The WPM the typer be above
    protected boolean thresholdTimerSet; // Starts when the WPM < wpmThreshold
    protected int secondsPastThreshold; // The number of seconds passed since the thresholdTimerSet is true
    protected int secondsTillDeath; // When secondsPastThreshold reaches this number, death occurs
    protected int wordCount; // How many words have been typed
    private int wordCharIndex; // How many characters until a full word is typed

    Registration keyPressListener;

    public GameLayout() {
        charCountContext = 0;

        initializeText();
        initializeInput();
        initializeStats();

        // Match with wordListContainer input
        resetCount();

        initializeKeyListener();

        setMargin(true);

        add(gameLayout, inputText, statsContainer);

        timerSet = false;
        countTime();
        timer.start();
    }

    public void initializeText() {
        gameLayout = new HorizontalLayout();
        
        completedText = "";
        previewText = "";

        // The text before the level begins
        beforeLevel = new Span();
        beforeLevel.setText("");
        beforeLevel.setClassName("p-before-level");

        // The text before the cursor (green)
        beforeCursor = new Span();
        beforeCursor.setText("");
        beforeCursor.setClassName("p-before-cursor");

        // This is the character that is currently being typed
        cursor = new Span();
        cursor.setText("");
        cursor.setClassName("p-cursor");

        // This is the text after the cursor
        afterCursor = new Span();
        afterCursor.setText("");
        afterCursor.setClassName("p-after-cursor");

        // This is the text after the level that has yet to be reached
        afterLevel = new Span();
        afterLevel.setText("");
        afterLevel.setClassName("p-after-level");

        wordListContainer = new Paragraph();
        wordListContainer.setWidth(705, Unit.PIXELS);
        wordListContainer.setMaxHeight(256, Unit.PIXELS);
        // wordListContainer.setReadOnly(true);
        wordListContainer.add(beforeLevel, beforeCursor, cursor, afterCursor, afterLevel);

        // Add display text to scroll pane
        displayTextScrollPane = new Scroller(wordListContainer);
        displayTextScrollPane.setWidth(720, Unit.PIXELS);
        displayTextScrollPane.setHeight(256, Unit.PIXELS);

        gameLayout.add(displayTextScrollPane);

    }

    public void initializeInput() {
        inputText = new TextArea("");
        inputText.setWidth(720, Unit.PIXELS);
        inputText.setValueChangeMode(ValueChangeMode.EAGER);
    }

    public void initializeStats() {
        secondsPassed = 0;
        wpm = 0;
        wpmThreshold = 40;
        thresholdTimerSet = false;
        secondsPastThreshold = 0;
        secondsTillDeath = 3;
        wordCount = 0;

        secondCounter = new H3();
        secondCounter.setVisible(true);
        secondCounter.setText("Seconds Passed: 0");

        wpmCounter = new H3();
        wpmCounter.setVisible(true);
        wpmCounter.setText("WPM: 0");

        wordCounter = new H3();
        wordCounter.setVisible(true);
        wordCounter.setText("Words Typed: 0");

        statsContainer = new Div();
        statsContainer.setWidth(720, Unit.PIXELS);
        statsContainer.add(secondCounter, wpmCounter, wordCounter);

    }

    public abstract void loadLevel();

    public abstract void youLose();

    public abstract void youWin();

    protected void resetCount() {
        charCount = 0;
    }

    protected void incrementCount() {
        charCount++;
    }

    public void resetInputField() {
        inputText.clear();
    }

    public void initializeKeyListener() {
        keyPressListener = inputText.addKeyPressListener(e -> {
            if (e.getKey().toString().equals(String.valueOf(textToType.charAt(charCount)))) {

                incrementCount();

                if (!timerSet) {
                    wpm = 0;
                    wordCount = 0;
                    secondsPassed = 0;
                    wordCharIndex = 0;

                    timerSet = true;
                }

                beforeCursor.setText(textToType.substring(0, charCount));
                cursor.setText(String.valueOf(textToType.charAt(charCount)));
                if (charCount + 1 <= numberOfCharsToType)
                    afterCursor.setText(textToType.substring(charCount + 1));
                else
                    afterCursor.setText("");

                if (numberOfCharsToType <= previewText.length()) {
                    afterLevel.setText(previewText.substring(0, charCount));
                } else if (numberOfCharsToType - charCount < previewText.length()) {
                    afterLevel.setText(previewText.substring(0, previewText.length() - (numberOfCharsToType - charCount)));
                } 

                incrementWordCount();

                // Check if the user wins
                if (charCount == numberOfCharsToType) {
                    youWin();
                }
            } else
                youLose();
        });
    }

    private void incrementWordCount() {
        // Count words
        wordCharIndex++;
        if (wordCharIndex == wordsInText.get(wordCount).length() + 1) {
            wordCount++;
            wordCharIndex = 0;
            wordCounter.setText("You type " + String.valueOf(wordCount) + " words");
        }
    }

    /**
     * Counts the time, WPM, and ensures the player is typing fast enough
     * 
     */
    protected void countTime() {

        timer = new Timer(1000, new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                secondsPassed++;
                if (timerSet) {
                    getUI().ifPresent(ui -> {
                        ui.access(() -> {
                            secondCounter.setText(secondsPassed + " seconds have passed");
                            wpm = (wordCount * 60) / (secondsPassed);
                            wpmCounter.setText(wpm + " WPM");

                            // Under WPM Threshold
                            if (wpmThreshold != 0) {
                                if (wpm < wpmThreshold && wpm != 0) {
                                    if (!thresholdTimerSet) {
                                        secondsPastThreshold = secondsPassed;
                                        wpmCounter.setClassName("text-red");
                                        thresholdTimerSet = true;
                                    }
                                    if ((secondsPassed - secondsPastThreshold) >= secondsTillDeath) {
                                        youLose();
                                    }
                                } else {
                                    wpmCounter.setClassName("text-green");
                                    thresholdTimerSet = false;
                                }
                            } else
                                wpmCounter.setClassName("text-normal");
                        });
                    });
                }
            }
        });
    }

    /**
     * Updates the level start position on the completion of a section
     * 
     */
    public void adjustLevelStartToCurrent() {
        this.completedText += textToType;
        this.charCountContext = this.charCount;
    }

    /**
     * To be called by the seek and skip buttons. Adjusts the level start position
     *  
     * @param nudgeByAmount The amount (either + or -) to increase the level start by
     */
    public void adjustLevelStart(int nudgeByAmount) {
        this.charCountContext += nudgeByAmount;
        if (this.charCountContext < 0) this.charCountContext = 0;
    }

    protected int getLevelLength(String level) {
        Scanner scan = new Scanner(level);
        String levelString = "";

        while (scan.hasNextLine()) {
            levelString = levelString + scan.nextLine() + "\n";
        }
        scan.close();
        return levelString.length();
    }

    /**
     * Counts the number of words typed and adds them to the currentWord Arraylist
     * 
     * @param levelToCount
     */
    public void countWords(String levelToCount) {
        String wordListFile = levelToCount;
        Scanner scanList;
        scanList = new Scanner(wordListFile);
        wordsInText = new ArrayList<>();
        while (scanList.hasNext()) {
            wordsInText.add(scanList.next());
        }
        scanList.close();

    }

    protected void setSecondsTillDeath(int secTillDeath) {
        this.secondsTillDeath = secTillDeath;
    }

    protected void setWpmThreshold(int threshold) {
        this.wpmThreshold = threshold;
    }
}
