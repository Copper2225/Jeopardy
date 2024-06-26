package org.copper.Admin.AdminPlay;

import javafx.scene.layout.GridPane;

public class AdminPlayScene {
    private static final GridPane all = new GridPane();
    private static Overview overview = new Overview();
    private static Edit edit = new Edit();
    private static Quest quest = new Quest();
    private static Inputs inputs = new Inputs();

    public AdminPlayScene() {
        all.getStyleClass().add("adminPlayGrid");
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
