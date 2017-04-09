//@@author A0127545A
package guitests;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import seedu.toluist.commons.util.DateTimeUtil;
import seedu.toluist.model.Task;
import seedu.toluist.model.TodoList;
import seedu.toluist.testutil.TypicalTestTodoLists;
import seedu.toluist.ui.UiStore;

/**
 * Gui tests for delete task command
 */
public class DeleteTaskCommandTest extends ToLuistGuiTest {
    @Before
    public void setUp() {
        String switchToDefaulTab = "switch i";
        commandBox.runCommand(switchToDefaulTab);
    }

    @Test
    public void deleteTask() {
        Task task = new TypicalTestTodoLists().getTypicalTasks()[0];
        String command = "delete 1";
        runCommandThenCheckForTasks(command, new Task[] {}, new Task[] { task });
    }

    @Test
    public void deleteMultipleTasksIndividually() {
        // Start with empty list
        commandBox.runCommand("delete 2");
        commandBox.runCommand("delete 1");

        // add one task
        String taskDescription = "do homework for Melvin";
        String command = "add " + taskDescription;
        commandBox.runCommand(command);
        Task task = new Task(taskDescription);

        // add task with deadline
        String taskDescription2 = "get v0.2 ready";
        LocalDateTime endDate2 = DateTimeUtil.parseDateString("15 Mar 2017, 12pm");
        String command2 = "add " + taskDescription2 + " /by " + endDate2;
        commandBox.runCommand(command2);
        Task task2 = new Task(taskDescription2, endDate2);

        // add event
        String taskDescription3 = "attend CS2103T tutorial";
        LocalDateTime startDate3 = DateTimeUtil.parseDateString("15 Mar 2017, 12pm");
        LocalDateTime endDate3 = DateTimeUtil.parseDateString("15 Mar 2017, 1pm");
        String command3 = "add " + taskDescription3 + " /from " + startDate3 + " /to " + endDate3;
        commandBox.runCommand(command3);
        Task task3 = new Task(taskDescription3, startDate3, endDate3);

        assertTasksShown(true, task, task2, task3);

        runCommandThenCheckForTasks("delete 3", new Task[] { task2, task3 }, new Task[] { task });
        runCommandThenCheckForTasks("delete 1", new Task[] { task3 }, new Task[] { task, task2 });
        runCommandThenCheckForTasks("delete 1", new Task[] {}, new Task[] { task, task2, task3 });
    }

    @Test
    public void testInvalidIndexInput() {
        String command = "delete 0";
        runCommandThenCheckForResultMessage(command, "No valid index found.");

        command = "delete potato";
        runCommandThenCheckForResultMessage(command, "No valid index found.");
    }

    public void deleteMultipleTasksTogether(String deleteCommand, int listSize, int... taskIndexesLeft) {
        // Start with empty list
        commandBox.runCommand("delete 2");
        commandBox.runCommand("delete 1");

        for (int i = 1; i <= listSize; i++) {
            String command = "add task " + i;
            commandBox.runCommand(command);
        }

        // Use shallow copy of task list so it doesn't mutate upon calling delete command
        List<Task> allTasks = (List<Task>) UiStore.getInstance().getShownTasks().clone();

        commandBox.runCommand(deleteCommand);

        Set<Integer> setOfTaskIndexLeft = new HashSet<Integer>();
        for (int taskIndex: taskIndexesLeft) {
            setOfTaskIndexLeft.add(taskIndex);
        }

        for (int i = 1; i <= listSize; i++) {
            // If task index exist in the set, that particular task should be shown in the UI.
            if (setOfTaskIndexLeft.contains(i)) {
                assertTrue(isTaskShown(allTasks.get(i - 1)));
            } else {
                assertFalse(isTaskShown(allTasks.get(i - 1)));
            }
        }
    }

    @Test
    public void deleteMultipleTasksTogether1() {
        String command = "delete  - 2, 4 -  5, 7    9- ";
        deleteMultipleTasksTogether(command, 10, 3, 6, 8);
    }

    @Test
    public void findThenDeleteTasksThenList() {
        String findCommand = "find lewis";
        commandBox.runCommand(findCommand);

        String deleteCommand = "delete -";
        commandBox.runCommand(deleteCommand);

        String listCommand = "list";
        commandBox.runCommand(listCommand);

        TodoList todoList = new TypicalTestTodoLists().getTypicalTodoList();
        assertTrue(isTaskShown(todoList.getTasks().get(1)));
        assertFalse(isTaskShown(todoList.getTasks().get(0)));
    }


    @Test
    public void switchThenDeleteTasksThenSwitchBack() {
        String addCommand = "add tomorrow by/Tomorrow 1:12pm";
        commandBox.runCommand(addCommand);

        String switchCommand = "switch 3";
        commandBox.runCommand(switchCommand);

        String deleteCommand = "delete 1";
        commandBox.runCommand(deleteCommand);

        String switchAgainCommand = "switch 1";
        commandBox.runCommand(switchAgainCommand);

        TodoList todoList = new TypicalTestTodoLists().getTypicalTodoList();
        assertTasksShown(true, todoList.getTasks().get(0), todoList.getTasks().get(1));
    }

    @Test
    public void deleteMultipleRecurringTaskMultipleTimes() {
        // add recurring floating task, will recur until end date is due.
        String taskDescription = "do homework for Melvin";
        String recurFrequencyString = "daily";
        LocalDateTime recurUntilEndDate = DateTimeUtil.parseDateString("15 May 2018, 12pm");
        String command = "add " + taskDescription + " /repeat " + recurFrequencyString
                       + " /repeatuntil " + recurUntilEndDate;
        Task task = new Task(taskDescription);
        task.setRecurring(recurUntilEndDate, recurFrequencyString);
        runCommandThenCheckForTasks(command, new Task[] { task }, new Task[] {});

        // add task with deadline, can recur once and then get deleted
        String taskDescription2 = "get v0.4 ready";
        String recurFrequencyString2 = "monthly";
        LocalDateTime endDate2 = DateTimeUtil.parseDateString("15 Mar 2017, 12pm");
        LocalDateTime recurUntilEndDate2 = DateTimeUtil.parseDateString("30 Apr 2017, 12pm");
        String command2 = "add " + taskDescription2 + " /by " + endDate2 + " /repeat " + recurFrequencyString2
                + " /repeatuntil " + recurUntilEndDate2;
        Task task2 = new Task(taskDescription2, endDate2);
        task2.setRecurring(recurUntilEndDate2, recurFrequencyString2);
        runCommandThenCheckForTasks(command2, new Task[] { task2 }, new Task[] {});

        // add event, can recur 0 times and then get deleted
        String taskDescription3 = "attend CS2103T tutorial";
        String recurFrequencyString3 = "weekly";
        LocalDateTime startDate3 = DateTimeUtil.parseDateString("24 Mar 2017, 12pm");
        LocalDateTime endDate3 = DateTimeUtil.parseDateString("24 Mar 2017, 1pm");
        LocalDateTime recurUntilEndDate3 = DateTimeUtil.parseDateString("28 Mar 2017, 1pm");
        String command3 = "add " + taskDescription3 + " /from " + startDate3 + " /to " + endDate3
                        + " /repeat " + recurFrequencyString3 + " /repeatuntil " + recurUntilEndDate3;
        Task task3 = new Task(taskDescription3, startDate3, endDate3);
        task3.setRecurring(recurUntilEndDate3, recurFrequencyString3);
        runCommandThenCheckForTasks(command3, new Task[] { task3 }, new Task[] {});

        String deleteAllCommand = "delete -";
        runCommandThenCheckForTasks(deleteAllCommand, new Task[] { task }, new Task[] { task2, task3 });
        task2.updateToNextRecurringTask();
        task3.updateToNextRecurringTask();
        assertTrue(isTaskShown(task2));
        assertFalse(isTaskShown(task3)); // recurring task reached end date, so it got deleted

        runCommandThenCheckForTasks(deleteAllCommand, new Task[] { task }, new Task[] { task2, task3 });
        task2.updateToNextRecurringTask();
        task3.updateToNextRecurringTask();
        assertFalse(isTaskShown(task2)); // recurring task reached end date, so it got deleted for real
        assertFalse(isTaskShown(task3));
    }
}
