//@@author A0162011A
package seedu.toluist.controller;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import seedu.toluist.commons.core.LogsCenter;
import seedu.toluist.controller.AddTaskController;
import seedu.toluist.controller.AliasController;
import seedu.toluist.controller.ClearController;
import seedu.toluist.controller.Controller;
import seedu.toluist.controller.DeleteTaskController;
import seedu.toluist.controller.ExitController;
import seedu.toluist.controller.FindController;
import seedu.toluist.controller.HelpController;
import seedu.toluist.controller.HistoryController;
import seedu.toluist.controller.LoadController;
import seedu.toluist.controller.MarkController;
import seedu.toluist.controller.RedoController;
import seedu.toluist.controller.StoreController;
import seedu.toluist.controller.SwitchController;
import seedu.toluist.controller.TagController;
import seedu.toluist.controller.UnaliasController;
import seedu.toluist.controller.UndoController;
import seedu.toluist.controller.UnknownCommandController;
import seedu.toluist.controller.UntagController;
import seedu.toluist.controller.UpdateTaskController;
import seedu.toluist.controller.ViewAliasController;
import seedu.toluist.ui.commons.CommandResult;

/**
 * HelpController is responsible for rendering the initial UI
 */
public class HelpController extends Controller {
    private static final Logger logger = LogsCenter.getLogger(HelpController.class);
    private static final String MESSAGE_ERROR = "Sorry, that command does not exist.\nPlease try something else.";
    private static final String MESSAGE_RESULT = "Displaying Help Screen.";
    private static final String COMMAND_WORD = "help";
    private static final String COMMAND_REGEX = "(?iu)^\\s*help.*";

    private static final String PARAMETER_COMMAND = "command";
    private static final int SECTION_COMMAND = 1;

    private static final int NUMBER_OF_SPLITS_FOR_COMMAND_PARSE = 2;
    private static final String COMMAND_SPLITTER_REGEX = " ";

    public void execute(String command) {
        logger.info(getClass().getName() + " will handle command");
        HashMap<String, String> tokens = tokenize(command);

        String commandWord = tokens.get(PARAMETER_COMMAND);
        if (commandWord.equals("")) {
            showGeneralHelp();
            uiStore.setCommandResult(new CommandResult(MESSAGE_RESULT));
        } else if (getControllerKeywords().contains(commandWord)) {
            showSpecificHelp(commandWord);
            uiStore.setCommandResult(new CommandResult(MESSAGE_RESULT));
        } else {
            uiStore.setCommandResult(new CommandResult(MESSAGE_ERROR));
        }        
    }

    private void showSpecificHelp(String commandWord) {
        // TODO Auto-generated method stub
        
    }

    private void showGeneralHelp() {
        List<List<String>> commandsBasicHelp = getBasicHelpFromClasses();
        List<String> resultText;


        for ( List<String> commandHelpMessage : commandsBasicHelp) {
            resultText.add(String.join("\n", commandHelpMessage));
        }

        uiStore.setTasks(resultText);
    }

    public HashMap<String, String> tokenize(String command) {
        HashMap<String, String> tokens = new HashMap<>();
        command = command.trim();

        String[] listOfParameters = command
                .split(COMMAND_SPLITTER_REGEX, NUMBER_OF_SPLITS_FOR_COMMAND_PARSE);
        try {
            tokens.put(PARAMETER_COMMAND, listOfParameters[SECTION_COMMAND]);
        } catch (Exception e) {
            tokens.put(PARAMETER_COMMAND, "");
        }

        return tokens;
    }

    public boolean matchesCommand(String command) {
        return command.matches(COMMAND_REGEX);
    }

    private Collection<Class <? extends Controller>> getHelpControllerClasses() {
        return new ArrayList<>(Arrays.asList(
                AddTaskController.class,
                ClearController.class,
                UpdateTaskController.class,
                DeleteTaskController.class,
                StoreController.class,
                HistoryController.class,
                LoadController.class,
                UndoController.class,
                HelpController.class,
                RedoController.class,
                ExitController.class,
                AliasController.class,
                UnaliasController.class,
                ViewAliasController.class,
                UntagController.class,
                FindController.class,
                TagController.class,
                MarkController.class,
                SwitchController.class
        ));
    }

    private Set<String> getControllerKeywords() {
        List<String> keywordList = getHelpControllerClasses()
                .stream()
                .map((Class<? extends Controller> klass) -> {
                    try {
                        final String methodName = "getCommandWords";
                        Method method = klass.getMethod(methodName);
                        return Arrays.asList((String[]) method.invoke(null));
                    } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                        return new ArrayList<String>();
                    }
                })
                .flatMap(List::stream)
                .collect(Collectors.toList());
        return new HashSet<>(keywordList);
    }

    private List<List<String>> getBasicHelpFromClasses() {
        List<List<String>> keywordList = getHelpControllerClasses()
                .stream()
                .map((Class<? extends Controller> klass) -> {
                    try {
                        final String methodName = "getBasicHelp";
                        Method method = klass.getMethod(methodName);
                        return Arrays.asList((String[]) method.invoke(null));
                    } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                        return new ArrayList<String>();
                    }
                })
                .collect(Collectors.toList());
        return keywordList;
    }

    public static String[] getCommandWords() {
        return new String[] { COMMAND_WORD };
    }
}
