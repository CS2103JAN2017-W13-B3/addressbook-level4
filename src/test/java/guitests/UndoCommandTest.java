//@@author A0131125Y
package guitests;

import static org.junit.Assert.assertFalse;

import org.junit.Test;

import javafx.scene.input.KeyCode;
import seedu.toluist.model.Task;
import seedu.toluist.model.TodoList;
import seedu.toluist.testutil.TypicalTestTodoLists;

/**
 * Gui tests for undo command
 */
public class UndoCommandTest extends ToLuistGuiTest {
    @Test
    public void undoSingleCommand() {
        String taskDescription = "build a rocket";
        String addCommand = "add " + taskDescription;
        Task task = new Task(taskDescription);
        runCommandThenCheckForTasks(addCommand, new Task[] { task }, new Task[0]);

        String undoCommand = "undo";
        runCommandThenCheckForTasks(undoCommand, new Task[0], new Task[] { task });

        assertFalse(TodoList.getInstance().getTasks().contains(task));
    }

    @Test
    public void undoMultipleCommand() {
        String taskDescription = "build a rocket";
        String addCommand = "add " + taskDescription;
        Task task = new Task(taskDescription);
        runCommandThenCheckForTasks(addCommand, new Task[] { task }, new Task[0]);

        String taskDescription2 = "ride a unicorn";
        String addCommand2 = "add " + taskDescription2;
        Task task2 = new Task(taskDescription2);
        runCommandThenCheckForTasks(addCommand2, new Task[] { task2 }, new Task[0]);

        String undoCommand = "undo 2";
        runCommandThenCheckForTasks(undoCommand, new Task[0], new Task[] { task, task2 });

        assertFalse(TodoList.getInstance().getTasks().contains(task));
        assertFalse(TodoList.getInstance().getTasks().contains(task2));
    }

    @Test
    public void undoWithHotkey() {
        String command = "delete -";
        commandBox.runCommand(command);
        for (Task task : new TypicalTestTodoLists().getTypicalTasks()) {
            assertFalse(isTaskShown(task));
        }

        mainGui.press(KeyCode.CONTROL, KeyCode.Z);
        assertTasksShown(true, new TypicalTestTodoLists().getTypicalTasks());
    }
}
