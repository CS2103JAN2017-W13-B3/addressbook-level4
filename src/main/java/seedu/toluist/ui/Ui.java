package seedu.toluist.ui;

import javafx.stage.Stage;
import seedu.toluist.commons.core.Config;
import seedu.toluist.dispatcher.Dispatcher;

/**
 * API of UI component
 */
public interface Ui {

    /** Starts the UI (and the App).  */
    void start(Stage primaryStage);

    /** Stops the UI. */
    void stop();

    void init(Config config, Dispatcher dispatcher);

    void render();

}