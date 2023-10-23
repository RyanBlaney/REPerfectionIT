package com.example.application.views.reticentless;

import com.example.application.views.MainLayout;
import com.example.application.views.reticentless.classes.ReticentlessDiscoveryRound;
import com.example.application.views.reticentless.classes.ReticentlessWord;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import com.vaadin.flow.shared.Registration;

import java.lang.reflect.Array;
import java.util.*;

@PageTitle("Reticentless")
@Route(value = "reticentless", layout = MainLayout.class)
@RouteAlias(value = "reticentless", layout = MainLayout.class)
public class ReticentlessMain extends HorizontalLayout {

    // Main UI
    private VerticalLayout gameLayout;
    private H2 gameName;
    private Paragraph description;
    private Button startGameButton;
    private Registration startGameButtonHandler;
    private Div promptDiv;
    private VerticalLayout answerChoiceLayout;
    private HorizontalLayout statsLayout;
    private H2 scoreLabel;
    private H2 momentumLabel;
    private H2 healthLabel;

    // Stats
    private int maxHealth;
    private int health; // When this hits 0 the player dies
    private float momentum; // The point multiplier
    private int points; // The number of points the player has

    // Game Data

    private static ArrayList<ReticentlessWord> fullWordList;

    private boolean isHybridRound;

    public ReticentlessMain() {
        initializeMenu();
    }

    public void initializeMenu() {
        gameLayout = new VerticalLayout();
        gameLayout.setWidth(960, Unit.PIXELS);

        gameName = new H2("Reticentless");
        description = new Paragraph("Reticentless is an absolutely unforgiveable vocab improvement game that will free you of your short-term memory loss. You will be in a constant state of fight-or-flight and must rely on momentum in order to survive!");
        startGameButton = new Button("Enter the Guantlet");
        startGameButtonHandler = startGameButton.addClickListener(e -> {
            initializeGame();
        });
        startGameButton.addClickShortcut(Key.ENTER);

        promptDiv = new Div();

        setMargin(true);
        setVerticalComponentAlignment(Alignment.START, gameName, description, startGameButton);

        // Answer Choices
        initializeAnswerChoices();

        statsLayout = new HorizontalLayout();

        gameLayout.add(gameName, description, startGameButton, statsLayout, promptDiv, answerChoiceLayout);
        add(gameLayout);
    }

    public void initializeGame() {
        startGameButtonHandler.remove();

        isHybridRound = false;

        gameName.setText("Round 1: Discovery");
        description.setText("You will be given several sentences and each one will contain an unfamiliar word. Given 4 definitions you must pick the definition that best represents the underlined word. Note: it takes a while to load the initial set of words, you may need to allow popups.");
        startGameButton.setText("Begin");
        startGameButtonHandler = startGameButton.addClickListener(e -> {
            beginDiscoveryRound();
        });

        scoreLabel = new H2("Score: 0");
        momentumLabel = new H2(String.format("%.3fx", momentum));
        healthLabel = new H2("HP: " + health + "/" + maxHealth);
        statsLayout.setVisible(false);
        statsLayout.add(scoreLabel, momentumLabel, healthLabel);
    }

    public void beginDiscoveryRound() {
        loadProgress();
        resetStats();

        disableButton(startGameButton, startGameButtonHandler);

        if (fullWordList == null) fullWordList = new ArrayList<>();
        ReticentlessDiscoveryRound.initializeWordGenerator(fullWordList);

        int numberOfRounds = 3;
        discoveryRound(numberOfRounds);

    }

    public void resetStats() {
        momentum = 1;
        health = maxHealth;
        points = 0;

        updateStats();
    }

    public void updateStats() {
        scoreLabel.setText("Score: " + points);
        healthLabel.setText("HP: " + health + "/" + maxHealth);
        momentumLabel.setText(momentum + "x");
    }

    public void discoveryRound(int numOfRoundsLeft) {
        if (numOfRoundsLeft <= 0) {
            gameName.setText("Round 2: Matching");
            beginMatchingRound();
            return;
        }

        statsLayout.setVisible(true);

        // Pick example sentence
        ReticentlessWord randomWord = getRandomWord(fullWordList);
        String exampleToDisplay = getRandomString(randomWord.getSentenceExamples());
        while (!exampleToDisplay.contains(randomWord.getWordText())) {
            fullWordList.remove(randomWord);
            randomWord = getRandomWord(fullWordList);
            exampleToDisplay = getRandomString(randomWord.getSentenceExamples());
        }
        ReticentlessWord currentWord = randomWord;
        String exampleText = exampleToDisplay;

        description.setText("What is the definition of the highlighted word?");

        // Break up example text
        Span beforeWordInExample = new Span(exampleText.substring(0, exampleText.indexOf(currentWord.getWordText())));
        Span wordInExample = new Span(exampleText.substring(exampleText.indexOf(currentWord.getWordText()), exampleText.indexOf(currentWord.getWordText()) + currentWord.getWordText().length()));
        wordInExample.setClassName("p-before-cursor");
        Span afterWordInExample = new Span(exampleText.substring(exampleText.indexOf(currentWord.getWordText()) + currentWord.getWordText().length()));
        promptDiv.removeAll();
        promptDiv.add(beforeWordInExample, wordInExample, afterWordInExample);

        int numberOfAnswerChoices = 4;
        ArrayList<ReticentlessWord> answerChoices = new ArrayList<>();
        answerChoices.add(currentWord);

        while (answerChoices.size() < numberOfAnswerChoices) {
            ReticentlessWord wordToAdd = getRandomWord(fullWordList);
            if (!answerChoices.contains(wordToAdd)) answerChoices.add(wordToAdd);
        }

        answerChoiceLayout.removeAll();
        for (int i = 0; i < numberOfAnswerChoices; i++) {
            ReticentlessWord wordToDisplay = getRandomWord(answerChoices);

            Button answerButton = new Button(wordToDisplay.getWordDefinition());
            answerButton.addClickListener(e -> {
                if (wordToDisplay.getWordText().equals(currentWord.getWordText())) {
                    answerIsCorrect(currentWord);
                    if (!isHybridRound)
                        discoveryRound(numOfRoundsLeft - 1);
                    else hybridRound(numOfRoundsLeft - 1);
                }
                else {
                    answerIsWrong(currentWord);
                    discoveryRound(numOfRoundsLeft);
                }
            });
            answerChoiceLayout.add(answerButton);
            answerChoices.remove(wordToDisplay);
        }
    }

    public void beginMatchingRound() {
        description.setText("What word best represents the definition?");

        int numberOfRounds = 3;
        matchingRound(numberOfRounds);

    }

    public void matchingRound(int numOfRoundsLeft) {
        if (numOfRoundsLeft <= 0) {
            gameName.setText("Round 3: Spelling");
            beginSpellingRound();
            return;
        }

        ReticentlessWord currentWord = getRandomWord(fullWordList);

        promptDiv.removeAll();
        Span definition = new Span(currentWord.getWordDefinition());
        promptDiv.add(definition);

        int numberOfAnswerChoices = 4;
        ArrayList<ReticentlessWord> answerChoices = new ArrayList<>();
        answerChoices.add(currentWord);

        while (answerChoices.size() < numberOfAnswerChoices) {
            ReticentlessWord wordToAdd = getRandomWord(fullWordList);
            if (!answerChoices.contains(wordToAdd)) answerChoices.add(wordToAdd);
        }

        answerChoiceLayout.removeAll();
        for (int i = 0; i < numberOfAnswerChoices; i++) {
            ReticentlessWord wordToDisplay = getRandomWord(answerChoices);

            Button answerButton = new Button(wordToDisplay.getWordText());
            answerButton.addClickListener(e -> {
                if (wordToDisplay.getWordText().equals(currentWord.getWordText())) {
                    answerIsCorrect(currentWord);
                    if (!isHybridRound)
                        matchingRound(numOfRoundsLeft - 1);
                    else hybridRound(numOfRoundsLeft - 1);
                }
                else {
                    answerIsWrong(currentWord);
                    matchingRound(numOfRoundsLeft);
                }
            });
            answerChoiceLayout.add(answerButton);
            answerChoices.remove(wordToDisplay);
        }
    }

    public void beginSpellingRound() {
        description.setText("Spell the word the definition is referring to.");

        int numberOfRounds = 2;
        spellingRound(numberOfRounds);

    }

    public void spellingRound(int numOfRoundsLeft) {
        if (numOfRoundsLeft <= 0) {
            beginHybridRound();
            return;
        }

        ReticentlessWord currentWord = getRandomWord(fullWordList);

        promptDiv.removeAll();
        Span definition = new Span(currentWord.getWordDefinition());
        promptDiv.add(definition);

        answerChoiceLayout.removeAll();

        Input inputField = new Input();

        Button submitAnswerButton = new Button("Submit");
        submitAnswerButton.addClickListener(e -> {
            if (currentWord.getWordText().toLowerCase().equalsIgnoreCase(inputField.getValue())) {
                answerIsCorrect(currentWord);
                if (!isHybridRound)
                    spellingRound(numOfRoundsLeft - 1);
                else hybridRound(numOfRoundsLeft - 1);
            } else {
                answerIsWrong(currentWord);
                spellingRound(numOfRoundsLeft);
            }
        });
        submitAnswerButton.addFocusShortcut(Key.ENTER);

        answerChoiceLayout.add(inputField, submitAnswerButton);
    }

    public void beginHybridRound() {
        gameName.setText("Round 4: Hybrid");

        isHybridRound = true;
        int numberOfRounds = 12;

        hybridRound(numberOfRounds);
    }

    public void hybridRound(int numOfRounds) {
        if (numOfRounds <= 0) {
            gameName.setText("Loading new word list.");
            description.setText("This will take some time and will be added onto your current list.");

            beginDiscoveryRound();

            gameName.setText("Round 1: Discovery");
            description.setText("You will be given several sentences and each one will contain an unfamiliar word. Given 4 definitions you must pick the definition that best represents the underlined word.");

            return;
        }

        Random random = new Random();
        int roundType = random.nextInt(0, 3);
        switch (roundType) {
            case 0 -> discoveryRound(numOfRounds);
            case 1 -> spellingRound(numOfRounds);
            case 2 -> matchingRound(numOfRounds);
        }
    }

    public void answerIsCorrect(ReticentlessWord currentWord) {
        // Set momentum
        momentum = momentum + .25f;
        points = (int) ((float) points + momentum * 3);
        updateStats();

        currentWord.setMastery(currentWord.getMastery() + 1 * momentum);
        currentWord.setTimesPassed(currentWord.getTimesPassed() + 1);
        Notification.show("Correct! Mastery for " + currentWord.getWordText() + " is " + String.format("%.3fx", currentWord.getMastery()));
    }

    public void answerIsWrong(ReticentlessWord currentWord) {
        // Set momentum
        momentum = momentum - (momentum * .5f);
        if (momentum < .5f) momentum = .5f;

        currentWord.setMastery(currentWord.getMastery() - 1 * momentum);
        if (currentWord.getMastery() < 0) currentWord.setMastery(0);
        currentWord.setTimesFailed(currentWord.getTimesFailed() + 1);
        Notification.show("Wrong! Mastery for " + currentWord.getWordText() + " is " + String.format("%.3fx", currentWord.getMastery()));

        health = (int) ((float) health - momentum * 3);
        updateStats();
        if (health <= 0)  {
            updateStats();
            health = 0;
            gameOver();
        }
    }

    public void gameOver() {

    }

    /**
     * Until I use cookies to load data, this will be hard coded
     * 
     */
    public void loadProgress() {
        maxHealth = 20;
        health = maxHealth;
        points = 0;
        momentum = 1;
    }

    public void initializeAnswerChoices() {
        answerChoiceLayout = new VerticalLayout();


    }

    public void disableButton(Button buttonToRemove, Registration handlerToRemove) {
        buttonToRemove.setEnabled(false);
        buttonToRemove.setVisible(false);
        handlerToRemove.remove();
    }

    public ReticentlessWord getRandomWord(ArrayList<ReticentlessWord> list) {
        Random random = new Random();

        HashMap<Integer, ReticentlessWord> skewedList = new HashMap<>();
        int currentIndex = 0;
        for (ReticentlessWord word : list) {
            int numOfTickets = (int) (50 - (Math.log10(word.getMastery()) * 29));
            if (numOfTickets < 1) numOfTickets = 1;
            for (int i = 0; i < numOfTickets; i++) {
                skewedList.put(currentIndex, word);
                currentIndex++;
            }
        }

        int wordIndex = random.nextInt(0, skewedList.size());
        return skewedList.get(wordIndex);
    }

    public String getRandomString(ArrayList<String> list) {
        Random random = new Random();
        int wordIndex = random.nextInt(0, list.size());
        return list.get(wordIndex);
    }

}
