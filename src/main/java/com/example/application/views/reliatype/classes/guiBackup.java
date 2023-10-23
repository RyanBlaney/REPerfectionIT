/*
 * package src.classes;
 * 
 * import java.awt.Color;
 * import java.awt.Dimension;
 * import java.awt.Font;
 * import java.awt.event.ActionEvent;
 * import java.awt.event.ActionListener;
 * 
 * import java.io.File;
 * import java.io.FileNotFoundException;
 * import java.io.IOException;
 * import java.util.ArrayList;
 * import java.util.Scanner;
 * 
 * import javax.swing.*;
 * import javax.swing.event.DocumentEvent;
 * import javax.swing.event.DocumentListener;
 * import javax.swing.text.BadLocationException;
 * 
 * public class guiBackup {
 * 
 * public static String applicationTitle = "Typing For Help";
 * public static String realApplicationTitle = "Typing From Hell";
 * public static int resolutionW = 850;
 * public static int resolutionH = 600;
 * public static int posX = 575;
 * public static int posY = 200;
 * 
 * public static File wordList;
 * public static String textToDisplay = "";
 * public static String wordToType = "";
 * public static char charToType = ' ';
 * public static int charsInWord = 0;
 * 
 * public static int numOfWordsToGenerate = 10;
 * public static int wpmThreshold = 0;
 * public static int secondsTillDeath = 3;
 * public static Boolean thresholdTimerSet = false;
 * 
 * public static String textTyped = "";
 * public static int charCount = 0;
 * public static int wordCount = 0;
 * public static int wordCharIndex = 0; // How many chars till a full word is
 * typed
 * public static ArrayList<String> currentWord; // The word index to count words
 * public static int deathCount = 0;
 * public static int winCount = 0;
 * public static int charRecord = 0;
 * 
 * public static int numberOfSeconds = 0;
 * public static int secondsPastThreshold = 0;
 * public static int WPM = 0;
 * public static Boolean timerSet = false;
 * public static Timer timer;
 * 
 * public static Boolean docListenerCreated = false; // Prevents infinite
 * document listeners to be created
 * public static Boolean inMenu = true;
 * 
 * public static JFrame frame;
 * public static JTextField insult;
 * public static JLabel textToType;
 * public static JButton startGameButton;
 * public static JTextArea inputText;
 * public static JTextField deathCounter;
 * public static JTextField wordCounter;
 * public static JTextField charCounter;
 * public static JTextField recordCounter;
 * public static JTextField secondCounter;
 * public static JTextField wpmCounter;
 * public static JTextField lastMistake;
 * public static JTextField winCounter;
 * public static JScrollPane textPanel;
 * public static JPanel inputPanel;
 * public static JButton practiceButton;
 * public static JButton returnToMenuButton;
 * public static JTextArea inputWordCount;
 * public static JTextArea inputWPMThreshold;
 * public static JTextArea inputSecTillDeath;
 * 
 * public static Boolean practiceOn = false;
 * 
 * public static void main(String[] args) {
 * frame = new JFrame();
 * 
 * Color baseColor = new Color(230, 225, 255);
 * Color textColor = new Color(30, 0, 20);
 * 
 * // Create the frame
 * frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // *EXIT OUT OF
 * APPLICATION WHEN CLOSED
 * frame.setResizable(false); // Prevent dragging to change resolution
 * frame.setTitle(applicationTitle);
 * frame.setSize(resolutionW, resolutionH);
 * frame.getContentPane().setBackground(baseColor);
 * frame.setLocation(posX, posY);
 * frame.setLayout(null);
 * 
 * // Add logo
 * ImageIcon icon = new ImageIcon("src/files/LOGO.png");
 * frame.setIconImage(icon.getImage());
 * 
 * // Add Panel
 * JPanel panel = new JPanel();
 * panel.setBackground(baseColor);
 * panel.setLayout(null);
 * panel.setBounds(0, 0, 850, 250);
 * 
 * // Add Text Panel
 * textPanel = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
 * JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
 * textPanel.setLayout(new ScrollPaneLayout());
 * textPanel.getViewport().setBackground(new Color(10, 5, 20));
 * textPanel.setBounds(60, 250, 720, 220);
 * textPanel.setWheelScrollingEnabled(true);
 * textPanel.getVerticalScrollBar().setUnitIncrement(50);
 * textPanel.setVisible(true);
 * 
 * // Add Input Panel
 * inputPanel = new JPanel();
 * inputPanel.setBackground(new Color(10, 5, 20));
 * inputPanel.setLayout(null);
 * inputPanel.setBounds(70, 475, 700, 70);
 * 
 * // Add insult label
 * insult = new JTextField("Ready to learn how to type?");
 * insult.setBackground(baseColor);
 * insult.setForeground(textColor);
 * insult.setFont(new Font("MV Boli", Font.PLAIN, 22));
 * insult.setEditable(false);
 * insult.setBorder(null);
 * Dimension size = insult.getPreferredSize();
 * insult.setBounds(posX / 2, 60, size.width + 20, size.height);
 * 
 * // Add word counter
 * wordCounter = new JTextField("You type 0 word");
 * wordCounter.setBackground(baseColor);
 * wordCounter.setForeground(textColor);
 * wordCounter.setFont(new Font("MV Boli", Font.BOLD, 22));
 * wordCounter.setBorder(null);
 * wordCounter.setEditable(false);
 * wordCounter.setBounds(550, 200, 250, 50);
 * wordCounter.setVisible(false);
 * 
 * // Add character counter
 * charCounter = new JTextField("You type 0 letter");
 * charCounter.setBackground(baseColor);
 * charCounter.setForeground(textColor);
 * charCounter.setFont(new Font("MV Boli", Font.BOLD, 22));
 * charCounter.setBorder(null);
 * charCounter.setEditable(false);
 * charCounter.setBounds(300, 150, 300, 50);
 * charCounter.setVisible(false);
 * 
 * // Add record counter
 * recordCounter = new JTextField("Record: 0 letter");
 * recordCounter.setBackground(baseColor);
 * recordCounter.setForeground(textColor);
 * recordCounter.setFont(new Font("MV Boli", Font.BOLD, 22));
 * recordCounter.setBorder(null);
 * recordCounter.setEditable(false);
 * recordCounter.setBounds(20, 90, 300, 50);
 * recordCounter.setVisible(false);
 * 
 * // Add second counter
 * secondCounter = new JTextField("0 second pass");
 * secondCounter.setBackground(baseColor);
 * secondCounter.setForeground(textColor);
 * secondCounter.setFont(new Font("MV Boli", Font.BOLD, 22));
 * secondCounter.setBorder(null);
 * secondCounter.setEditable(false);
 * secondCounter.setBounds(75, 200, 200, 50);
 * secondCounter.setVisible(false);
 * 
 * // Add WPM counter
 * wpmCounter = new JTextField("0 WPM");
 * wpmCounter.setBackground(baseColor);
 * wpmCounter.setForeground(textColor);
 * wpmCounter.setFont(new Font("MV Boli", Font.BOLD, 22));
 * wpmCounter.setBorder(null);
 * wpmCounter.setEditable(false);
 * wpmCounter.setBounds(325, 200, 150, 50);
 * wpmCounter.setVisible(false);
 * 
 * // Add last mistake
 * lastMistake = new JTextField("");
 * lastMistake.setBackground(baseColor);
 * lastMistake.setForeground(textColor);
 * lastMistake.setFont(new Font("MV Boli", Font.BOLD, 20));
 * lastMistake.setBorder(null);
 * lastMistake.setEditable(false);
 * lastMistake.setBounds(250, 80, 600, 70);
 * lastMistake.setHorizontalAlignment(JTextField.CENTER);
 * lastMistake.setVisible(false);
 * 
 * // Add death counter
 * deathCounter = new JTextField("You die 0 Time");
 * deathCounter.setBackground(baseColor);
 * deathCounter.setForeground(textColor);
 * deathCounter.setFont(new Font("MV Boli", Font.BOLD, 25));
 * deathCounter.setBorder(null);
 * deathCounter.setEditable(false);
 * deathCounter.setBounds(20, 150, 250, 50);
 * deathCounter.setVisible(false);
 * 
 * // Add win counter
 * winCounter = new JTextField("You win 0 time");
 * winCounter.setBackground(baseColor);
 * winCounter.setForeground(textColor);
 * winCounter.setFont(new Font("MV Boli", Font.BOLD, 25));
 * winCounter.setBorder(null);
 * winCounter.setEditable(false);
 * winCounter.setBounds(600, 20, 150, 50);
 * winCounter.setVisible(false);
 * 
 * // Add text to be typed
 * textToType = new
 * JLabel("<html><font color=\"rgb(255, 220, 200)\"></font></html>");
 * textToType.setLayout(null);
 * textToType.setFont(new Font("Gadugi", Font.PLAIN, 22));
 * textToType.setVerticalAlignment(JLabel.TOP);
 * textToType.setHorizontalAlignment(JLabel.LEFT);
 * textToType.setBounds(15, 0, 650, 210);
 * 
 * // Add input text
 * inputText = new JTextArea("/");
 * inputText.setForeground(new Color(255, 255, 255));
 * inputText.setBackground(new Color(10, 5, 20));
 * inputText.setFont(new Font("Gadugi", Font.PLAIN, 20));
 * size = inputText.getPreferredSize();
 * inputText.setBounds(5, 15, 700, 100);
 * inputText.select(1, 1);
 * inputText.setEditable(false);
 * inputText.setVisible(false);
 * 
 * // Add field to input word count to generate
 * inputWordCount = new JTextArea("how many word: 50");
 * inputWordCount.setForeground(textColor);
 * inputWordCount.setBackground(baseColor);
 * inputWordCount.setFont(new Font("HV Boli", Font.PLAIN, 20));
 * size = inputWordCount.getPreferredSize();
 * inputWordCount.setBounds(5, 5, 200, 25);
 * inputWordCount.select(16, 16);
 * inputWordCount.setEditable(true);
 * inputWordCount.setVisible(true);
 * 
 * // Add field to input wpm threshold to beat
 * inputWPMThreshold = new JTextArea("wpm get faster than: 20");
 * inputWPMThreshold.setForeground(textColor);
 * inputWPMThreshold.setBackground(baseColor);
 * inputWPMThreshold.setFont(new Font("HV Boli", Font.PLAIN, 20));
 * size = inputWPMThreshold.getPreferredSize();
 * inputWPMThreshold.setBounds(5, 35, 250, 25);
 * inputWPMThreshold.select(15, 15);
 * inputWPMThreshold.setEditable(true);
 * inputWPMThreshold.setVisible(true);
 * 
 * // Add field to input seconds till death
 * inputSecTillDeath = new JTextArea("second till do death: 3");
 * inputSecTillDeath.setForeground(textColor);
 * inputSecTillDeath.setBackground(baseColor);
 * inputSecTillDeath.setFont(new Font("HV Boli", Font.PLAIN, 20));
 * size = inputSecTillDeath.getPreferredSize();
 * inputSecTillDeath.setBounds(5, 65, 250, 25);
 * inputSecTillDeath.setEditable(true);
 * inputSecTillDeath.setVisible(true);
 * 
 * // Add a button
 * startGameButton = new JButton();
 * startGameButton.setText("Yes I'm Ready!");
 * size = startGameButton.getPreferredSize();
 * startGameButton.setBounds(posX / 2 + 55, 100, size.width + 60, size.height);
 * startGameButton.setBackground(new Color(255, 200, 100));
 * startGameButton.setForeground(new Color(10, 10, 0));
 * startGameButton.setFocusable(false);
 * startGameButton.setFont(new Font("MV Boli", Font.PLAIN, 20));
 * startGameButton.addActionListener(new ActionListener() {
 * 
 * @Override
 * public void actionPerformed(java.awt.event.ActionEvent e) {
 * returnToMenuButton.setEnabled(true);
 * returnToMenuButton.setVisible(true);
 * inputText.setEditable(true);
 * inMenu = false;
 * 
 * inputWordCount.setVisible(false);
 * inputWordCount.setEditable(false);
 * numOfWordsToGenerate = Integer
 * .valueOf(inputWordCount.getText().substring(15,
 * inputWordCount.getText().length()));
 * inputWPMThreshold.setVisible(false);
 * inputWPMThreshold.setEditable(false);
 * wpmThreshold = Integer
 * .valueOf(inputWPMThreshold.getText().substring(21,
 * inputWPMThreshold.getText().length()));
 * inputSecTillDeath.setVisible(false);
 * inputSecTillDeath.setEditable(false);
 * secondsTillDeath = Integer
 * .valueOf(inputSecTillDeath.getText().substring(22,
 * inputSecTillDeath.getText().length()));
 * resetGame();
 * insult.setVisible(false);
 * 
 * }
 * 
 * });
 * 
 * // Add practice button
 * practiceButton = new JButton();
 * practiceButton.setText("practice mode");
 * size = practiceButton.getPreferredSize();
 * practiceButton.setBounds(600, 160, size.width + 60, size.height);
 * practiceButton.setBackground(new Color(255, 200, 0));
 * practiceButton.setForeground(new Color(10, 10, 0));
 * practiceButton.setFocusable(false);
 * practiceButton.setFont(new Font("MV Boli", Font.PLAIN, 20));
 * practiceButton.setVisible(false);
 * practiceButton.setEnabled(false);
 * practiceButton.addActionListener(new ActionListener() {
 * 
 * @Override
 * public void actionPerformed(java.awt.event.ActionEvent e) {
 * if (!practiceOn)
 * practiceOn = true;
 * else
 * practiceOn = false;
 * }
 * 
 * });
 * 
 * // Add return to menu button
 * returnToMenuButton = new JButton();
 * returnToMenuButton.setText("return to menu");
 * size = returnToMenuButton.getPreferredSize();
 * returnToMenuButton.setBounds(10, 10, size.width + 60, size.height);
 * returnToMenuButton.setBackground(new Color(255, 200, 0));
 * returnToMenuButton.setForeground(new Color(10, 10, 0));
 * returnToMenuButton.setFocusable(false);
 * returnToMenuButton.setFont(new Font("MV Boli", Font.PLAIN, 20));
 * returnToMenuButton.setVisible(false);
 * returnToMenuButton.setEnabled(false);
 * returnToMenuButton.addActionListener(new ActionListener() {
 * 
 * @Override
 * public void actionPerformed(java.awt.event.ActionEvent e) {
 * returnToMenu();
 * }
 * 
 * });
 * 
 * // Add to panel
 * panel.add(insult);
 * panel.add(startGameButton);
 * panel.add(deathCounter);
 * panel.add(wordCounter);
 * panel.add(secondCounter);
 * panel.add(wpmCounter);
 * panel.add(lastMistake);
 * panel.add(practiceButton);
 * panel.add(returnToMenuButton);
 * panel.add(winCounter);
 * panel.add(charCounter);
 * panel.add(recordCounter);
 * panel.add(inputWordCount);
 * panel.add(inputWPMThreshold);
 * panel.add(inputSecTillDeath);
 * panel.setVisible(true);
 * insult.setVisible(true);
 * 
 * // Add to text panel
 * textPanel.getViewport().add(textToType);
 * inputPanel.add(inputText);
 * 
 * // Display
 * frame.add(panel);
 * frame.getContentPane().add(textPanel);
 * frame.add(inputPanel);
 * frame.setVisible(true);
 * 
 * // Start timer
 * countTime();
 * timer.start();
 * 
 * }
 * 
 * public static void countWords() throws FileNotFoundException {
 * File wordListFile = new File("src/files/new_word_list.txt");
 * Scanner scanList = new Scanner(wordListFile);
 * currentWord = new ArrayList<>();
 * while (scanList.hasNext()) {
 * currentWord.add(scanList.next());
 * 
 * }
 * scanList.close();
 * 
 * }
 * 
 * public static DocumentListener textChanged = new DocumentListener() {
 * 
 * @Override
 * public void removeUpdate(DocumentEvent e) {
 * try {
 * textTyped =
 * e.getDocument().getText(e.getDocument().getStartPosition().getOffset() + 1,
 * e.getDocument().getLength());
 * charCount = 0;
 * } catch (BadLocationException e1) {
 * e1.printStackTrace();
 * }
 * }
 * 
 * @Override
 * public void changedUpdate(DocumentEvent e) {
 * 
 * }
 * 
 * @Override
 * public void insertUpdate(DocumentEvent e) {
 * try {
 * if (!practiceOn && !inMenu) {
 * // Get the text that the user typed
 * textTyped =
 * e.getDocument().getText(e.getDocument().getStartPosition().getOffset() + 1,
 * e.getDocument().getLength());
 * 
 * if (charCount == 0) {
 * wordCounter.setText("You type " + String.valueOf(wordCount) + " word");
 * 
 * numberOfSeconds = 0;
 * WPM = 0;
 * wpmCounter.setText(WPM + " WPM");
 * secondCounter.setText(numberOfSeconds + " second pass");
 * timerSet = true;
 * 
 * secondCounter.setVisible(true);
 * insult.setVisible(false);
 * deathCounter.setVisible(false);
 * wordCounter.setVisible(true);
 * wpmCounter.setVisible(true);
 * recordCounter.setVisible(true);
 * 
 * lastMistake.setVisible(false);
 * practiceButton.setVisible(false);
 * practiceButton.setEnabled(false);
 * 
 * }
 * charCount++;
 * 
 * // Determine if text matches
 * if (textTyped.charAt(charCount - 1) != textToDisplay.charAt(charCount - 1))
 * youLost();
 * 
 * else {
 * 
 * // Change to successful typed color
 * textToType.setText("<html><p style=\"width:550px\"><font color=\"#21fc37\">"
 * + textToDisplay.substring(0, charCount) +
 * "</font><font color=\"rgb(255, 220, 200)\">"
 * + textToDisplay.substring(charCount, textToDisplay.length())
 * + "</font></p></html>");
 * 
 * // Display char count
 * charCounter.setVisible(true);
 * charCounter.setText("You type " + charCount + " letter");
 * if (charCount > charRecord) {
 * charRecord = charCount;
 * recordCounter.setText("Record: " + charRecord + " letter");
 * }
 * 
 * // Count words
 * wordCharIndex++;
 * if (charCount == textToDisplay.length() - 1) {
 * youWin();
 * } else if (wordCharIndex == currentWord.get(wordCount).length() + 1) {
 * wordCount++;
 * wordCharIndex = 0;
 * wordCounter.setText("You type " + String.valueOf(wordCount) + " word");
 * }
 * 
 * }
 * } else {
 * wpmCounter.setVisible(false);
 * wordCounter.setVisible(false);
 * secondCounter.setVisible(false);
 * deathCounter.setVisible(false);
 * insult.setVisible(false);
 * charCounter.setVisible(false);
 * recordCounter.setVisible(false);
 * 
 * }
 * 
 * } catch (BadLocationException e1) {
 * e1.printStackTrace();
 * } catch (FileNotFoundException e1) {
 * // TODO Auto-generated catch block
 * e1.printStackTrace();
 * }
 * }
 * 
 * };
 * 
 * public static void youLost() throws FileNotFoundException {
 * insult.setText(functionality.humiliationStation());
 * insult.setHorizontalAlignment(JTextField.CENTER);
 * insult.setBounds(insult.getHorizontalAlignment(), 0, 800, 100);
 * insult.setVisible(true);
 * 
 * // Change UI if death count is too high
 * if (deathCount >= 10) {
 * frame.setTitle(realApplicationTitle);
 * 
 * }
 * 
 * // You should have typed
 * if (charCount > 20) {
 * if (textToDisplay.length() >= charCount + 15) {
 * lastMistake.setText(textToDisplay.substring(charCount - 20, charCount + 15));
 * } else {
 * lastMistake.setText(textToDisplay.substring(charCount - 20,
 * textToDisplay.length()));
 * }
 * } else {
 * lastMistake.setText(textToDisplay.substring(0, charCount + 20));
 * }
 * 
 * lastMistake.setVisible(true);
 * practiceButton.setVisible(true);
 * practiceButton.setEnabled(true);
 * 
 * secondCounter.setText(numberOfSeconds + " second passed");
 * timerSet = false;
 * 
 * deathCount++;
 * deathCounter.setVisible(true);
 * deathCounter.setText("You die " + deathCount + " Time");
 * 
 * resetGame();
 * 
 * }
 * 
 * public static void youWin() throws FileNotFoundException {
 * insult.setText(functionality.humiliationStation());
 * insult.setHorizontalAlignment(JTextField.CENTER);
 * insult.setBounds(insult.getHorizontalAlignment(), 0, 800, 100);
 * insult.setVisible(false);
 * 
 * secondCounter.setText(numberOfSeconds + " second passed");
 * timerSet = false;
 * 
 * winCount++;
 * winCounter.setVisible(true);
 * winCounter.setText("You win " + winCount + " Time");
 * 
 * resetGame();
 * 
 * }
 * 
 * public static void resetGame() {
 * if (!inMenu) { // @@@
 * // Create new word list
 * try {
 * functionality.main(null);
 * functionality.createGameText(numOfWordsToGenerate);
 * } catch (IOException e2) {
 * e2.printStackTrace();
 * }
 * 
 * wordList = new File("src/files/new_word_list.txt");
 * try {
 * Scanner scan = new Scanner(wordList);
 * 
 * textToDisplay = "";
 * 
 * while (scan.hasNextLine()) {
 * textToDisplay = textToDisplay + scan.nextLine() + "\n";
 * }
 * textToType.setText("<html><p style=\"width:545px\">" + textToDisplay +
 * "</p></html>");
 * 
 * scan.close();
 * 
 * } catch (FileNotFoundException e1) {
 * e1.printStackTrace();
 * }
 * 
 * inputText.setVisible(true);
 * 
 * inputText.setEditable(true);
 * startGameButton.setVisible(false);
 * startGameButton.setEnabled(false);
 * 
 * if (!docListenerCreated) {
 * inputText.getDocument().addDocumentListener(textChanged);
 * docListenerCreated = true;
 * }
 * 
 * // Reset All Counters
 * charCount = 0;
 * wordCount = 0;
 * wordCharIndex = 0;
 * textTyped = "";
 * try {
 * countWords();
 * } catch (FileNotFoundException e1) {
 * e1.printStackTrace();
 * }
 * }
 * }
 * 
 * public static void countTime() {
 * 
 * timer = new Timer(1000, new ActionListener() {
 * 
 * @Override
 * public void actionPerformed(ActionEvent e) {
 * numberOfSeconds++;
 * if (timerSet) {
 * secondCounter.setText(numberOfSeconds + " second pass");
 * 
 * WPM = (wordCount * 60) / (numberOfSeconds);
 * wpmCounter.setText(WPM + " WPM");
 * 
 * // Under WPM Threshold
 * if (WPM < wpmThreshold && WPM != 0) {
 * if (!thresholdTimerSet) {
 * secondsPastThreshold = numberOfSeconds;
 * thresholdTimerSet = true;
 * }
 * if ((numberOfSeconds - secondsPastThreshold) >= secondsTillDeath) {
 * try {
 * youLost();
 * } catch (FileNotFoundException e1) {
 * e1.printStackTrace();
 * }
 * }
 * } else
 * thresholdTimerSet = false;
 * }
 * }
 * });
 * }
 * 
 * public static void returnToMenu() {
 * inMenu = true;
 * 
 * // Disable buttons and labels
 * returnToMenuButton.setVisible(false);
 * returnToMenuButton.setEnabled(false);
 * 
 * practiceButton.setEnabled(false);
 * practiceButton.setVisible(false);
 * practiceOn = false;
 * 
 * secondCounter.setVisible(false);
 * deathCounter.setVisible(false);
 * wordCounter.setVisible(false);
 * wpmCounter.setVisible(false);
 * recordCounter.setVisible(false);
 * 
 * lastMistake.setVisible(false);
 * practiceButton.setVisible(false);
 * practiceButton.setEnabled(false);
 * 
 * inputText.setEditable(false);
 * textToType.setText("");
 * inputText.setText("/");
 * 
 * // Enable menu buttons and text
 * startGameButton.setEnabled(true);
 * startGameButton.setVisible(true);
 * 
 * inputWordCount.setVisible(true);
 * inputWordCount.setEditable(true);
 * inputWPMThreshold.setVisible(true);
 * inputWPMThreshold.setEditable(true);
 * inputSecTillDeath.setVisible(true);
 * inputSecTillDeath.setEditable(true);
 * insult.setText("Skill issue?");
 * insult.setVisible(true);
 * 
 * }
 * 
 * }
 */
