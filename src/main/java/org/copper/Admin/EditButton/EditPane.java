package org.copper.Admin.EditButton;

import javafx.scene.layout.Pane;
import org.copper.Questions.Question;

abstract public class EditPane {
    protected Pane pane;

    public EditPane() {
    }

    public Pane getPane() {
        return pane;
    }

    public abstract void load(Question question);
    public abstract void save(int[] index);
}
