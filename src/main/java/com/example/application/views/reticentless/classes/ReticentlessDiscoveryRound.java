package com.example.application.views.reticentless.classes;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.ArrayList;
import java.util.List;

public class ReticentlessDiscoveryRound {

    //private static String wordGenSite = "https://randomwordgenerator.com/weird-word.php";
    private static String wordGenSite = "https://www.randomlists.com/random-vocabulary-words";
    //private static String exampleGenSite = "https://sentence.yourdictionary.com/";
    private static String exampleGenSite = "https://www.dictionary.com/browse/";

    private static String thesaurusSite = "https://www.thesaurus.com/browse/";
    private static ArrayList<ReticentlessWord> fullWordList;
    private static ArrayList<ReticentlessWord> newWords;

    public static void initializeWordGenerator(ArrayList<ReticentlessWord> wordList) {
        try {

            // Initialize Chrome Driver
            ChromeDriver chromeDriver = new ChromeDriver();
            chromeDriver.manage().window().minimize();

            // Initialize the new wordlist and full list pointer
            fullWordList = wordList;
            newWords = new ArrayList<>();

            // Get 20 Words (will be less than 20 if a duplicate is generated)
            generateFiveWords(chromeDriver);
            generateFiveWords(chromeDriver);
            generateFiveWords(chromeDriver);
            generateFiveWords(chromeDriver);

            generateExamples(chromeDriver);
            fullWordList.addAll(newWords);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void generateFiveWords(ChromeDriver driver) {
        try {
            driver.get(wordGenSite);

            // Get Generated List
            WebElement fiveWords = driver.findElement(By.className("Rand-stage"));
            List<WebElement> wordList = fiveWords.findElements(By.tagName("span"));

            // Add Words to full list
            for (int i = 0; i < 5; i++) {
                ReticentlessWord wordToAdd = new ReticentlessWord(wordList.get(i * 2).getText(), wordList.get(i * 2 + 1).getText());
                if (!existingWord(wordToAdd)) newWords.add(wordToAdd);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void generateExamples(ChromeDriver driver) {
        try {

            for (ReticentlessWord word : newWords) {

                driver.get(exampleGenSite + word.getWordText());

                // Get Generated List
//                List<WebElement> wordList = driver.findElements(By.className("sentence-item__text"));
                WebElement exampleSection = driver.findElement(By.id("examples"));
                List<WebElement> wordList = exampleSection.findElements(By.tagName("p"));


                // Add examples to example list
                for (int i = 0; i < 4; i++) {
                    word.getSentenceExamples().add(wordList.get(i).getText());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean existingWord(ReticentlessWord wordToAdd) {
        for (ReticentlessWord word : fullWordList)
            if (word.getWordText().equals(wordToAdd.getWordText()))
                return true;
        return false;
    }
}
