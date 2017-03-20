package seedu.toluist.controller;

import java.util.ArrayList;
import java.util.HashMap;

import seedu.toluist.dispatcher.CommandResult;
import seedu.toluist.ui.Ui;
import seedu.toluist.ui.UiStore;


/**
 * Abstract Controller class
 * Controllers are in charge of receiving the input from the UI,
 * modifies the models as appropriate, and render the updated UI subsequently
 */
public abstract class Controller {

    protected final Ui renderer;

    /**
     * UiStore to store data to be used by Ui
     */
    protected final UiStore uiStore = UiStore.getInstance();

    /**
     * ArrayList to store previous commands entered since starting the application
     */
    protected static ArrayList<String> commandHistory = new ArrayList<String>();
    protected static int historyPointer = 0;

    public Controller(Ui renderer) {
        this.renderer = renderer;
    }

    /**
     * Given a command string, execute the command
     * and modifies the data appropriately. Also optionally
     * update the UI
     * @param command
     * @return
     */
    public abstract CommandResult execute(String command);

    /**
     * Given command string, tokenize the string into
     * a dictionary of tokens
     * @param command
     * @return
     */
    public abstract HashMap<String, String> tokenize(String command);

    /**
     * Check if Controller can handle this command
     * @param command
     * @return
     */
    public abstract boolean matchesCommand(String command);

    /**
     * Returns command word(s) used by controller
     */
    public static String[] getCommandWords() {
        return new String[] {};
    }

    /**
     * Adds entered command to history list
     */
    public static void recordCommand(String command) {
        commandHistory.add(command);
        historyPointer = commandHistory.size();
    }
}
