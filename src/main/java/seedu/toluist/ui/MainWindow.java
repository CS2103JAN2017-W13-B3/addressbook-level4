package seedu.toluist.ui;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.logging.Logger;

import javax.swing.ImageIcon;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.TextInputControl;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import javafx.stage.Stage;

import seedu.toluist.commons.core.Config;
import seedu.toluist.commons.core.GuiSettings;
import seedu.toluist.commons.core.LogsCenter;
import seedu.toluist.commons.events.ui.ExitAppRequestEvent;
import seedu.toluist.commons.util.FxViewUtil;
import seedu.toluist.dispatcher.Dispatcher;
import seedu.toluist.ui.view.CommandBox;
import seedu.toluist.ui.view.ResultView;
import seedu.toluist.ui.view.TabBarView;
import seedu.toluist.ui.view.TaskListUiView;

/**
 * The Main Window. Provides the basic application layout containing
 * a menu bar and space where other JavaFX elements can be placed.
 */
public class MainWindow extends UiPart<Region> {
    private static final Logger logger = LogsCenter.getLogger(MainWindow.class);
    private static final String LOGO_IMAGE_PATH = "/images/logo.png";
    private static final String FXML = "MainWindow.fxml";
    private static final int MIN_HEIGHT = 600;
    private static final int MIN_WIDTH = 800;

    private Stage primaryStage;
    private Dispatcher dispatcher;

    @FXML
    private AnchorPane commandBoxPlaceholder;

    @FXML
    private AnchorPane taskListPlaceholder;

    @FXML
    private AnchorPane resultDisplayPlaceholder;

    @FXML
    private AnchorPane tabPanePlaceholder;

    private CommandBox commandBox;
    private TaskListUiView taskListUiView;
    private ResultView resultView;
    private TabBarView tabBarView;


    public MainWindow (Stage primaryStage, Dispatcher dispatcher) {
        super(FXML);

        // Set dependencies
        this.primaryStage = primaryStage;
        this.dispatcher = dispatcher;

        // Configure the UI
        setLogo();
        setWindowMinSize();
        setWindowDefaultSize();
        Scene scene = new Scene(getRoot());
        primaryStage.setScene(scene);
        configureKeyCombinations();
        configureChildrenViews();
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public void render() {
        taskListUiView.render();
        commandBox.render();
        resultView.render();
        tabBarView.render();
    }

    private void configureKeyCombinations() {
        configureSwitchTabKeyCombinations();
        configureHistoryNavigationKeyPresses();
    }

    /**
     * Sets the key combination on root.
     * @param keyCombination the KeyCombination value of the accelerator
     * @param handler the event handler
     */
    private void setKeyCombination(KeyCombination keyCombination, EventHandler<ActionEvent> handler) {
        /*
         * TODO: the code below can be removed once the bug reported here
         * https://bugs.openjdk.java.net/browse/JDK-8131666
         * is fixed in later version of SDK.
         *
         * According to the bug report, TextInputControl (TextField, TextArea) will
         * consume function-key events. Because CommandBox contains a TextField, and
         * ResultView contains a TextArea, thus some accelerators (e.g F1) will
         * not work when the focus is in them because the key event is consumed by
         * the TextInputControl(s).
         *
         * For now, we add following event filter to capture such key events and open
         * help window purposely so to support accelerators even when focus is
         * in CommandBox or ResultView.
         */
        getRoot().addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getTarget() instanceof TextInputControl && keyCombination.match(event)) {
                handler.handle(new ActionEvent());
                event.consume();
            }
        });
    }

    /**
     * Sets the key combination on root.
     * @param keyCode the KeyCode value of the accelerator
     * @param handler the event handler
     */
    private void setKeyCode(KeyCode keyCode, EventHandler<ActionEvent> handler) {
        /*
         * TODO: the code below can be removed once the bug reported here
         * https://bugs.openjdk.java.net/browse/JDK-8131666
         * is fixed in later version of SDK.
         *
         * According to the bug report, TextInputControl (TextField, TextArea) will
         * consume function-key events. Because CommandBox contains a TextField, and
         * ResultView contains a TextArea, thus some accelerators (e.g F1) will
         * not work when the focus is in them because the key event is consumed by
         * the TextInputControl(s).
         *
         * For now, we add following event filter to capture such key events and open
         * help window purposely so to support accelerators even when focus is
         * in CommandBox or ResultView.
         */
        getRoot().addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getTarget() instanceof TextInputControl && keyCode.equals(event.getCode())) {
                handler.handle(new ActionEvent());
                event.consume();
            }
        });
    }

    void hide() {
        primaryStage.hide();
    }

    private void configureSwitchTabKeyCombinations() {
        String[] tabNames = new String[] { "i", "t", "n", "c", "a" };
        Arrays.stream(tabNames).forEach(tabName -> {
            KeyCombination keyCombination = new KeyCodeCombination(getKeyCode(tabName), KeyCombination.CONTROL_DOWN);
            String switchCommand = "switch " + tabName;
            EventHandler<ActionEvent> handler = event -> dispatcher.dispatchQuietly(switchCommand);
            setKeyCombination(keyCombination, handler);
        });
    }

    private void configureHistoryNavigationKeyPresses() {
        String[] keyNames = new String[] { "up", "down"};
        Arrays.stream(keyNames).forEach(keyName -> {
            KeyCode keycode = getKeyCode(keyName);
            String navigateCommand = "navigatehistory " + keyName;
            EventHandler<ActionEvent> handler = event -> dispatcher.dispatchQuietly(navigateCommand);
            setKeyCode(keycode, handler);
        });    }

    /**
     * Get matching key code for a string
     * @param s string
     * @return a key code
     */
    private KeyCode getKeyCode(String s) {
        switch (s) {
        case "i": return KeyCode.I;
        case "t": return KeyCode.T;
        case "n": return KeyCode.N;
        case "c": return KeyCode.C;
        case "a": return KeyCode.A;
        case "up": return KeyCode.UP;
        case "down": return KeyCode.DOWN;
        default: return KeyCode.ESCAPE;
        }
    }

    private AnchorPane getTaskListPlaceholder() {
        return taskListPlaceholder;
    }

    private AnchorPane getCommandBoxPlaceholder() {
        return commandBoxPlaceholder;
    }

    private AnchorPane getResultDisplayPlaceholder() {
        return resultDisplayPlaceholder;
    }

    private AnchorPane getTabPanePlaceholder() {
        return tabPanePlaceholder;
    }

    private void setWindowMinSize() {
        primaryStage.setMinHeight(MIN_HEIGHT);
        primaryStage.setMinWidth(MIN_WIDTH);
    }

    /**
     * Sets the logo for the app
     */
    private void setLogo() {
        FxViewUtil.setStageIcon(primaryStage, LOGO_IMAGE_PATH);
        // Only in macOS, you can try to use reflection to access this library
        // and use it to set a custom app icon
        try {
            Class applicationClass = Class.forName("com.apple.eawt.Application");
            Method getApplication = applicationClass.getMethod("getApplication");
            Object application = getApplication.invoke(applicationClass);
            Method setDockIconImage = applicationClass.getMethod("setDockIconImage", java.awt.Image.class);
            setDockIconImage.invoke(application,
                    new ImageIcon(MainWindow.class.getResource(LOGO_IMAGE_PATH)).getImage());
        } catch (NoSuchMethodException | IllegalAccessException
                | InvocationTargetException | ClassNotFoundException e) {
            logger.info("Not on macOS");
        }
    }

    /**
     * Sets the default size based on user config.
     */
    private void setWindowDefaultSize() {
        GuiSettings guiSettings = Config.getInstance().getGuiSettings();
        primaryStage.setHeight(guiSettings.getWindowHeight());
        primaryStage.setWidth(guiSettings.getWindowWidth());
        if (guiSettings.getWindowCoordinates() != null) {
            primaryStage.setX(guiSettings.getWindowCoordinates().getX());
            primaryStage.setY(guiSettings.getWindowCoordinates().getY());
        }
    }

    private void configureChildrenViews() {
        taskListUiView = new TaskListUiView();
        taskListUiView.setParent(getTaskListPlaceholder());

        commandBox = new CommandBox(dispatcher);
        commandBox.setParent(getCommandBoxPlaceholder());

        resultView = new ResultView();
        resultView.setParent(getResultDisplayPlaceholder());

        tabBarView = new TabBarView();
        tabBarView.setParent(getTabPanePlaceholder());
    }

    void show() {
        primaryStage.show();
    }

    /**
     * Returns the current size and the position of the main Window.
     */
    GuiSettings getCurrentGuiSetting() {
        return new GuiSettings(primaryStage.getWidth(), primaryStage.getHeight(),
                (int) primaryStage.getX(), (int) primaryStage.getY());
    }

    /** ================ ACTION HANDLERS ================== **/

    @FXML
    public void handleHelp() {
    }

    @FXML
    public void handleMenu() {
    }

    /**
     * Closes the application.
     */
    @FXML
    private void handleExit() {
        raise(new ExitAppRequestEvent());
    }
}
