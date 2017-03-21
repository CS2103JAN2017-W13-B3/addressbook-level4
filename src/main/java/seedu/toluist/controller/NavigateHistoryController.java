package seedu.toluist.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Logger;

import seedu.toluist.commons.core.LogsCenter;
import seedu.toluist.dispatcher.CommandResult;
import seedu.toluist.model.CommandHistoryList;

/**
 * UnknownCommandController is responsible for rendering the initial UI
 */
public class NavigateHistoryController extends Controller {
    private static final Logger logger = LogsCenter.getLogger(NavigateHistoryController.class);
    private static final String RESULT_MESSAGE = "";
    private static final String COMMAND_WORD = "navigatehistory";
    private static final String DIRECTION_PARAMETER = "direction";
    private static final String ERROR_MESSAGE = "Invalid Parameters for Navigate History command."
                                                 + "\nnavigatehistory (up/down)";
    private CommandHistoryList commandHistory;

    public CommandResult execute(String command) {
        logger.info(getClass().getName() + " will handle command");

        HashMap<String, String> tokens = tokenize(command);

        String direction = tokens.get(DIRECTION_PARAMETER);
        
        if (direction.equals("up")) {
            return showUpCommand();
        } else if (direction.equals("down")) {
            return showDownCommand();
        } else { //error
            return new CommandResult(ERROR_MESSAGE);
        }
    }

    private CommandResult showDownCommand() {
        int pointer = commandHistory.movePointerDown();
        if (pointer == commandHistory.size()) {
            return new CommandResult(RESULT_MESSAGE);
        }
        return new CommandResult(RESULT_MESSAGE);
    }

    private CommandResult showUpCommand() {
        int pointer = commandHistory.movePointerUp();
        return new CommandResult(RESULT_MESSAGE);
    }

    public HashMap<String, String> tokenize(String command) {
        HashMap<String, String> tokens = new HashMap<>();

        command = command.replace(COMMAND_WORD, "").trim();
        tokens.put(DIRECTION_PARAMETER, command);

        return tokens;
    }

    public boolean matchesCommand(String command) {
        return command.startsWith(COMMAND_WORD);
    }

    public static String[] getCommandWords() {
        return new String[] { COMMAND_WORD };
    }

    public void setCommandHistory(CommandHistoryList commandHistory) {
        this.commandHistory = commandHistory;
    }
}
