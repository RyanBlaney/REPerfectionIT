package com.example.application.views.reliatype.classes;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


import com.example.application.views.MainLayout;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.ScrollOptions;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.ScrollOptions.Behavior;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.orderedlayout.Scroller.ScrollDirection;
import com.vaadin.flow.component.virtuallist.VirtualList;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

@PageTitle("Reliatype")
@Route(value = "reliatype/reading", layout = MainLayout.class)
public class ReadingMode extends GameLayout {
    private String textToDisplay;

    private VerticalLayout bookSelect;
    private H3 bookSelectMenuLabel;
    private String bookText;
    private Document bookDocument;
    private ArrayList<String> bookSections;
    private String bookTitle;
    private int numberOfSections;
    // public static int chapter = 1;
    private int sectionIndex = 0;
    private int maxWords = 20; // max char count

    private char gameState; // For loadLevel(): 'w' = win, 'l' = loss, '' = game start 

    public ReadingMode() {
        super();

        // Initialize Level
        bookSelectMenuLabel = new H3("");
        textToType = "";
        textToDisplay = "";

        Scroller scrollBox = new Scroller();
        scrollBox.setWidth(720, Unit.PIXELS);
        scrollBox.setMaxHeight(256, Unit.PIXELS);
        scrollBox.setScrollDirection(ScrollDirection.VERTICAL);
        scrollBox.getStyle()
        .set("border-bottom", "1px solid var(--lumo-contrast-20pct)")
        .set("padding", "var(--lumo-space-m)");

        bookSelect = new VerticalLayout();
        bookSelect.setWidth(720, Unit.PIXELS);
        scrollBox.setContent(bookSelect);

        DisplayCategories();

        Button skipButton = new Button(">", new ComponentEventListener<ClickEvent<Button>>() {

            @Override
            public void onComponentEvent(ClickEvent<Button> event) {
                if (bookSections == null) return;

                youWin();
            }
            
        });

        Button seekButton = new Button("<", new ComponentEventListener<ClickEvent<Button>>() {

            @Override
            public void onComponentEvent(ClickEvent<Button> event) {
                if (bookSections == null || sectionIndex <= 0) return;

                sectionIndex--;
                completedText = completedText.substring(0, completedText.length() - getLevelLength(bookSections.get(sectionIndex)));
                beforeLevel.setText(completedText);
                youLose();
            }
            
        });

        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.add(seekButton, skipButton);

        super.gameLayout.add(scrollBox);
        super.add(buttonLayout);

        initializeReadingMode();
    }

    public void youLose() {
        gameState = 'l';

        this.resetCount();
        loadLevel();
    }

    public void youWin() {
        gameState = 'w';
        
        sectionIndex++; // Level up

        adjustLevelStartToCurrent();

        this.resetCount();
        resetInputField();
        loadLevel();
    }

    public void DisplayCategories() {
        // Change menu label
        bookSelectMenuLabel.setText("Book Categories");
        bookSelect.add(bookSelectMenuLabel);

        // ADD BOOK LIST
        String url = "https://www.gutenberg.org/ebooks/bookshelf/";
        try {
            Document mainPage = Jsoup.connect(url).get();

            // Search for categories
            for (Element row : mainPage.select(
                    "div.bookshelf_pages ul")) {
                int index = 1;
                while (!row.select("li:nth-of-type(" + index + ")").text().equals("")) {
                    // Get the link
                    Element link = row.select("li:nth-of-type(" + index + ") a").first();

                    // Get the title
                    String title = row.select("li:nth-of-type(" + index + ")").text();

                    bookSelect.add(new Button(title, new ComponentEventListener<ClickEvent<Button>>() {

                        @Override
                        public void onComponentEvent(ClickEvent<Button> event) {
                            DisplayBooks(link.attr("href"), title);
                        }
                        
                    }));

                    index++;

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void DisplayBooks(String linkToList, String categoryString) {
        bookSelect.removeAll();
        bookSelectMenuLabel.setText(categoryString);
        bookSelect.add(bookSelectMenuLabel);

        // SHOW BOOK LIST
        String url = "https://www.gutenberg.org" + linkToList;
        try {
            Document categoryPage = Jsoup.connect(url).get();

            // Search for books
            for (Element row : categoryPage.select(
                    "li.booklink")) {
                // Get the link
                Element link = row.select("a.link").first();

                // Get the title
                String title = row.select("span.title").text() + " by "
                        + row.select("span.subtitle").text();
                bookSelect.add(new Button(title, new ComponentEventListener<ClickEvent<Button>>() {

                    @Override
                    public void onComponentEvent(ClickEvent<Button> event) {
                        LoadBook(link.attr("href"), title);
                    }
                    
                }));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void LoadBook(String linkToBook, String title) {
        bookSelect.removeAll();
        bookSelectMenuLabel.setText(title);
        bookSelect.add(bookSelectMenuLabel);

        String url = "https://www.gutenberg.org" + linkToBook + ".html.images";

        try {
            bookDocument = Jsoup.connect(url).get();
            bookTitle = title;

            createBookText();
            loadLevel();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadLevel() {


        timerSet = false;

        previewText = "";
        afterLevel.setText(previewText);


        try {
            bookText = bookSections.get(sectionIndex);

            Scanner scan = new Scanner(bookText);

            textToDisplay = "";

            while (scan.hasNextLine()) {
                textToDisplay = textToDisplay + scan.nextLine() + "\n";
            }
            beforeCursor.setText("");
            if (gameState == 'w') { 
                beforeLevel.setText(completedText);
                cursor.setText(String.valueOf(textToDisplay.charAt(0)));
                afterCursor.setText(textToDisplay.substring(1));
            }
            else {
                cursor.setText("");
                afterCursor.setText(textToDisplay);
            }
            textToType = textToDisplay;
            numberOfCharsToType = textToDisplay.length() - 1;

            cursor.scrollIntoView();

            // Load Preview Text
            if (bookSections.get(sectionIndex + 1) != null) {
                bookText = bookSections.get(sectionIndex + 1);

                scan.close();
                scan = new Scanner(bookText);

                while (scan.hasNextLine()) {
                    previewText = previewText + scan.nextLine() + "\n";
                }
            }

            scan.close();
            gameState = ' ';

        } catch (Exception e1) {
            e1.printStackTrace();
        }

        inputText.setVisible(true);

        
    
        //loadLevelData();

        wpmCounter.setClassName("text-normal");
        countWords(textToDisplay);
    }

    /**
     * createBookText() loads the data from the selected book and breaks
     * it up into section by the 'maxWords'
     * The current section is then stored in the 'sectionIndex'
     * 
     */
    public void createBookText() {
        bookSections = new ArrayList<>();
        ArrayList<String> words = new ArrayList<>();

        // Read html and break them up into typeable sections
        try {
            Elements elementsHtml = bookDocument.getElementsByTag("p"); // Gets all <p> tag text
            for (Element section : elementsHtml) { // For each paragraph
                if (!section.text().equals("") && !section.text().equals(" ")) { // ensures the paragraph is not blank
                                                                                 // space or empty
                    words.clear(); // clear previous paragraph
                    Scanner scan = new Scanner(section.text()); // Used to separate words
                    while (scan.hasNext()) {
                        words.add(scan.next() + "");
                    }
                    scan.close();

                    // Split up a paragraph that's greater than the maxWords variable
                    if (wordCount(section.text()) > maxWords) {
                        int wordsOver = wordCount(section.text()) - maxWords;
                        int wordsAdded = 0;
                        String wordsToAdd;
                        while (wordsOver > 0) {
                            if (wordsOver > maxWords) { // maxWords * 2
                                wordsToAdd = "";
                                for (int i = 0 + wordsAdded; i < maxWords + wordsAdded; i++) {
                                    wordsToAdd = wordsToAdd + words.get(i) + " ";
                                }
                                wordsAdded += maxWords;
                                bookSections.add(wordsToAdd.replaceAll("[\\u2018\\u2019]", "'")
                                        .replaceAll("[\\u201C\\u201D]", "\"")); // ensures that the '' are typeable and
                                                                                // adds to the total words
                                numberOfSections++;
                                wordsOver -= maxWords;
                            } else {
                                wordsToAdd = "";
                                for (int i = 0 + wordsAdded; i < wordCount(section.text()); i++) {
                                    wordsToAdd = wordsToAdd + words.get(i) + " ";
                                }
                                wordsAdded = wordCount(section.text());
                                bookSections.add(wordsToAdd.replaceAll("[\\u2018\\u2019]", "'")
                                        .replaceAll("[\\u201C\\u201D]", "\""));
                                numberOfSections++;
                                wordsOver = 0;
                            }
                        }
                    } else {
                        bookSections.add(section.text().replaceAll("[\\u2018\\u2019]", "'")
                                .replaceAll("[\\u201C\\u201D]", "\""));
                        numberOfSections++;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // LOAD PROGRESS
        //loadProgress(); @@@

    }

    public void initializeReadingMode() {

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

        VirtualList<String> list = new VirtualList<>();
        list.setItems(categoryList.toArray(new String[categoryList.size()]));

        // list.addMouseListener(new MouseListener() {

        //     @Override
        //     public void mouseClicked(MouseEvent evt) {
        //         if (evt.getClickCount() == 2) {
        //             int index = list.locationToIndex(evt.getPoint());

        //             // SHOW BOOK LIST
        //             String url = "https://www.gutenberg.org" + categoryURL.get(index);
        //             ArrayList<String> bookURL = new ArrayList<>(1);
        //             List<String> bookList = new ArrayList<>(1);
        //             try {
        //                 Document categoryPage = Jsoup.connect(url).get();

        //                 // Search for books
        //                 for (Element row : categoryPage.select(
        //                         "li.booklink")) {

        //                     // Get the title
        //                     String ticker = row.select("span.title").text() + " by "
        //                             + row.select("span.subtitle").text();
        //                     bookList.add(ticker);

        //                     // Get the link
        //                     Element link = row.select("a.link").first();
        //                     bookURL.add(link.attr("href"));
        //                 }

        //                 JList<String> list = new JList<String>(bookList.toArray(new String[bookList.size()]));
        //                 list.setLayoutOrientation(JList.VERTICAL);
        //                 list.setLayout(null);
        //                 list.setFont(new Font("Gadugi", Font.PLAIN, 22));
        //                 list.setBounds(15, 0, 650, 210);
        //                 list.setBackground(new Color(10, 5, 20));
        //                 list.setForeground(Color.white);

        //                 textPanel.setViewportView(list);

        //                 list.addMouseListener(new MouseListener() {

        //                     @Override
        //                     public void mouseClicked(MouseEvent evt) {
        //                         if (evt.getClickCount() == 2) {
        //                             int index = list.locationToIndex(evt.getPoint());

        //                             String url = "https://www.gutenberg.org" + bookURL.get(index) + ".html.images";

        //                             try {
        //                                 bookDocument = Jsoup.connect(url).get();
        //                                 list.setVisible(false);
        //                                 list.setEnabled(false);

        //                                 textPanel.setViewportView(textToType);

        //                                 bookTitle = bookList.get(index);

        //                                 createBookText();
        //                                 loadLevel();

        //                             } catch (Exception e) {
        //                                 e.printStackTrace();
        //                             }

        //                         }
        //                     }

        //                     @Override
        //                     public void mouseEntered(MouseEvent arg0) {
        //                         // TODO Auto-generated method stub

        //                     }

        //                     @Override
        //                     public void mouseExited(MouseEvent arg0) {
        //                         // TODO Auto-generated method stub

        //                     }

        //                     @Override
        //                     public void mousePressed(MouseEvent arg0) {
        //                         // TODO Auto-generated method stub

        //                     }

        //                     @Override
        //                     public void mouseReleased(MouseEvent arg0) {
        //                         // TODO Auto-generated method stub

        //                     }

        //                 });

        //             } catch (Exception e) {
        //                 e.printStackTrace();
        //             }
        //         }
        //     }
        // });
    }
           
    /**
     * Counts the number of words typed and adds them to the currentWord Arraylist
     * 
     * @param levelToCount
     * @throws FileNotFoundException
     */
    public static int wordCount(String levelToCount) throws FileNotFoundException {
        Scanner scanList = new Scanner(levelToCount);
        int count = 0;
        while (scanList.hasNext()) {
            String next = scanList.next();
            if (!next.equals(""))
                count++;
        }
        scanList.close();
        return count;
    }

}
