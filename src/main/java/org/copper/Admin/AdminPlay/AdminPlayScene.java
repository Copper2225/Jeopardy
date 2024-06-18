package org.copper.Admin.AdminPlay;

import javafx.scene.layout.GridPane;

public class AdminPlayScene {
    private static final GridPane all = new GridPane();
    private static Overview overview;
    private static Edit edit;

    public AdminPlayScene() {
        overview = new Overview();
        edit = new Edit();
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
}
