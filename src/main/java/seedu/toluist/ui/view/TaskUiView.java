//@@author A0131125Y
package seedu.toluist.ui.view;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import seedu.toluist.commons.util.AppUtil;
import seedu.toluist.commons.util.DateTimeFormatterUtil;
import seedu.toluist.commons.util.FxViewUtil;
import seedu.toluist.model.Tag;
import seedu.toluist.model.Task;

/**
 * View to display task row
 */
public class TaskUiView extends UiView {

    private static final String FXML = "TaskView.fxml";
    private static final String CLOCK_ICON_IMAGE_PATH = "/images/clock.png";
    private static final String OVERDUE_ICON_IMAGE_PATH = "/images/warning.png";
    private static final String COMPLETED_STYLE_CLASS = "completed";
    private static final String OVERDUE_STYLE_CLASS = "overdue";

    @FXML
    private Pane taskPane;
    @FXML
    private FlowPane tagsPane;
    @FXML
    private Label name;
    @FXML
    private Label id;
    @FXML
    private Label date;
    @FXML
    private HBox statusBox;
    @FXML
    private ImageView clockIcon;

    private Task task;
    private int displayedIndex;


    public TaskUiView (Task task, int displayedIndex) {
        super(FXML);
        this.task = task;
        this.displayedIndex = displayedIndex;
    }

    @Override
    protected void viewDidMount() {
        boolean isFloatingTask = task.isFloatingTask();
        boolean isTaskWithDeadline = task.isTaskWithDeadline();
        boolean isTask = isFloatingTask || isTaskWithDeadline;
        boolean isEvent = task.isEvent();

        tagsPane.getChildren().clear();

        TaskTypeTagView taskTypeTagView = new TaskTypeTagView(isTask);
        taskTypeTagView.setParent(tagsPane);
        taskTypeTagView.render();

        for (Tag tag : task.getAllTags()) {
            TagView tagView = new TagView(tag.tagName);
            tagView.setParent(tagsPane);
            tagView.render();
        }

        statusBox.getChildren().clear();
        if (task.isOverdue()) {
            TaskStatusView statusView = new TaskStatusView(AppUtil.getImage(OVERDUE_ICON_IMAGE_PATH));
            statusView.setParent(statusBox);
            statusView.render();
            FxViewUtil.addStyleClass(taskPane, OVERDUE_STYLE_CLASS);
        }

        name.setText(task.getDescription());
        id.setText(displayedIndex + ". ");
        if (isTaskWithDeadline) {
            date.setText(DateTimeFormatterUtil.formatTaskDeadline(task.getEndDateTime()));
        } else if (isEvent) {
            date.setText(DateTimeFormatterUtil.formatEventRange(task.getStartDateTime(), task.getEndDateTime()));
        }
        if (isTaskWithDeadline || task.isEvent()) {
            clockIcon.setImage(AppUtil.getImage(CLOCK_ICON_IMAGE_PATH));
        }
        if (task.isCompleted()) {
            FxViewUtil.addStyleClass(taskPane, COMPLETED_STYLE_CLASS);
        }
    }
}
