package seedu.address.ui.view;

import java.util.logging.Logger;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.util.FxViewUtil;
import seedu.address.dispatcher.CommandDispatcher;

public class CommandBox extends UiComponent {
    private final Logger logger = LogsCenter.getLogger(CommandBox.class);
    private static final String FXML = "CommandBox.fxml";
    public static final String ERROR_STYLE_CLASS = "error";

    private final CommandDispatcher dispatcher = CommandDispatcher.getInstance();

    @FXML
    private TextField commandTextField;

    public CommandBox(Pane parentNode) {
        super(FXML, parentNode);
    }

    @Override
    protected void viewDidMount () {
        FxViewUtil.applyAnchorBoundaryParameters(getRoot(), 0.0, 0.0, 0.0, 0.0);
        FxViewUtil.applyAnchorBoundaryParameters(commandTextField, 0.0, 0.0, 0.0, 0.0);
    }

    @FXML
    private void handleCommandInputChanged() {
        dispatcher.dispatch(commandTextField.getText());
        setStyleToIndicateCommandSuccess();
        commandTextField.setText("");
//        logger.info("Result: " + commandResult.feedbackToUser);
//        raise(new NewResultAvailableEvent(commandResult.feedbackToUser));
    }


    /**
     * Sets the command box style to indicate a successful command.
     */
    private void setStyleToIndicateCommandSuccess() {
        commandTextField.getStyleClass().remove(ERROR_STYLE_CLASS);
    }

    /**
     * Sets the command box style to indicate a failed command.
     */
    private void setStyleToIndicateCommandFailure() {
        commandTextField.getStyleClass().add(ERROR_STYLE_CLASS);
    }

}