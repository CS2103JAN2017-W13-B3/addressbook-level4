package seedu.toluist.dispatcher;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import seedu.toluist.commons.core.Config;
import seedu.toluist.commons.core.EventsCenter;
import seedu.toluist.commons.core.LogsCenter;
import seedu.toluist.commons.events.ui.NewResultAvailableEvent;
import seedu.toluist.controller.AddTaskController;
import seedu.toluist.controller.AliasController;
import seedu.toluist.controller.ClearController;
import seedu.toluist.controller.Controller;
import seedu.toluist.controller.DeleteTaskController;
import seedu.toluist.controller.ExitController;
import seedu.toluist.controller.FindController;
import seedu.toluist.controller.HistoryController;
import seedu.toluist.controller.LoadController;
import seedu.toluist.controller.MarkController;
import seedu.toluist.controller.NavigateHistoryController;
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
import seedu.toluist.model.AliasTable;
import seedu.toluist.model.CommandHistoryList;

public class CommandDispatcher extends Dispatcher {
    private static final Logger logger = LogsCenter.getLogger(CommandDispatcher.class);
    private final EventsCenter eventsCenter = EventsCenter.getInstance();
    private final AliasTable aliasConfig = Config.getInstance().getAliasTable();

    /**
     * ArrayList to store previous commands entered since starting the application
     */
    private CommandHistoryList commandHistory;

    public CommandDispatcher() {
        super();
        aliasConfig.setReservedKeywords(getControllerKeywords());
        commandHistory = new CommandHistoryList();
    }

    public void dispatch(String command) {
        commandHistory.recordCommand(command);
        String deAliasedCommand = aliasConfig.dealias(command);
        logger.info("De-aliased command to be dispatched: " + deAliasedCommand + " original command " + command);

        Controller controller = getBestFitController(deAliasedCommand);
        logger.info("Controller class to be executed: " + controller.getClass());

        CommandResult feedbackToUser;
        if (controller instanceof HistoryController) {
            ((HistoryController) controller).setCommandHistory(commandHistory);
        }

        feedbackToUser = controller.execute(deAliasedCommand);

        eventsCenter.post(new NewResultAvailableEvent(feedbackToUser.getFeedbackToUser()));
    }

    /**
     * Similar to the dispatch method, however is called by commands from the application
     * As such, does not need alias validation, and does not record command being called
     */
    public void dispatchQuietly(String command) {
        Controller controller = getBestFitController(command);
        logger.info("Controller class to be executed: " + controller.getClass());

        CommandResult feedbackToUser;
        if (controller instanceof NavigateHistoryController) {
            ((NavigateHistoryController) controller).setCommandHistory(commandHistory);
        }

        feedbackToUser = controller.execute(command);

        eventsCenter.post(new NewResultAvailableEvent(feedbackToUser.getFeedbackToUser()));
    }

    private Controller getBestFitController(String command) {
        Collection<Controller> controllerCollection = getAllControllers();

        return controllerCollection
                .stream()
                .filter(controller -> controller.matchesCommand(command))
                .findFirst()
                .orElse(new UnknownCommandController()); // fail-safe
    }

    private Collection<Class <? extends Controller>> getAllControllerClasses() {
        return new ArrayList<>(Arrays.asList(
                AddTaskController.class,
                ClearController.class,
                UpdateTaskController.class,
                DeleteTaskController.class,
                StoreController.class,
                HistoryController.class,
                LoadController.class,
                UndoController.class,
                RedoController.class,
                ExitController.class,
                AliasController.class,
                UnaliasController.class,
                ViewAliasController.class,
                NavigateHistoryController.class,
                UntagController.class,
                FindController.class,
                TagController.class,
                MarkController.class,
                SwitchController.class,
                UnknownCommandController.class
        ));
    }

    private Collection<Controller> getAllControllers() {
        return getAllControllerClasses()
                .stream()
                .map((Class<? extends Controller> klass) -> {
                    try {
                        Constructor constructor = klass.getConstructor();
                        return (Controller) constructor.newInstance();
                    } catch (NoSuchMethodException | InstantiationException
                            | IllegalAccessException | InvocationTargetException e) {
                        // fail-safe. But should not actually reach here
                        return new UnknownCommandController();
                    }
                })
                .collect(Collectors.toList());
    }

    private Set<String> getControllerKeywords() {
        List<String> keywordList = getAllControllerClasses()
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
}
