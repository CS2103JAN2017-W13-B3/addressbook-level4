package seedu.toluist.ui.view;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import seedu.toluist.commons.util.FxViewUtil;
import seedu.toluist.model.Tag;
import seedu.toluist.model.Task;

/**
 * View to display task row
 */
public class TaskUiView extends UiView {

    private static final String FXML = "TaskView.fxml";

    @FXML
    private Label name;
    @FXML
    private Label id;

    private Task task;
    private int displayedIndex;


    public TaskUiView (Task task, int displayedIndex) {
        super(FXML);
        this.task = task;
        this.displayedIndex = displayedIndex;
    }

    @Override
    protected void viewDidMount() {
        String tags = "";
        for (Tag tag : task.getAllTags()) {
            tags += tag.tagName + " ";
        }
        name.setText(task.description + " TAGS/" + tags);
        id.setText(displayedIndex + ". ");
        FxViewUtil.makeFullWidth(getRoot());
    }
}
