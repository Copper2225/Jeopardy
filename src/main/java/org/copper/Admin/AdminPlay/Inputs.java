package org.copper.Admin.AdminPlay;

import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import org.copper.ApplicationContext;
import org.copper.Play.PlayScreen;

public class Inputs {
    VBox root;
    Label[] inputs = new Label[ApplicationContext.getTeamAmount()];

    public Inputs() {
        root = new VBox();
        for(int i = 0; i < ApplicationContext.getTeamAmount(); i++){
            Label input = new Label();
            root.getChildren().add(input);
            inputs[i] = input;
        }
        AdminPlayScene.getRoot().add(root ,1, 1);
    }

    public Label[] getInputs() {
        return inputs;
    }
}
