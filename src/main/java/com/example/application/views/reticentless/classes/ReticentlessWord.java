package com.example.application.views.reticentless.classes;

import java.util.ArrayList;

public class ReticentlessWord {
    private String wordText;
    private String wordDefinition;
    private ArrayList<String> sentenceExamples;
    private int length;
    private float mastery;
    private int timesPassed;
    private int timesFailed;

    public ReticentlessWord(String wordName, String shortDefinition) {
        this.setText(wordName, shortDefinition);
        this.resetProgress();
        this.sentenceExamples = new ArrayList<>();
    }

    public void resetProgress() {
        this.mastery = 0;
        this.timesFailed = 0;
        this.timesPassed = 0;
    }

    public void setText(String name, String definition) {
        this.wordText = name;
        this.wordDefinition = definition;
        this.length = name.length();
        this.sentenceExamples = new ArrayList<>();
    }

    public String getWordText() {
        return this.wordText;
    }

    public String getWordDefinition() { return this.wordDefinition; }

    public int getLength() {
        return this.length;
    }

    public float getMastery() {
        return this.mastery;
    }

    public int getTimesFailed() {
        return this.timesFailed;
    }

    public int getTimesPassed() {
        return this.timesPassed;
    }

    public void setTimesPassed(int value) {
        this.timesPassed = value;
    }

    public void setTimesFailed(int value) {
        this.timesFailed = value;
    }

    public void setMastery(float value) {
        this.mastery = value;
    }

    public ArrayList<String> getSentenceExamples() { return this.sentenceExamples; }
}
