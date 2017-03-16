package seedu.toluist.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import seedu.toluist.commons.core.Config;
import seedu.toluist.commons.core.LogsCenter;
import seedu.toluist.commons.core.Messages;
import seedu.toluist.dispatcher.CommandResult;
import seedu.toluist.model.TodoList;
import seedu.toluist.ui.Ui;
import seedu.toluist.ui.UiStore;

/**
 * Responsible for loading-related task
 */
public class LoadController extends Controller {
    private static final Logger logger = LogsCenter.getLogger(LoadController.class);
    private static final String COMMAND_TEMPLATE = "^load(\\s+(?<directory>\\S+))?\\s*";
    public static final String COMMAND_WORD = "load";
    public static final String STORE_DIRECTORY = "directory";

    public LoadController(Ui renderer) {
        super(renderer);
    }

    public CommandResult execute(String command) {
        logger.info(getClass() + "will handle command");
        HashMap<String, String> tokens = tokenize(command);
        String path = tokens.get(STORE_DIRECTORY);

        if (path == null) {
            return new CommandResult(Messages.MESSAGE_NO_STORAGE_PATH);
        }

        Config config = Config.getInstance();
        String oldStoragePath = config.getTodoListFilePath();
        if (oldStoragePath.equals(path)) {
            return new CommandResult(String.format(Messages.MESSAGE_STORAGE_SAME_LOCATION, path));
        }

        // Attemp to load from new storage
        try {
            TodoList newTodoList = TodoList.load().getStorage().load(path);
            newTodoList.save();
            UiStore.getInstance().setTask(newTodoList.getTasks());
            renderer.render();
            return new CommandResult(String.format(Messages.MESSAGE_SET_STORAGE_SUCCESS, path));
        } catch (IOException e) {
            return new CommandResult(String.format(Messages.MESSAGE_SET_STORAGE_FAILURE, path));
        }
    }

    public HashMap<String, String> tokenize(String command) {
        Pattern pattern = Pattern.compile(COMMAND_TEMPLATE);
        Matcher matcher = pattern.matcher(command.trim());
        matcher.find();
        HashMap<String, String> tokens = new HashMap<>();
        tokens.put(STORE_DIRECTORY, matcher.group(STORE_DIRECTORY));
        return tokens;
    }

    public boolean matchesCommand(String command) {
        return command.matches(COMMAND_TEMPLATE);
    }

    public static String[] getCommandWords() {
        return new String[] { COMMAND_WORD };
    }
}