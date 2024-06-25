package org.copper.Admin.AdminPlay;

import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableArray;
import javafx.collections.ObservableList;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import org.copper.Admin.AdminScreen;
import org.copper.ApplicationContext;
import org.copper.Play.PlayScreen;

import java.util.Arrays;

public class Inputs {
    VBox root;
    CheckBox[] inputs = new CheckBox[ApplicationContext.getTeamAmount()];
    ObservableList<String> inputTexts = FXCollections.observableArrayList();

    public Inputs() {
        root = new VBox();
        for(int i = 0; i < ApplicationContext.getTeamAmount(); i++){
            inputTexts.add("");
            CheckBox checkBox = new CheckBox(PlayScreen.getTeamNames().get(i) + ": ");
            checkBox.getStyleClass().add("teamCheck");
            checkBox.textProperty().bind(Bindings.valueAt(PlayScreen.getTeamNames(), i).asString().concat(": ").concat(Bindings.valueAt(inputTexts, i)));
            root.getChildren().add(checkBox);
            bind(checkBox);
            inputs[i] = checkBox;
        }
        AdminPlayScene.getRoot().add(root ,1, 1);
    }

    private void bind(Control... nodes){
        Arrays.stream(nodes).forEach(n -> {
            n.prefWidthProperty().bind(AdminScreen.getAdminStage().widthProperty().divide(2).subtract(30).subtract((nodes.length - 1) * 10).divide(nodes.length));
            n.prefHeightProperty().bind(AdminScreen.getAdminStage().getScene().heightProperty().divide(2).subtract(30).divide(ApplicationContext.getTeamAmount()));
            n.maxWidthProperty().bind(AdminScreen.getAdminStage().widthProperty().divide(2).subtract(30).subtract((nodes.length - 1) * 10).divide(nodes.length));
            n.maxHeightProperty().bind(AdminScreen.getAdminStage().getScene().heightProperty().divide(2).subtract(30).divide(ApplicationContext.getTeamAmount()));
        });
    }

    public ObservableList<String> getInputTexts() {
        return inputTexts;
    }

    public CheckBox[] getInputs() {
        return inputs;
    }

    public void reset(){
        for( int i = 0; i< ApplicationContext.getTeamAmount(); i++){
            inputs[i].setSelected(false);
            inputTexts.set(i, "");
        }
    }
}
