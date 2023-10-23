package com.example.application.views.reliatype;

import com.example.application.views.MainLayout;
import com.vaadin.flow.component.HasElement;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import com.vaadin.flow.theme.lumo.LumoUtility.Margin;

@PageTitle("Reliatype")
@Route(value = "reliatype", layout = MainLayout.class)
@RouteAlias(value = "reliatype", layout = MainLayout.class)
public class ReliatypeMain extends HorizontalLayout {

    private Button campaignButton;
    private Button trainingButton;
    private Button readingButton;

    public ReliatypeMain() {
        VerticalLayout mainLayout = new VerticalLayout();
        mainLayout.setWidth(960, Unit.PIXELS);

        // Elevator Pitch
        Paragraph description = new Paragraph("Are you tired of backspacing every few words? Do you get destroyed in type-racer because you misspell every other word? Well let me share some wisdom that may turn your life around. You won't waste time making up for mistakes if you never f**k up in the first place.");
        Paragraph science = new Paragraph("This game is designed to be challenging, but if you want to see improvements fast, you will need to embrace it and conquer the challenge. Don't be misled by other typing tools or platforms that impose \"correct\" finger placement on you. Every word will have a different finger combination just as there are over 24 ways of playing the note A on a guitar, yet you never have to think about which one you are playing. Just like with an instrument, you can train your muscle memory to notice the patterns in each word and adjust your fingers accordingly.");
        Paragraph advice = new Paragraph("There are 3 different exercises, you can hover over each button to learn more. I recommend starting with campaign mode, then using training to teach your fingers to adapt to unorthodoxed words and phrases. The objectively best gamemode is the reading mode, since you can read a whole book by typing the text in digestable levels. Enjoy the experience, whatever it may be for you.");

        // Button Layout
        HorizontalLayout buttonLayout = new HorizontalLayout();

        // Campaign
        campaignButton = new Button("Campaign Mode");
        campaignButton.addClickListener(e -> {
            campaignButton.getUI().ifPresent(ui -> ui.navigate("reliatype/campaign"));
        });
        campaignButton.setTooltipText("Start your adventure through the gauntlet that awaits. Conquer the world ahead of you. Don't give up!");

        // Training
        trainingButton = new Button("Training Mode");
        trainingButton.addClickListener(e -> {
            trainingButton.getUI().ifPresent(ui -> ui.navigate("reliatype/training"));
        });
        trainingButton.setTooltipText("Practice Mode: train to become skilled enough to challenge the dangers ahead of you.");

        // Reading
        readingButton = new Button("Reading Mode");
        readingButton.addClickListener(e -> {
            trainingButton.getUI().ifPresent(ui -> ui.navigate("reliatype/reading"));
        });
        readingButton.setTooltipText("Reading Mode: type out words from a book that gets harder as you level up.");


        setMargin(true);
        //setVerticalComponentAlignment(Alignment.END, campaignButton, trainingButton, readingButton);

        buttonLayout.setAlignSelf(Alignment.CENTER, description);
        buttonLayout.add(campaignButton, trainingButton, readingButton);
        mainLayout.add(description, science, advice, buttonLayout);
        add(mainLayout);
    }

}
