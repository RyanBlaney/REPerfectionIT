/**
 * mainMenu is the main driver class that runs the program. It needs to be in the classes 
 * folder with the other dependent classes. This class initializes the main menu and 
 * runs the GUI class to start the game
 * 
 * FEATURES: 1, 
 *
 * @author Ryan Blaney
 * @version 12/3/2022
 */

package com.example.application.views.reliatype.classes;

// Import Swing for the UI
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.MouseInputListener;

// Import AWT for styling and events
import java.awt.Color;
import java.awt.Font;
import java.awt.image.BufferedImage;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;

// Imports for File IO
import java.io.File;
import java.io.IOException;

public class mainMenu {
    // Application attributes
    public static String applicationTitle = "Typing To Hero";
    public static String realApplicationTitle = "Typing To Hell";
    public static Color baseColor = new Color(190, 185, 255);
    public static Color textColor = new Color(30, 0, 20);
    public static Color darkGoldColor = new Color(179, 129, 5);
    public static Color goldColor = new Color(255, 200, 100);
    public static Color darkBlueColor = new Color(25, 25, 112);
    public static int resolutionW = 850;
    public static int resolutionH = 600;
    public static int posX = 575;
    public static int posY = 200;

    // Frames and panels
    public static JFrame frame;
    public static JPanel welcomePanel;
    public static JPanel menuPanel;

    // Welcome Player
    public static JTextPane welcomeText;
    public static JTextPane infoText;
    public static BufferedImage heroIcon;
    public static JLabel heroIconDisplay;
    public static BufferedImage bottomTrimImage;
    public static JLabel bottomTrimImageDisplay;

    // Menu Buttons
    public static JButton trainingButton;
    public static JButton campaignButton;
    public static JButton readingModeButton;

    /**
     * The main method to start the application and create the interface
     * 
     * @param args
     */
    public static void main(String[] args) {
        initializeMenu();
    }

    /**
     * createFrame() creates the frame object to place the UI elements
     * 
     */
    public static void createFrame() {
        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // *EXIT OUT OF APPLICATION WHEN CLOSED
        frame.setResizable(false); // Prevent dragging to change resolution
        frame.setTitle(applicationTitle);
        frame.setSize(resolutionW, resolutionH);
        frame.getContentPane().setBackground(baseColor);
        frame.setLocation(posX, posY);
        frame.setLayout(null);
    }

    public static void initializeMenu() {
        createFrame();

        // Add logo
        ImageIcon icon = new ImageIcon("src/files/LOGO.png");
        frame.setIconImage(icon.getImage());

        welcomePlayer();

        displayUIElements();
    }

    /**
     * displayUIElements() adds each panel to the frame and adds all UI elements to
     * their respective panel. Don't forget to add each new UI element to a panel
     * for it to display
     * 
     */
    public static void displayUIElements() {
        // Add UI to panels
        welcomePanel.add(welcomeText);
        welcomePanel.add(infoText);
        menuPanel.add(heroIconDisplay);
        menuPanel.add(campaignButton);
        menuPanel.add(trainingButton);
        menuPanel.add(readingModeButton);
        menuPanel.add(bottomTrimImageDisplay);

        // Add panels
        frame.add(welcomePanel);
        frame.add(menuPanel);
        frame.setVisible(true);
    }

    /**
     * welcomePlayer() displays the UI to introduce the player to the suffering that
     * awaits them as subtly as possible. Creates all the UI elements
     * 
     */
    public static void welcomePlayer() {
        // Welcome Panel
        welcomePanel = new JPanel();
        welcomePanel.setBackground(darkGoldColor);
        welcomePanel.setLayout(null);
        Border border = BorderFactory.createBevelBorder(0, Color.black, darkGoldColor);
        welcomePanel.setBorder(border);
        welcomePanel.setBounds(0, 0, resolutionW, 143);

        // Welcome Text
        welcomeText = new JTextPane();
        welcomeText.setContentType("text/html");
        welcomeText.setLayout(null);
        welcomeText.setText(
                "<html><center style=\"font-family: Gadugi\"><font style=\"font-size: 21px\">Welcome to <font style=\"color: #EEE8AA\"> \"Typing to Hero!\" </font> You will go far if you let yourself.</font></center></html>");
        welcomeText.setFont(new Font("Gadugi", Font.PLAIN, 23));
        welcomeText.setBackground(darkGoldColor);
        welcomeText.setForeground(textColor);
        welcomeText.setBounds(20, 10, 800, 45);

        // Info Text
        infoText = new JTextPane();
        infoText.setContentType("text/html");
        infoText.setLayout(null);
        infoText.setText(
                "<html><center style=\"font-family: Gadugi\"><font style=\"font-size: 21px\">Today you will either <font style=\"color: #4169E1\"> conquer </font> the most dangerous endeavours or <font style=\"color: #8B0000\"> die </font> trying. Ready?</font></center></html>");
        infoText.setFont(new Font("Gadugi", Font.PLAIN, 23));
        infoText.setBackground(darkGoldColor);
        infoText.setForeground(textColor);
        infoText.setEditable(false);
        infoText.setBorder(null);
        infoText.setDragEnabled(false);
        infoText.setBounds(105, 50, 650, 82);

        // Create menu panel
        menuPanel = new JPanel();
        menuPanel.setBackground(mainMenu.baseColor);
        menuPanel.setLayout(null);
        menuPanel.setBorder(null);
        menuPanel.setBounds(0, 143, resolutionW, resolutionH - 143);

        // Display images
        try {
            heroIcon = ImageIO.read(new File("src/files/Heroic_Symbol.png"));
            heroIconDisplay = new JLabel(new ImageIcon(heroIcon));
            heroIconDisplay.setBounds(310, 30, 200, 200);

            bottomTrimImage = ImageIO.read(new File("src/files/Bottom_Trim.png"));
            bottomTrimImageDisplay = new JLabel(new ImageIcon(bottomTrimImage));
            bottomTrimImageDisplay.setBounds(-15, 160, resolutionW, 300);

        } catch (IOException e) {
            System.err.println(e.getMessage());
        }

        createCampaignButton();
        createTrainingButton();
        createReadingModeButton();
    }

    /**
     * CreateCampaignButton() creates the button that loads the campaign mode
     * 
     */
    public static void createCampaignButton() {
        campaignButton = new JButton();
        campaignButton.setText("CAMPAIGN");
        campaignButton.setBounds(40, 40, 250, 75);
        campaignButton.setBackground(goldColor);
        campaignButton.setForeground(textColor);
        campaignButton.setFocusable(false);
        Border border = BorderFactory.createBevelBorder(0, Color.black, darkGoldColor);
        campaignButton.setBorder(border);
        campaignButton.setFont(new Font("Abadi MT", Font.PLAIN, 38));
        UIManager.put("Button.select", darkGoldColor);
        campaignButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                loadCampaign();
            }
        });

        // Create Tooltip
        createTooltip(campaignButton,
                "Start your adventure through the gauntlet that awaits. Conquer the world ahead of you. Don't give up!",
                40, 115, 250, 80, 12, baseColor);

    }

    /**
     * CreateTrainingButton() creates the button that loads the training mode
     * 
     */
    public static void createTrainingButton() {
        trainingButton = new JButton();
        trainingButton.setText("TRAINING");
        trainingButton.setBounds(530, 40, 250, 75);
        trainingButton.setBackground(goldColor);
        trainingButton.setForeground(textColor);
        Border border = BorderFactory.createBevelBorder(0, Color.black, darkGoldColor);
        trainingButton.setBorder(border);
        trainingButton.setFocusable(false);
        trainingButton.setFont(new Font("Abadi MT", Font.PLAIN, 38));
        UIManager.put("Button.select", darkGoldColor);
        trainingButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                loadTraining();
            }
        });

        // Create Tooltip
        createTooltip(trainingButton,
                "Practice Mode: train to become skilled enough to challenge the dangers ahead of you.", 530, 115, 250,
                80, 12, baseColor);
    }

    /**
     * CreateReadingModeButton() creates the button that loads the training mode
     * 
     */
    public static void createReadingModeButton() {
        readingModeButton = new JButton();
        readingModeButton.setText("READING");
        readingModeButton.setBounds(40, 200, 250, 75);
        readingModeButton.setBackground(goldColor);
        readingModeButton.setForeground(textColor);
        Border border = BorderFactory.createBevelBorder(0, Color.black, darkGoldColor);
        readingModeButton.setBorder(border);
        readingModeButton.setFocusable(false);
        readingModeButton.setFont(new Font("Abadi MT", Font.PLAIN, 38));
        UIManager.put("Button.select", darkGoldColor);
        readingModeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                loadReadingMode();
            }
        });

        // Create Tooltip
        createTooltip(readingModeButton,
                "Reading Mode: type out words from a book that gets harder as you level up.", 40, 275, 250,
                80, 12, baseColor);
    }

    /**
     * loadCampaign() loads the campaign.java class and runs the campaign game mode
     * 
     */
    public static void loadCampaign() {
        destroyMenuElements();
        campaignMode.initializeCampaignMode();
    }

    /**
     * loadTraining() loads the training.java class and runs the training game mode
     * 
     */
    public static void loadTraining() {
        destroyMenuElements();
        trainingMode.initializeTrainingMode();
    }

    /**
     * loadReadingMode() loads the training.java class and runs the training game
     * mode
     * 
     */
    public static void loadReadingMode() {
        destroyMenuElements();
        readingMode.initializeReadingMode();
    }

    /**
     * DestroyMenuElements() clears all UI components from the frame
     * 
     */
    public static void destroyMenuElements() {
        frame.getContentPane().removeAll();
        frame.repaint();
    }

    /**
     * Creates a description of each button that reveals itself when the player
     * hovers the mouse over the button
     * 
     * @param button    the button to trigger
     * @param text      the text to display
     * @param x         position
     * @param y         position
     * @param width
     * @param height
     * @param fontSize
     * @param textColor
     */
    public static void createTooltip(JButton button, String text, int x, int y, int width, int height, int fontSize,
            Color textColor) {
        JTextPane tooltip = new JTextPane();
        tooltip.setContentType("text/html");
        tooltip.setText(
                "<html><div style=\"font-family: Gadugi size:" + width + "\"><font style=\"color: rgb("
                        + textColor.getRed() + ", "
                        + textColor.getGreen() + ", " + textColor.getBlue() + ")\"> <font style=\"font-size: "
                        + fontSize
                        + "px\">" + text
                        + "</font></font></div></html>");
        tooltip.setBounds(x, y, width, height);
        tooltip.setLayout(null);
        tooltip.setBackground(darkBlueColor);
        tooltip.setForeground(baseColor);
        Border border = BorderFactory.createBevelBorder(0, Color.black, baseColor);
        tooltip.setBorder(border);
        tooltip.setVisible(false);
        menuPanel.add(tooltip);
        button.addMouseListener(new MouseInputListener() {

            @Override
            public void mouseClicked(MouseEvent arg0) {
                // TODO Auto-generated method stub

            }

            @Override
            public void mouseEntered(MouseEvent arg0) {
                tooltip.setVisible(true);
            }

            @Override
            public void mouseExited(MouseEvent arg0) {
                tooltip.setVisible(false);
            }

            @Override
            public void mousePressed(MouseEvent arg0) {
                // TODO Auto-generated method stub

            }

            @Override
            public void mouseReleased(MouseEvent arg0) {
                // TODO Auto-generated method stub

            }

            @Override
            public void mouseDragged(MouseEvent arg0) {
                // TODO Auto-generated method stub

            }

            @Override
            public void mouseMoved(MouseEvent arg0) {
                // TODO Auto-generated method stub

            }
        });
    }
}
