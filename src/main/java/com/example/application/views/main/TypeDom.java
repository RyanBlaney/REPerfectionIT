package com.example.application.views.main;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@PageTitle("GasLamp Games")
@Route(value = "")
public class TypeDom extends VerticalLayout {

    private TextField name;
    private Button sayHello;
    private Text title;
    private HorizontalLayout hl;

    public TypeDom() {
        title = new Text("GasLamp Games");

        name = new TextField("Your name");
        sayHello = new Button("Say hello");
        sayHello.addClickListener(e -> {
            Notification.show("Hello " + name.getValue());
        });
        sayHello.addClickShortcut(Key.ENTER);

        setMargin(true);

        hl = new HorizontalLayout(name, sayHello);
        hl.setDefaultVerticalComponentAlignment(Alignment.BASELINE);

        add(title, hl);
    }

}
