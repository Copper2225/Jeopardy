package org.copper;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ReadOnlyDoubleProperty;
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
        EDIT_BUTTONS
    }

    public interface QuestionTypes {
        String TEXT = "text";
        String BILD = "bild";
    }



    private static double screenWidth;
    private static double screenHeight;

    private static int columns = 4;
    private static int rows = 3;
    private static int[] currentIndex = new int[2];
    private static int[][] pointMatrix = new int[columns][rows];

    public static void loadApplicationContext(){
        Rectangle2D bounds = Screen.getPrimary().getBounds();
        screenHeight = bounds.getHeight()/2;
        screenWidth = bounds.getWidth()/2;
    }

    public static void bindProperties(DoubleProperty property, Layouts layout, int padding, int elements, int spacing) {
        ReadOnlyDoubleProperty adminProp = switch (layout){
            case WITDH -> {
                yield AdminScreen.getAdminStage().widthProperty();
            }
            case HEIGHT -> {
                yield AdminScreen.getAdminStage().heightProperty();
            }
        };
        property.bind(adminProp.subtract(padding*2).subtract((elements - 1) * spacing).divide(elements));
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
}
