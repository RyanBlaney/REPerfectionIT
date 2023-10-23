package com.example.application.views.reliatype.classes;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Loads the UI Elements of the Campaign Story Mode and makes the game playable.
 * Is very similar to the Training Mode Class, but loads each individual level
 * and message from the respective text documents
 * 
 * FEATURES: 6, 4
 * 
 * @author Ryan Blaney
 * @version 12/6/2022
 */

public class readingMode_copy {

    public static String applicationTitle = mainMenu.applicationTitle;
    public static String realApplicationTitle = mainMenu.realApplicationTitle;
    public static int resolutionW = mainMenu.resolutionW;
    public static int resolutionH = mainMenu.resolutionH;
    public static int posX = mainMenu.posX;
    public static int posY = mainMenu.posY;

    public static Color baseColor = mainMenu.baseColor;
    public static Color textColor = mainMenu.textColor;
    public static Color goldColor = mainMenu.goldColor;
    public static Color darkGoldColor = mainMenu.darkGoldColor;

    public static String bookText;
    public static Document bookDocument;
    public static int chapter = 1;
    public static int sectionIndex = 0;
    public static int maxLength = 300; // max char count
    public static int level = 1;

    public static File wordList;
    public static String textToDisplay = "";
    public static String wordToType = "";
    public static char charToType = ' ';
    public static int charsInWord = 0;

    public static int numOfWordsToGenerate = 10;
    public static int wpmThreshold = 0;
    public static int secondsTillDeath = 3;
    public static Boolean thresholdTimerSet = false;

    public static String textTyped = "";
    public static int charCount = 0;
    public static int wordCount = 0;
    public static int wordCharIndex = 0; // How many chars till a full word is typed
    public static ArrayList<String> currentWord; // The word index to count words
    public static int deathCount = 0;
    public static int winCount = 0;
    public static int charRecord = 0;

    public static boolean isTextReset = true; // Is the buffer since resetting the input text must be delayed

    public static int numberOfSeconds = 0;
    public static int secondsPastThreshold = 0;
    public static int WPM = 0;
    public static boolean isWPMEnabled = true;
    public static Boolean timerSet = false;
    public static Timer timer;

    public static Boolean docListenerCreated = false; // Prevents infinite document listeners to be created
    public static Boolean inMenu = true;

    public static JFrame frame = mainMenu.frame;
    public static JTextPane insult;
    public static JLabel textToType;
    public static JButton startGameButton;
    public static JTextArea inputText;
    public static JTextField deathCounter;
    public static JTextField wordCounter;
    public static JTextField charCounter;
    public static JTextField recordCounter;
    public static JTextField secondCounter;
    public static JTextField wpmCounter;
    public static JTextField lastMistake;
    public static JTextField winCounter;
    public static JScrollPane textPanel;
    public static JPanel inputPanel;
    public static JButton returnToMenuButton;

    public static Boolean practiceOn = false;

    /**
     * Resets to the first level of the campaign mode and creates the UI interface
     * 
     */
    public static void initializeReadingMode() {
        inMenu = false;

        // Add Panel
        JPanel panel = new JPanel();
        panel.setBackground(mainMenu.baseColor);
        panel.setLayout(null);
        panel.setBounds(0, 0, 850, 250);

        // ADD BOOK LIST
        String url = "https://www.gutenberg.org/ebooks/bookshelf/";
        ArrayList<String> categoryURL = new ArrayList<>(1);
        List<String> categoryList = new ArrayList<>(1);
        try {
            Document mainPage = Jsoup.connect(url).get();

            // Search for categories
            for (Element row : mainPage.select(
                    "div.bookshelf_pages ul")) {
                int index = 1;
                while (!row.select("li:nth-of-type(" + index + ")").text().equals("")) {
                    // Get the title
                    String ticker = row.select("li:nth-of-type(" + index + ")").text();
                    categoryList.add(ticker);

                    // Get the link
                    Element link = row.select("li:nth-of-type(" + index + ") a").first();
                    categoryURL.add(link.attr("href"));
                    index++;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        JList<String> list = new JList<String>(categoryList.toArray(new String[categoryList.size()]));
        list.setLayoutOrientation(JList.VERTICAL);
        list.setLayout(null);
        list.setFont(new Font("Gadugi", Font.PLAIN, 22));
        list.setBounds(15, 0, 650, 210);
        list.setBackground(new Color(10, 5, 20));
        list.setForeground(Color.white);

        list.addMouseListener(new MouseListener() {

            @Override
            public void mouseClicked(MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    int index = list.locationToIndex(evt.getPoint());

                    // SHOW BOOK LIST
                    String url = "https://www.gutenberg.org" + categoryURL.get(index);
                    ArrayList<String> bookURL = new ArrayList<>(1);
                    List<String> bookList = new ArrayList<>(1);
                    try {
                        Document categoryPage = Jsoup.connect(url).get();

                        // Search for books
                        for (Element row : categoryPage.select(
                                "li.booklink")) {

                            // Get the title
                            String ticker = row.select("span.title").text() + " by "
                                    + row.select("span.subtitle").text();
                            bookList.add(ticker);

                            // Get the link
                            Element link = row.select("a.link").first();
                            bookURL.add(link.attr("href"));
                        }

                        JList<String> list = new JList<String>(bookList.toArray(new String[bookList.size()]));
                        list.setLayoutOrientation(JList.VERTICAL);
                        list.setLayout(null);
                        list.setFont(new Font("Gadugi", Font.PLAIN, 22));
                        list.setBounds(15, 0, 650, 210);
                        list.setBackground(new Color(10, 5, 20));
                        list.setForeground(Color.white);

                        textPanel.setViewportView(list);

                        list.addMouseListener(new MouseListener() {

                            @Override
                            public void mouseClicked(MouseEvent evt) {
                                if (evt.getClickCount() == 2) {
                                    int index = list.locationToIndex(evt.getPoint());

                                    String url = "https://www.gutenberg.org" + bookURL.get(index) + ".html.images";

                                    try {
                                        bookDocument = Jsoup.connect(url).get();
                                        list.setVisible(false);
                                        list.setEnabled(false);

                                        textPanel.setViewportView(textToType);

                                        loadLevel();

                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }

                                }
                            }

                            @Override
                            public void mouseEntered(MouseEvent arg0) {
                                // TODO Auto-generated method stub

                            }

                            @Override
                            public void mouseExited(MouseEvent arg0) {
                                // TODO Auto-generated method stub

                            }

                            @Override
                            public void mousePressed(MouseEvent arg0) {
                                // TODO Auto-generated method stub

                            }

                            @Override
                            public void mouseReleased(MouseEvent arg0) {
                                // TODO Auto-generated method stub

                            }

                        });

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void mouseEntered(MouseEvent arg0) {
                // TODO Auto-generated method stub

            }

            @Override
            public void mouseExited(MouseEvent arg0) {
                // TODO Auto-generated method stub

            }

            @Override
            public void mousePressed(MouseEvent arg0) {
                // TODO Auto-generated method stub

            }

            @Override
            public void mouseReleased(MouseEvent arg0) {
                // TODO Auto-generated method stub

            }
        });

        // Add Text Panel
        textPanel = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        // textPanel.setLayout(new ScrollPaneLayout());
        textPanel.getViewport().setBackground(new Color(10, 5, 20));
        textPanel.setBounds(60, 250, 720, 220);
        textPanel.setWheelScrollingEnabled(true);
        textPanel.getVerticalScrollBar().setUnitIncrement(50);
        textPanel.setViewportView(list);
        textPanel.setVisible(true);

        // Add Input Panel
        inputPanel = new JPanel();
        inputPanel.setBackground(new Color(10, 5, 20));
        inputPanel.setLayout(null);
        inputPanel.setBounds(70, 475, 700, 70);

        // Add insult label
        insult = new JTextPane();
        insult.setContentType("text/html");
        insult.setText(
                "<html><center style=\"font-family: Gadugi\"><font style=\"font-size: 20px\">Select a category from the menu below, select a book, then click 'Start Reading'</font></center></html>");
        insult.setBackground(baseColor);
        insult.setForeground(textColor);
        insult.setFont(new Font("MV Boli", Font.PLAIN, 22));
        insult.setEditable(false);
        insult.setBorder(null);
        insult.setFocusable(false);
        Dimension size = insult.getPreferredSize();
        insult.setBounds(40, 25, 750, 80);

        // Add word counter
        wordCounter = new JTextField("You type 0 word");
        wordCounter.setBackground(baseColor);
        wordCounter.setForeground(textColor);
        wordCounter.setFont(new Font("MV Boli", Font.BOLD, 22));
        wordCounter.setBorder(null);
        wordCounter.setEditable(false);
        wordCounter.setBounds(550, 200, 250, 50);
        wordCounter.setVisible(false);

        // Add character counter
        charCounter = new JTextField("You type 0 letter");
        charCounter.setBackground(baseColor);
        charCounter.setForeground(textColor);
        charCounter.setFont(new Font("MV Boli", Font.BOLD, 22));
        charCounter.setBorder(null);
        charCounter.setEditable(false);
        charCounter.setBounds(300, 150, 300, 50);
        charCounter.setVisible(false);

        // Add record counter
        recordCounter = new JTextField("Record: 0 letter");
        recordCounter.setBackground(baseColor);
        recordCounter.setForeground(textColor);
        recordCounter.setFont(new Font("MV Boli", Font.BOLD, 22));
        recordCounter.setBorder(null);
        recordCounter.setEditable(false);
        recordCounter.setBounds(20, 90, 300, 50);
        recordCounter.setVisible(false);

        // Add second counter
        secondCounter = new JTextField("0 second pass");
        secondCounter.setBackground(baseColor);
        secondCounter.setForeground(textColor);
        secondCounter.setFont(new Font("MV Boli", Font.BOLD, 22));
        secondCounter.setBorder(null);
        secondCounter.setEditable(false);
        secondCounter.setBounds(75, 200, 200, 50);
        secondCounter.setVisible(false);

        // Add WPM counter
        wpmCounter = new JTextField("0 WPM");
        wpmCounter.setBackground(baseColor);
        wpmCounter.setForeground(textColor);
        wpmCounter.setFont(new Font("MV Boli", Font.BOLD, 22));
        wpmCounter.setBorder(null);
        wpmCounter.setEditable(false);
        wpmCounter.setBounds(325, 200, 150, 50);
        wpmCounter.setVisible(false);

        // Add last mistake
        lastMistake = new JTextField("");
        lastMistake.setBackground(baseColor);
        lastMistake.setForeground(textColor);
        lastMistake.setFont(new Font("MV Boli", Font.BOLD, 20));
        lastMistake.setBorder(null);
        lastMistake.setEditable(false);
        lastMistake.setBounds(250, 80, 600, 70);
        lastMistake.setHorizontalAlignment(JTextField.CENTER);
        lastMistake.setVisible(false);

        // Add death counter
        deathCounter = new JTextField("You die 0 Time");
        deathCounter.setBackground(baseColor);
        deathCounter.setForeground(textColor);
        deathCounter.setFont(new Font("MV Boli", Font.BOLD, 25));
        deathCounter.setBorder(null);
        deathCounter.setEditable(false);
        deathCounter.setBounds(20, 150, 250, 50);
        deathCounter.setVisible(false);

        // Add win counter
        winCounter = new JTextField("You win 0 time");
        winCounter.setBackground(baseColor);
        winCounter.setForeground(textColor);
        winCounter.setFont(new Font("MV Boli", Font.BOLD, 25));
        winCounter.setBorder(null);
        winCounter.setEditable(false);
        winCounter.setBounds(600, 20, 150, 50);
        winCounter.setVisible(false);

        // Add text to be typed
        textToType = new JLabel("<html><font color=\"rgb(255, 220, 200)\"></font></html>");
        textToType.setLayout(null);
        textToType.setFont(new Font("Gadugi", Font.PLAIN, 22));
        textToType.setVerticalAlignment(JLabel.TOP);
        textToType.setHorizontalAlignment(JLabel.LEFT);
        textToType.setBounds(15, 0, 650, 210);

        // Add input text
        inputText = new JTextArea("/");
        inputText.setForeground(new Color(255, 255, 255));
        inputText.setBackground(new Color(10, 5, 20));
        inputText.setFont(new Font("Gadugi", Font.PLAIN, 20));
        size = inputText.getPreferredSize();
        inputText.setBounds(5, 15, 700, 100);
        inputText.select(1, 1);
        inputText.setEditable(false);
        inputText.setVisible(false);

        // Add a button
        startGameButton = new JButton();
        startGameButton.setText("Start Reading");
        size = startGameButton.getPreferredSize();
        startGameButton.setBounds(posX / 2 + 55, 120, size.width + 60, size.height + 10);
        startGameButton.setBackground(goldColor);
        startGameButton.setForeground(textColor);
        startGameButton.setFocusable(false);
        startGameButton.setFont(new Font("Gadugi", Font.PLAIN, 20));
        UIManager.put("Button.select", darkGoldColor);
        startGameButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                returnToMenuButton.setEnabled(true);
                returnToMenuButton.setVisible(true);
                inputText.setEditable(true);
                inMenu = false;
                insult.setVisible(false);

                // LOAD BOOK
            }

        });

        // Add return to menu button
        returnToMenuButton = new JButton();
        returnToMenuButton.setText("return to menu");
        size = returnToMenuButton.getPreferredSize();
        returnToMenuButton.setBounds(10, 10, size.width + 80, size.height);
        returnToMenuButton.setBackground(goldColor);
        returnToMenuButton.setForeground(textColor);
        returnToMenuButton.setFocusable(false);
        returnToMenuButton.setFont(new Font("MV Boli", Font.PLAIN, 20));
        returnToMenuButton.setVisible(false);
        returnToMenuButton.setEnabled(false);
        UIManager.put("Button.select", darkGoldColor);
        returnToMenuButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                returnToMenu();
            }

        });

        // Add to panel
        panel.add(insult);
        panel.add(startGameButton);
        panel.add(deathCounter);
        panel.add(wordCounter);
        panel.add(secondCounter);
        panel.add(wpmCounter);
        panel.add(lastMistake);
        panel.add(returnToMenuButton);
        panel.add(winCounter);
        panel.add(charCounter);
        panel.add(recordCounter);
        panel.setVisible(true);
        insult.setVisible(true);

        // Add to text panel
        textPanel.getViewport().add(textToType);
        textPanel.getViewport().add(list);
        inputPanel.add(inputText);

        // Display
        frame.add(panel);
        frame.getContentPane().add(textPanel);
        frame.add(inputPanel);
        frame.setVisible(true);

        // Start timer
        countTime();
        timer.start();

    }

    /**
     * Counts the number of words typed and adds them to the currentWord Arraylist
     * 
     * @param levelToCount
     * @throws FileNotFoundException
     */
    public static void countWords(String levelToCount) throws FileNotFoundException {
        String wordListText = levelToCount;
        Scanner scanList = new Scanner(wordListText);
        currentWord = new ArrayList<>();
        while (scanList.hasNext()) {
            currentWord.add(scanList.next());

        }
        scanList.close();

    }

    /**
     * Changes the text to be typed whenever the input text field is changed by the
     * player (i.e. when the player types).
     * 
     */
    public static DocumentListener textChanged = new DocumentListener() {
        @Override
        public void removeUpdate(DocumentEvent e) {
            try {
                textTyped = e.getDocument().getText(e.getDocument().getStartPosition().getOffset() + 1,
                        e.getDocument().getLength());
                charCount = 0;
            } catch (BadLocationException e1) {
                e1.printStackTrace();
            }
        }

        @Override
        public void changedUpdate(DocumentEvent e) {

        }

        @Override
        public void insertUpdate(DocumentEvent e) {
            try {
                if (!practiceOn && !inMenu && isTextReset) {

                    // Get the text that the user typed
                    textTyped = e.getDocument().getText(e.getDocument().getStartPosition().getOffset() + 1,
                            e.getDocument().getLength());

                    if (charCount == 0) {
                        wordCounter.setText("You type " + String.valueOf(wordCount) + " word");

                        numberOfSeconds = 0;
                        WPM = 0;
                        wpmCounter.setText(WPM + " WPM");
                        if (isWPMEnabled) {
                            wpmCounter.setVisible(true);
                        } else
                            wpmCounter.setVisible(false);

                        secondCounter.setText(numberOfSeconds + " second pass");
                        timerSet = true;

                        secondCounter.setVisible(true);
                        insult.setVisible(false);

                    }
                    charCount++;

                    // Determine if text matches
                    if (textTyped.charAt(charCount - 1) != textToDisplay.charAt(charCount - 1)) {
                        if (wordCount > 0) {
                            deathCount++;
                            deathCounter.setVisible(true);
                            deathCounter.setText("You die " + deathCount + " Time");
                        }

                        youLost();
                    }

                    else {

                        // Change to successful typed color
                        textToType.setText("<html><p style=\"width:540px\"><font color=\"#21fc37\">"
                                + textToDisplay.substring(0, charCount)
                                + "</font><font color=\"rgb(255, 220, 200)\">"
                                + "<span style=\"background-color: #FFFF00\"><font color=\"rgb:(0,0,0)\">"
                                + textToDisplay.substring(charCount, charCount + 1)
                                + "</font></span>"
                                + textToDisplay.substring(charCount + 1, textToDisplay.length())
                                + "</font></p></html>");

                        // Display char count
                        charCounter.setVisible(false); // @@@
                        charCounter.setText("You type " + charCount + " letter");
                        if (charCount > charRecord) {
                            charRecord = charCount;
                            recordCounter.setText("Record: " + charRecord + " letter");
                        }

                        deathCounter.setVisible(false);

                        // Reset if the char count and input count don't match
                        if (textTyped.length() - 1 > charCount) {
                            if (wordCount > 0) {
                                deathCount++;
                                deathCounter.setVisible(true);
                                deathCounter.setText("You die " + deathCount + " Time");
                            }

                            youLost();
                        }

                        // Count words
                        wordCharIndex++;
                        if (charCount == textToDisplay.length() - 1) {
                            resetTextMethod();
                        } else if (wordCharIndex == currentWord.get(wordCount).length() + 1) {
                            wordCount++;
                            wordCharIndex = 0;
                            wordCounter.setText("You type " + String.valueOf(wordCount) + " word");
                        }

                    }
                } else {
                    // wpmCounter.setVisible(false); @@@
                    wordCounter.setVisible(false);
                    secondCounter.setVisible(false);
                    deathCounter.setVisible(false);
                    insult.setVisible(false);
                    charCounter.setVisible(false);
                    recordCounter.setVisible(false);

                }

            } catch (BadLocationException e1) {
                e1.printStackTrace();
            } catch (FileNotFoundException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        }

    };

    /**
     * Code to run when the player dies, calls a reset of the game
     * 
     * @throws FileNotFoundException
     */
    public static void youLost() throws FileNotFoundException {
        insult.setText("<html><div style=\"size: " + insult.getWidth()
                + "\"><center style=\"font-family: Gadugi\"><font style=\"font-size: 20px\">"
                + "You die" + "</font></center></div></html>");
        insult.setVisible(true);
        insult.setBounds(40, 50, 750, 110);

        // Change UI if death count is too high
        if (deathCount >= 1000) {
            frame.setTitle(realApplicationTitle);

        }

        // // You should have typed
        // if (charCount > 20) {
        // if (textToDisplay.length() >= charCount + 15) {
        // lastMistake.setText(textToDisplay.substring(charCount - 20, charCount + 15));
        // } else {
        // lastMistake.setText(textToDisplay.substring(charCount - 20,
        // textToDisplay.length()));
        // }
        // } else {
        // lastMistake.setText(textToDisplay.substring(0, charCount + 20));
        // }

        // lastMistake.setVisible(false); // @@@

        wpmCounter.setForeground(textColor);
        secondCounter.setText(numberOfSeconds + " second passed");
        timerSet = false;

        // LOAD BOOK;
        loadLevel();
    }

    /**
     * Code to run when the player wins. Calls a load of the next level.
     * 
     * @throws FileNotFoundException
     */
    public static void youWin() throws FileNotFoundException {
        insult.setText("<html><div style=\"size: " + insult.getWidth()
                + "\"><center style=\"font-family: Gadugi\"><font style=\"font-size: 20px\">"
                + "You win" + "</font></center></div></html>");
        insult.setVisible(true);
        insult.setBounds(40, 50, 750, 110);

        secondCounter.setText(numberOfSeconds + " second passed");
        timerSet = false;

        winCount++;
        // winCounter.setVisible(true);
        winCounter.setText("You win " + winCount + " Time");

        // NEXT SECTION
        sectionIndex++;
        // LOAD NEXT SECTION
        loadLevel();

    }

    /**
     * Clears the input text field
     */
    public static void resetTextMethod() {
        isTextReset = false;

        Runnable resetText = new Runnable() {
            @Override
            public void run() {
                inputText.setText("/");
                isTextReset = true;

                try {
                    youWin();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

            }
        };

        SwingUtilities.invokeLater(resetText);
    }

    /**
     * Loads the next level
     * 
     */
    public static void loadLevel() {
        ArrayList<String> bookSections = new ArrayList<>();

        // Read list
        try {
            Elements elementsHtml = bookDocument.getElementsByTag("p");
            for (Element section : elementsHtml) {
                if (!section.text().equals("")) {
                    if (section.text().length() > maxLength) { // ADD CASES FOR LONGER THAN 2X MAX LENGTH
                        bookSections.add(section.text().substring(0, maxLength - 1));
                        bookSections.add(section.text().substring(maxLength, section.text().length()));
                    } else {
                        bookSections.add(section.text());
                    }
                }
            }
            bookText = bookSections.get(sectionIndex);
            Scanner scan = new Scanner(bookText);

            textToDisplay = "";
            while (scan.hasNextLine()) {
                textToDisplay = textToDisplay + scan.nextLine() + "\n";
            }
            textToType.setText("<html><p style=\"width:545px\">" + textToDisplay + "</p></html>");

            scan.close();

        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

        // Initialize UI
        inputText.setVisible(true);

        inputText.setEditable(true);
        startGameButton.setVisible(false);
        startGameButton.setEnabled(false);

        // LOAD LEVEL DATA

        // Check for changed text
        if (!docListenerCreated) {
            inputText.getDocument().addDocumentListener(textChanged);
            docListenerCreated = true;
        }

        // Reset All Counters
        charCount = 0;
        wordCount = 0;
        wordCharIndex = 0;
        textTyped = "";
        try {
            countWords(bookText);
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        }

    }

    // /**
    // * Loads the next level
    // *
    // */
    // public static void loadLevel() {
    // String textToBeTyped = "";

    // // Read list
    // try {
    // Elements elementsHtml = bookDocument.getElementsByTag("p");
    // for (Element section : elementsHtml) {
    // if (!section.text().equals("")) {
    // sectionIndex++;
    // }
    // }
    // bookText = elementsHtml.eachText().get(sectionIndex);
    // Scanner scan = new Scanner(textToBeTyped);

    // textToDisplay = "";
    // while (scan.hasNextLine()) {
    // textToDisplay = textToDisplay + scan.nextLine() + "\n";
    // }
    // textToType.setText("<html><p style=\"width:545px\">" + textToDisplay +
    // "</p></html>");

    // scan.close();

    // } catch (Exception e) {
    // System.err.println(e.getMessage());
    // }

    // // Initialize UI
    // inputText.setVisible(true);

    // inputText.setEditable(true);
    // startGameButton.setVisible(false);
    // startGameButton.setEnabled(false);

    // // LOAD LEVEL DATA

    // // Check for changed text
    // if (!docListenerCreated) {
    // inputText.getDocument().addDocumentListener(textChanged);
    // docListenerCreated = true;
    // }

    // // Reset All Counters
    // charCount = 0;
    // wordCount = 0;
    // wordCharIndex = 0;
    // textTyped = "";
    // try {
    // countWords(bookText);
    // } catch (FileNotFoundException e1) {
    // e1.printStackTrace();
    // }

    // }

    /**
     * Counts the time, WPM, and ensures the player is typing fast enough
     * 
     */
    public static void countTime() {

        timer = new Timer(1000, new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                numberOfSeconds++;
                if (timerSet) {
                    secondCounter.setText(numberOfSeconds + " second pass");

                    WPM = (wordCount * 60) / (numberOfSeconds);
                    wpmCounter.setText(WPM + " WPM");

                    if (wpmThreshold != 0) {
                        // Under WPM Threshold
                        if (WPM < wpmThreshold && WPM != 0) {
                            if (!thresholdTimerSet) {
                                secondsPastThreshold = numberOfSeconds;
                                wpmCounter.setForeground(new Color(255, 0, 0));
                                thresholdTimerSet = true;
                            }
                            if ((numberOfSeconds - secondsPastThreshold) >= secondsTillDeath) {
                                try {
                                    wpmCounter.setForeground(textColor);
                                    youLost();
                                } catch (FileNotFoundException e1) {
                                    e1.printStackTrace();
                                }
                            }
                        } else {
                            wpmCounter.setForeground(new Color(20, 255, 0));
                            thresholdTimerSet = false;
                        }
                    }
                } else {
                    wpmCounter.setForeground(textColor);
                }
            }
        });
    }

    /**
     * returnToMenu() clears UI elements and returns to the main menu screen
     * 
     */
    public static void returnToMenu() {

        inMenu = true;

        docListenerCreated = false;
        timer.stop();

        frame.getContentPane().removeAll();
        frame.repaint();

        mainMenu.welcomePlayer();
        mainMenu.displayUIElements();
    }

}
