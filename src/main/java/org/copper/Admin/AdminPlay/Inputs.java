package org.copper.Admin.AdminPlay;

import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import org.copper.Admin.AdminScreen;
import org.copper.ApplicationContext;
import org.copper.Play.Overview.TeamTile;
import org.copper.Play.PlayScreen;

public class Inputs {
    VBox root;
    CheckBox[] inputs = new CheckBox[ApplicationContext.getTeamAmount()];
    ObservableList<String> inputTexts = FXCollections.observableArrayList();
    BooleanProperty showInputs = new SimpleBooleanProperty(false);

    HBox showBox = new HBox();

    public Inputs() {
        root = new VBox();
        root.getStyleClass().add("play-flow-pane");
        for(int i = 0; i < ApplicationContext.getTeamAmount(); i++){
            inputTexts.add("");
            CheckBox checkBox = new CheckBox(PlayScreen.getTeamNames().get(i) + ": ");
            checkBox.getStyleClass().add("teamCheck");
            checkBox.textProperty().bind(Bindings.valueAt(PlayScreen.getTeamNames(), i).asString().concat(": ").concat(Bindings.valueAt(inputTexts, i)));
            Button showDetailed = getButton(i);
            bind(checkBox, showDetailed);
            HBox team = new HBox(checkBox, showDetailed);
            team.getStyleClass().add("teamInput");
            root.getChildren().add(team);
            inputs[i] = checkBox;
        }
        Button showInputs = new Button("Zeigen");
        showInputs.setOnAction(event -> AdminPlayScene.getInputs().showInputsProperty().set(!AdminPlayScene.getInputs().isShowInputs()));
        showInputs.prefWidthProperty().bind(AdminScreen.getAdminStage().widthProperty().divide(2).subtract(30).multiply(0.84));
        showInputs.prefHeightProperty().bind(AdminScreen.getAdminStage().getScene().heightProperty().divide(2).subtract(90).divide(7));
        showInputs.maxHeightProperty().bind(AdminScreen.getAdminStage().getScene().heightProperty().divide(2).subtract(90).divide(7));
        Button bigger = new Button("+");
        bigger.setOnAction(event -> TeamTile.inputSize.set(TeamTile.inputSize.get() + 0.1));
        bigger.minWidthProperty().bind(AdminScreen.getAdminStage().widthProperty().divide(2).subtract(30).multiply(0.08));
        bigger.prefHeightProperty().bind(AdminScreen.getAdminStage().getScene().heightProperty().divide(2).subtract(30).divide(ApplicationContext.getTeamAmount() + 1));
        bigger.maxHeightProperty().bind(AdminScreen.getAdminStage().getScene().heightProperty().divide(2).subtract(30).divide(ApplicationContext.getTeamAmount() + 1));
        bigger.prefHeightProperty().bind(AdminScreen.getAdminStage().getScene().heightProperty().divide(2).subtract(90).divide(7));
        bigger.maxHeightProperty().bind(AdminScreen.getAdminStage().getScene().heightProperty().divide(2).subtract(90).divide(7));
        Button smaller = new Button("-");
        smaller.setOnAction(event -> TeamTile.inputSize.set(TeamTile.inputSize.get() - 0.1));
        smaller.minWidthProperty().bind(AdminScreen.getAdminStage().widthProperty().divide(2).subtract(30).multiply(0.08));
        smaller.prefHeightProperty().bind(AdminScreen.getAdminStage().getScene().heightProperty().divide(2).subtract(90).divide(7));
        smaller.maxHeightProperty().bind(AdminScreen.getAdminStage().getScene().heightProperty().divide(2).subtract(90).divide(7));
        showBox.getChildren().addAll(smaller, showInputs, bigger);
        root.getChildren().add(showBox);
        AdminPlayScene.getRoot().add(root ,1, 1);
    }

    private Button getButton(int i) {
        Button showDetailed = new Button();
        showDetailed.setOnAction(event -> {
            Alert alert = new Alert(Alert.AlertType.NONE);
            alert.initOwner(AdminScreen.getAdminStage());
            alert.initModality(Modality.WINDOW_MODAL);
            Label alertLabel = new Label();
            alertLabel.textProperty().bind(Bindings.valueAt(inputTexts, i));
            alertLabel.setWrapText(false);
            alert.getDialogPane().setContent(alertLabel);
            alert.titleProperty().bind(Bindings.valueAt(PlayScreen.getTeamNames(), i));
            alert.getButtonTypes().add(ButtonType.CLOSE);
            alert.show();
        });
        return showDetailed;
    }

    private void bind(CheckBox c, Button bt) {
        c.prefWidthProperty().bind((AdminScreen.getAdminStage().widthProperty().divide(2).subtract(30)).multiply(0.92).add(20));
        c.maxWidthProperty().bind((AdminScreen.getAdminStage().widthProperty().divide(2).subtract(30)).multiply(0.92).add(20));
        c.prefHeightProperty().bind(AdminScreen.getAdminStage().getScene().heightProperty().divide(2).subtract(showBox.heightProperty()).divide(ApplicationContext.getTeamAmount()));
        c.maxHeightProperty().bind(AdminScreen.getAdminStage().getScene().heightProperty().divide(2).subtract(showBox.heightProperty()).divide(ApplicationContext.getTeamAmount()));
        bt.minWidthProperty().bind(AdminScreen.getAdminStage().widthProperty().divide(2).subtract(30).multiply(0.08));
        bt.prefHeightProperty().bind(AdminScreen.getAdminStage().getScene().heightProperty().divide(2).subtract(90).divide(7));
        bt.maxHeightProperty().bind(AdminScreen.getAdminStage().getScene().heightProperty().divide(2).subtract(90).divide(7));
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

    public boolean isShowInputs() {
        return showInputs.get();
    }

    public BooleanProperty showInputsProperty() {
        return showInputs;
    }
}
