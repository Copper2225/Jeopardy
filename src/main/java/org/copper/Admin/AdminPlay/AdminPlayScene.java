package org.copper.Admin.AdminPlay;

import javafx.scene.layout.GridPane;

public class AdminPlayScene {
    private static final GridPane all = new GridPane();
    private static Overview overview;
    private static Edit edit;
    private static Quest quest;
    private static Inputs inputs;

    public AdminPlayScene() {
        all.getStyleClass().add("adminPlayGrid");
        overview = new Overview();
        edit = new Edit();
        quest = new Quest();
        inputs = new Inputs();
    }

    public static Overview getOverview() {
        return overview;
    }

    public static Edit getEdit() {
        return edit;
    }

    public static GridPane getRoot(){
        return all;
    }

    public static Quest getQuest() {
        return quest;
    }

    public static Inputs getInputs() {
        return inputs;
    }
}
