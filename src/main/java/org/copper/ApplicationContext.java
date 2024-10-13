package org.copper;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.geometry.Rectangle2D;
import org.copper.Admin.AdminScreen;

public class ApplicationContext {
    public enum Layouts {
        WITDH,
        HEIGHT
    }

    public enum AdminScenes {
        CONFIG,
        EDIT_BUTTON,
        EDIT_BUTTONS,
        ADMIN_OVERVIEW,
    }

    public interface QuestionTypes {
        String TEXT = "text";
        String BILD = "bild";
        String AUDIO = "audio";
        String CHOICE = "choice";
        String LIST = "list";
    }

    private static double screenWidth;
    private static double screenHeight;
    private static int columns = 6;
    private static int rows = 5;
    private static int teamAmount = 8;
    private static boolean showLogo = true;
    private static int[] currentIndex = new int[2];
    private static int[][] pointMatrix = new int[columns][rows];
    private static String[] categories = new String[columns];
    private static int wrongMultiplier = 2;
    private static String quizName = "testing";
    private static int[] defaultPointValues = {100, 200, 300, 400, 500};

    public static void loadApplicationContext(){
        Rectangle2D bounds = Screen.getPrimary().getBounds();
        screenHeight = bounds.getHeight()/2;
        screenWidth = bounds.getWidth()/2;
    }

    private static BooleanProperty isChoiceQuestion = new SimpleBooleanProperty(false);

    public static void bindProperties(DoubleProperty property, Layouts layout, int padding, int elements, int spacing) {
        ReadOnlyDoubleProperty adminProp = switch (layout){
            case WITDH -> {
                yield AdminScreen.getAdminStage().widthProperty();
            }
            case HEIGHT -> {
                yield AdminScreen.getAdminStage().heightProperty();
            }
        };
        if(elements == 0){
            elements = 1;
        }
        property.bind(adminProp.subtract(padding*2).subtract((elements - 1) * spacing).divide(elements));
    }

    public static Node createSpacer(boolean horizontal) {
        final Region spacer = new Region();
        if(horizontal){
            HBox.setHgrow(spacer, Priority.ALWAYS);
        }else{
            VBox.setVgrow(spacer, Priority.ALWAYS);
        }
        return spacer;
    }

    public static int[] getCurrentIndex() {
        return currentIndex;
    }

    public static void setCurrentIndex(int[] currentIndex) {
        ApplicationContext.currentIndex = currentIndex;
    }

    public static double getScreenWidth() {
        return screenWidth;
    }

    public static void setScreenWidth(double screenWidth) {
        ApplicationContext.screenWidth = screenWidth;
    }

    public static double getScreenHeight() {
        return screenHeight;
    }

    public static void setScreenHeight(double screenHeight) {
        ApplicationContext.screenHeight = screenHeight;
    }

    public static int getColumns() {
        return columns;
    }

    public static String[] getCategories() {
        return categories;
    }

    public static void setCategories(String[] categories) {
        ApplicationContext.categories = categories;
    }

    public static void setColumns(int columns) {
        ApplicationContext.columns = columns;
    }

    public static int getRows() {
        return rows;
    }

    public static void setRows(int rows) {
        ApplicationContext.rows = rows;
    }

    public static int[][] getPointMatrix() {
        return pointMatrix;
    }

    public static void setPointMatrix(int[][] pointMatrix) {
        ApplicationContext.pointMatrix = pointMatrix;
    }

    public static int getTeamAmount() {
        return teamAmount;
    }

    public static void setTeamAmount(int teamAmount) {
        ApplicationContext.teamAmount = teamAmount;
    }

    public static int getWrongMultiplier() {
        return wrongMultiplier;
    }

    public static void setWrongMultiplier(int wrongMultiplier) {
        ApplicationContext.wrongMultiplier = wrongMultiplier;
    }

    public boolean isIsChoiceQuestion() {
        return isChoiceQuestion.get();
    }

    public static BooleanProperty isChoiceQuestionProperty() {
        return isChoiceQuestion;
    }

    public static void setIsChoiceQuestion(boolean isChoiceQuestion) {
        ApplicationContext.isChoiceQuestion.set(isChoiceQuestion);
    }

    public static String getQuizName() {
        return quizName;
    }

    public static void setQuizName(String quizName) {
        ApplicationContext.quizName = quizName;
    }

    public static int[] getDefaultPointValues() {
        return defaultPointValues;
    }

    public static void setDefaultPointValues(int[] defaultPointValues) {
        ApplicationContext.defaultPointValues = defaultPointValues;
    }

    public static boolean isShowLogo() {
        return showLogo;
    }

    public static void setShowLogo(boolean showLogo) {
        ApplicationContext.showLogo = showLogo;
    }
}
