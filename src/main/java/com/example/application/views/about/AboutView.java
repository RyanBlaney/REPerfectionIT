package com.example.application.views.about;

import com.example.application.views.MainLayout;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.charts.model.Title;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import com.vaadin.flow.theme.lumo.LumoUtility.Margin;

@PageTitle("About")
@Route(value = "about", layout = MainLayout.class)
@RouteAlias(value = "", layout = MainLayout.class)
public class AboutView extends VerticalLayout {

    public AboutView() {
        setWidth(960, Unit.PIXELS);

        H2 title = new H2("RE-Perfection-IT");
        Paragraph pFirst = new Paragraph("This is a web game platform devoted to making games that improve practical skills. These games are created by Ryan Blaney, Co-founder and leading programmer of Cortex Labs. All of the games are intended to be very difficult as we believe that you can't truly see results without struggle.");

        H2 gamesHeader = new H2("About Our Games");
        Paragraph pSecond = new Paragraph("Reliatype is a challenging typing game meant to maximize your accuracy. The reading mode allows you to read a book while learning to improve your typing speed and accuracy simultaneously. You can pick between thousands of books brought to you by Project Gutenberg.");
        Paragraph pThird = new Paragraph("Reticentless is a vocabulary improvement, memory game, designed to be fast paced and intense. You will have to be on your toes in order to keep up with the ever-increasing pace.");

        add(title, pFirst, gamesHeader, pSecond, pThird);
    }

}
