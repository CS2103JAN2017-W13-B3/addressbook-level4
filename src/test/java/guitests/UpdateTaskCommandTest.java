//@@author A0127545A
package guitests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

import seedu.toluist.commons.util.DateTimeUtil;
import seedu.toluist.model.Tag;
import seedu.toluist.model.Task;
import seedu.toluist.model.Task.TaskPriority;
import seedu.toluist.testutil.TypicalTestTodoLists;

/**
 * Gui tests for update task command
 */
public class UpdateTaskCommandTest extends ToLuistGuiTest {
    private static final String ADD = "add ";
    private static final String UPDATE = "update ";
    private static final String FROM = " /from ";
    private static final String TO = " /to ";
    private static final String BY = " /by ";
    private static final String FLOATING = " /floating ";
    private static final String TAGS = " /tags ";
    private static final String PRIORITY = " /priority ";
    private static final String REPEAT = " /repeat ";
    private static final String REPEAT_UNTIL = " /repeatuntil ";
    private static final String STOP_REPEATING = " /stoprepeating ";

    private static Tag tag1 = new Tag("tag1");
    private static Tag tag2 = new Tag("tag2");
    private static Tag tag3 = new Tag("tag3");

    @Before
    public void setUp() {
        String switchToDefaultTab = "switch i";
        commandBox.runCommand(switchToDefaultTab);
    }

    @Test
    public void testInvalidIndexInput() {
        String command = UPDATE + " 0 " + "description";
        runCommandThenCheckForResultMessage(command, "No valid index found.");

        command = UPDATE + " 3 " + "description";
        runCommandThenCheckForResultMessage(command,"No valid index found.");

        command = UPDATE + " potato " + "description";
        runCommandThenCheckForResultMessage(command, "No valid index found.");
    }

    @Test
    public void updateFloatingTask() {
        // update description
        Task task = new TypicalTestTodoLists().getTypicalTasks()[0];
        String newDescription = "do homework for Melvin";
        String command = UPDATE + " 1 " + newDescription;
        task.setDescription(newDescription);
        runCommandThenCheckForTasks(command, new Task[] { task },
                new Task[] { new TypicalTestTodoLists().getTypicalTasks()[0] });

        // update tags
        command = UPDATE + "2" + TAGS + "tag2";
        Task task2 = new Task(newDescription);
        task2.replaceTags(new ArrayList<>(Arrays.asList(tag2)));
        runCommandThenCheckForTasks(command, new Task[] { task2 }, new Task[] { task });

        // update tags with new tags
        command = UPDATE + "2" + TAGS + "tag1 tag3" + PRIORITY + "high";
        Task task3 = new Task(newDescription);
        task3.setTaskPriority(TaskPriority.HIGH);
        task3.replaceTags(new ArrayList<>(Arrays.asList(tag1, tag3)));
        runCommandThenCheckForTasks(command, new Task[] { task3 }, new Task[] { task, task2 });
    }

    @Test
    public void updateTaskWithDeadline() {
        int eventIndex = 1;

        // add task with deadline
        String taskDescription = "get v0.2 ready";
        LocalDateTime endDate = DateTimeUtil.parseDateString("15 Mar 2017, 12pm");
        String command = ADD + taskDescription + BY + endDate;
        Task task = new Task(taskDescription, endDate);
        runCommandThenCheckForTasks(command, new Task[] { task }, new Task[0]);

        // update task deadline
        LocalDateTime newEndDate = DateTimeUtil.parseDateString("22 Mar 2017, 11am");
        command = UPDATE + eventIndex + BY + newEndDate;
        Task task2 = new Task(taskDescription, newEndDate);
        runCommandThenCheckForTasks(command, new Task[] { task2 }, new Task[] { task });

        // update task description
        String newTaskDescription = "complete v0.2";
        command = UPDATE + eventIndex + " " + newTaskDescription + PRIORITY + "high";
        Task task3 = new Task(newTaskDescription, newEndDate);
        task3.setTaskPriority(TaskPriority.HIGH);
        runCommandThenCheckForTasks(command, new Task[] { task3 }, new Task[] { task, task2 });

        // update all parameters for task with deadline and tags
        String newerTaskDescription = "get v0.2 ready";
        LocalDateTime newerEndDate = DateTimeUtil.parseDateString("15 Mar 2017, 12pm");
        command = UPDATE + eventIndex + " " + newerTaskDescription + BY + newerEndDate +
                PRIORITY + "low" + TAGS + "tag1 tag3";
        Task task4 = new Task(newerTaskDescription, null, newerEndDate);
        task4.replaceTags(new ArrayList<>(Arrays.asList(tag1, tag3)));
        runCommandThenCheckForTasks(command, new Task[] { task4 }, new Task[] { task, task2, task3 });
    }

    @Test
    public void updateEvents() {
        int eventIndex = 1;

        // add event
        String taskDescription = "attend CS2103T tutorial";
        LocalDateTime startDate = DateTimeUtil.parseDateString("15 Mar 2017, 12pm");
        LocalDateTime endDate = DateTimeUtil.parseDateString("15 Mar 2017, 1pm");
        String command = ADD + taskDescription + FROM + startDate + TO + endDate;
        Task task = new Task(taskDescription, startDate, endDate);
        runCommandThenCheckForTasks(command, new Task[] { task }, new Task[0]);

        // update event start date and end date
        LocalDateTime newStartDate = DateTimeUtil.parseDateString("22 Mar 2017, 12pm");
        LocalDateTime newEndDate = DateTimeUtil.parseDateString("22 Mar 2017, 1pm");
        command = UPDATE + eventIndex + FROM + newStartDate + TO + newEndDate + PRIORITY + "high";
        Task task2 = new Task(taskDescription, newStartDate, newEndDate);
        task2.setTaskPriority(TaskPriority.HIGH);
        runCommandThenCheckForTasks(command, new Task[] { task2 }, new Task[] { task });

        // update event description
        String newTaskDescription = "participate in CS2103 tutorial";
        command = UPDATE + eventIndex + " " + newTaskDescription;
        Task task3 = new Task(newTaskDescription, newStartDate, newEndDate);
        task3.setTaskPriority(TaskPriority.HIGH);
        runCommandThenCheckForTasks(command, new Task[] { task3 }, new Task[] { task, task2 });

        // update all parameters for event (and test tags, startdate and enddate not in order)
        String newerTaskDescription = "attend CS2103T tutorial";
        LocalDateTime newerStartDate = DateTimeUtil.parseDateString("19 Mar 2017, 12pm");
        LocalDateTime newerEndDate = DateTimeUtil.parseDateString("19 Mar 2017, 1pm");
        command = UPDATE + eventIndex + " " + newerTaskDescription +
                  PRIORITY + "low" + TO + newerEndDate + TAGS + "tag1" + FROM + newerStartDate;
        Task task4 = new Task(newerTaskDescription, newerStartDate, newerEndDate);
        task4.replaceTags(new ArrayList<>(Arrays.asList(tag1)));
        runCommandThenCheckForTasks(command, new Task[] { task4 }, new Task[] { task, task2, task3 });

        // update event /from
        command = UPDATE + eventIndex + FROM + newStartDate;
        runCommandThenCheckForResultMessage(command, "Start date must be before end date.");
        command = UPDATE + eventIndex + FROM + startDate;

        Task task5 = new Task(newerTaskDescription, startDate, newerEndDate);
        task5.replaceTags(new ArrayList<>(Arrays.asList(tag1)));
        runCommandThenCheckForTasks(command, new Task[] { task5 }, new Task[] { task4 });

        // update event /to
        LocalDateTime newestEndDate = DateTimeUtil.parseDateString("14 Mar 2017, 1pm");
        command = UPDATE + eventIndex + TO + newestEndDate;
        runCommandThenCheckForResultMessage(command, "Start date must be before end date.");
        command = UPDATE + eventIndex + " " + taskDescription + TO + endDate;
        task.replaceTags(new ArrayList<>(Arrays.asList(tag1)));
        runCommandThenCheckForTasks(command, new Task[] { task }, new Task[] { task5 });
    }

    @Test
    public void updateTaskToADuplicatedTask_shouldNotBeUpdated() {
        String taskDescription = "yet another task";
        String command = ADD + taskDescription;
        commandBox.runCommand(command);
        Task task = new Task(taskDescription);

        taskDescription = "task";
        command = ADD + taskDescription;
        Task task2 = new Task(taskDescription);
        runCommandThenCheckForTasks(command, new Task[] { task, task2 }, new Task[0]);

        command = UPDATE + " 3 " + taskDescription;
        runCommandThenCheckForTasks(command, "Task provided already exist in the list.",
                new Task[] { task, task2 }, new Task[0]);

        command = UPDATE + " 4 " + taskDescription;
        runCommandThenCheckForTasks(command, "Task provided already exist in the list.",
                new Task[] { task, task2 }, new Task[0]);
    }

    @Test
    public void updateMultipleTypeTask_shouldNotBeUpdated() {
        int eventIndex = 1;

        String taskDescription = "attend CS2103T tutorial";
        LocalDateTime startDate = DateTimeUtil.parseDateString("15 Mar 2017, 12pm");
        LocalDateTime endDate = DateTimeUtil.parseDateString("15 Mar 2017, 1pm");
        LocalDateTime endDate2 = DateTimeUtil.parseDateString("15 Mar 2017, 5pm");
        String command = ADD + taskDescription + FROM + startDate + TO + endDate;
        Task task = new Task(taskDescription, startDate, endDate);
        runCommandThenCheckForTasks(command, new Task[] { task }, new Task[0]);

        // Update to both event, and floating task
        command = UPDATE + eventIndex + FLOATING + FROM + startDate + TO + endDate;
        runCommandThenCheckForTasks(command, new Task[] { task }, new Task[0]);

        // Update to both event, and task with deadline
        command = UPDATE + eventIndex + FROM + startDate + TO + endDate + BY + endDate2;
        runCommandThenCheckForTasks(command, new Task[] { task }, new Task[0]);

        // Update to both floating task, and task with deadline
        command = UPDATE + eventIndex + FLOATING + BY + endDate2;
        runCommandThenCheckForTasks(command, new Task[] { task }, new Task[0]);

        // Update to all floating task, task with deadline, event
        command = UPDATE + eventIndex + BY + endDate2 + FROM + startDate + TO + endDate + FLOATING;
        runCommandThenCheckForTasks(command, new Task[] { task }, new Task[0]);
    }

    @Test
    public void updateFloatingTaskToEvent() {
        int eventIndex = 1;

        String taskDescription = "attend CS2103T tutorial";
        LocalDateTime startDate = DateTimeUtil.parseDateString("15 Mar 2017, 12pm");
        LocalDateTime endDate = DateTimeUtil.parseDateString("15 Mar 2017, 1pm");
        String command = ADD + taskDescription;
        Task task = new Task(taskDescription);
        runCommandThenCheckForTasks(command, new Task[] { task }, new Task[0]);

        // /from /to -> Successful update to event
        command = UPDATE + eventIndex + FROM + startDate + TO + endDate;
        Task task2 = new Task(taskDescription, startDate, endDate);
        runCommandThenCheckForTasks(command, new Task[] { task2 }, new Task[] { task });

        // Update back to floating task
        command = UPDATE + eventIndex + FLOATING;
        runCommandThenCheckForTasks(command, new Task[] { task }, new Task[] { task2 });

        // Event /from -> Fail to update
        command = UPDATE + eventIndex + FROM + startDate;
        runCommandThenCheckForTasks(command, "Task cannot contain only start date.",
                new Task[] { task }, new Task[0]);

        // Event /to -> Successful update to deadline task
        command = UPDATE + eventIndex + TO + endDate;
        Task task3 = new Task(taskDescription, endDate);
        runCommandThenCheckForTasks(command, new Task[] { task3 }, new Task[] { task });
    }

    @Test
    public void updateFloatingTaskToTaskWithDeadline() {
        int eventIndex = 1;

        String taskDescription = "attend CS2103T tutorial";
        LocalDateTime endDate2 = DateTimeUtil.parseDateString("15 Mar 2017, 5pm");
        String command = ADD + taskDescription;
        Task task = new Task(taskDescription);
        runCommandThenCheckForTasks(command, new Task[] { task }, new Task[0]);

        // Task with deadline
        command = UPDATE + eventIndex + BY + endDate2;
        Task task2 = new Task(taskDescription, endDate2);
        runCommandThenCheckForTasks(command, new Task[] { task2 }, new Task[] { task });
    }

    @Test
    public void updateTaskWithDeadlineToFloatingTask() {
        int eventIndex = 1;

        String taskDescription = "attend CS2103T tutorial";
        LocalDateTime endDate2 = DateTimeUtil.parseDateString("15 Mar 2017, 5pm");
        String command = ADD + taskDescription + BY + endDate2;
        Task task = new Task(taskDescription, endDate2);
        runCommandThenCheckForTasks(command, new Task[] { task }, new Task[0]);

        // Floating task
        command = UPDATE + eventIndex + FLOATING;
        Task task2 = new Task(taskDescription);
        runCommandThenCheckForTasks(command, new Task[] { task2 }, new Task[] { task });
    }

    @Test
    public void updateTaskWithDeadlineToEvent() {
        int eventIndex = 1;

        String taskDescription = "attend CS2103T tutorial";
        LocalDateTime startDate = DateTimeUtil.parseDateString("15 Mar 2017, 12pm");
        LocalDateTime startDate2 = DateTimeUtil.parseDateString("16 Mar 2017, 12pm");
        LocalDateTime endDate = DateTimeUtil.parseDateString("15 Mar 2017, 1pm");
        LocalDateTime endDate2 = DateTimeUtil.parseDateString("15 Mar 2017, 5pm");
        String command = ADD + taskDescription + BY + endDate2;
        Task task = new Task(taskDescription, endDate2);
        runCommandThenCheckForTasks(command, new Task[] { task }, new Task[0]);

        // /from /to -> Successful update to event
        command = UPDATE + eventIndex + FROM + startDate + TO + endDate;
        Task task2 = new Task(taskDescription, startDate, endDate);
        runCommandThenCheckForTasks(command, new Task[] { task2 }, new Task[] { task });

        // Update back to deadline task
        command = UPDATE + eventIndex + BY + endDate2;
        runCommandThenCheckForTasks(command, new Task[] { task }, new Task[] { task2 });

        // /from -> Successful update to event (provided startDate is before endDate)
        command = UPDATE + eventIndex + FROM + startDate2;
        runCommandThenCheckForTasks(command, "Start date must be before end date.",
                new Task[] { task }, new Task[0]);

        command = UPDATE + eventIndex + FROM + startDate;
        Task task3 = new Task(taskDescription, startDate, endDate2);
        runCommandThenCheckForTasks(command, new Task[] { task3 }, new Task[] { task });

        // Update back to deadline task
        command = UPDATE + eventIndex + BY + endDate2;
        runCommandThenCheckForTasks(command, new Task[] { task }, new Task[] { task3 });

        // /to -> Successful update to deadline task
        command = UPDATE + eventIndex + BY + endDate;
        Task task4 = new Task(taskDescription, endDate);
        runCommandThenCheckForTasks(command, new Task[] { task4 }, new Task[] { task });
    }

    @Test
    public void updateEventToFloatingTask() {
        int eventIndex = 1;

        String taskDescription = "attend CS2103T tutorial";
        LocalDateTime startDate = DateTimeUtil.parseDateString("15 Mar 2017, 12pm");
        LocalDateTime endDate = DateTimeUtil.parseDateString("15 Mar 2017, 1pm");
        String command = ADD + taskDescription + FROM + startDate + TO + endDate;
        Task task = new Task(taskDescription, startDate, endDate);
        runCommandThenCheckForTasks(command, new Task[] { task }, new Task[0]);

        // Event to floating task
        command = UPDATE + eventIndex + FLOATING;
        Task task2 = new Task(taskDescription);
        runCommandThenCheckForTasks(command, new Task[] { task2 }, new Task[] { task });
    }

    @Test
    public void updateEventToTaskWithDeadline() {
        int eventIndex = 1;

        String taskDescription = "attend CS2103T tutorial";
        LocalDateTime startDate = DateTimeUtil.parseDateString("15 Mar 2017, 12pm");
        LocalDateTime endDate = DateTimeUtil.parseDateString("15 Mar 2017, 1pm");
        LocalDateTime endDate2 = DateTimeUtil.parseDateString("15 Mar 2017, 5pm");
        String command = ADD + taskDescription + FROM + startDate + TO + endDate;
        Task task = new Task(taskDescription, startDate, endDate);
        runCommandThenCheckForTasks(command, new Task[] { task }, new Task[0]);

        // Event to task with deadline
        command = UPDATE + eventIndex + BY + endDate2;
        Task task2 = new Task(taskDescription, endDate2);
        runCommandThenCheckForTasks(command, new Task[] { task2 }, new Task[] { task });
    }

    @Test
    public void updateTaskAfterMarkingComplete() {
        String markCompleteCommand = "mark 1";
        commandBox.runCommand(markCompleteCommand);

        // update description
        Task task = new TypicalTestTodoLists().getTypicalTasks()[1];
        String newDescription = "do homework for Melvin";
        String updateCommand = UPDATE + " 1 " + newDescription;
        task.setDescription(newDescription);
        runCommandThenCheckForTasks(updateCommand, new Task[] { task },
                new Task[] { new TypicalTestTodoLists().getTypicalTasks()[1] });
    }

    @Test
    public void updateTaskWithInvalidPriorityLevel_shouldNotBeUpdated() {
        int eventIndex = 1;

        String taskDescription = "attend CS2103T tutorial";
        String command = ADD + taskDescription;
        Task task = new Task(taskDescription);
        runCommandThenCheckForTasks(command, new Task[] { task }, new Task[0]);

        command = UPDATE + eventIndex + PRIORITY + "high low";
        runCommandThenCheckForTasks(command, new Task[] { task }, new Task[0]);

        command = UPDATE + eventIndex + PRIORITY;
        runCommandThenCheckForTasks(command, new Task[] { task }, new Task[0]);
    }

    @Test
    public void updateRecurringEventToRecurringFloatingTask() {
        int eventIndex = 1;

        String taskDescription = "attend CS2103T tutorial";
        String recurFrequencyString = "weekly";
        LocalDateTime from = DateTimeUtil.parseDateString("15 Mar 2017, 12pm");
        LocalDateTime to = DateTimeUtil.parseDateString("15 Mar 2017, 1pm");
        String command = ADD + taskDescription + FROM + from + TO + to + REPEAT + recurFrequencyString;
        Task task = new Task(taskDescription, from, to);
        task.setRecurring(recurFrequencyString);
        runCommandThenCheckForTasks(command, new Task[] { task }, new Task[0]);

        command = UPDATE + eventIndex + FLOATING;
        Task task2 = new Task(taskDescription);
        task2.setRecurring(recurFrequencyString);
        runCommandThenCheckForTasks(command, new Task[] { task2 }, new Task[] { task });
    }

    @Test
    public void updateNonRecurringFloatingTaskToRecurringEvent() {
        int eventIndex = 1;

        String taskDescription = "attend CS2103T tutorial";
        String command = ADD + taskDescription;
        Task task = new Task(taskDescription);
        runCommandThenCheckForTasks(command, new Task[] { task }, new Task[0]);

        LocalDateTime from = DateTimeUtil.parseDateString("15 Mar 2017, 12pm");
        LocalDateTime to = DateTimeUtil.parseDateString("15 Mar 2017, 1pm");
        String recurFrequencyString = "weekly";
        command = UPDATE + eventIndex + FROM + from + TO + to + REPEAT + recurFrequencyString;
        Task task2 = new Task(taskDescription, from, to);
        task2.setRecurring(recurFrequencyString);
        runCommandThenCheckForTasks(command, new Task[] { task2 }, new Task[] { task });
    }

    @Test
    public void updateNonRecurringEventToRecurringEvent() {
        int eventIndex = 1;

        String taskDescription = "attend CS2103T tutorial";
        LocalDateTime from = DateTimeUtil.parseDateString("15 Mar 2017, 12pm");
        LocalDateTime to = DateTimeUtil.parseDateString("15 Mar 2017, 1pm");
        String command = ADD + taskDescription + FROM + from + TO + to;
        Task task = new Task(taskDescription, from, to);
        runCommandThenCheckForTasks(command, new Task[] { task }, new Task[0]);

        String recurFrequencyString = "monthly";
        LocalDateTime recurEndDate = DateTimeUtil.parseDateString("15 Mar 2018, 1pm");
        command = UPDATE + eventIndex + REPEAT + recurFrequencyString + REPEAT_UNTIL + recurEndDate;
        Task task2 = new Task(taskDescription, from, to);
        task2.setRecurring(recurEndDate, recurFrequencyString);
        runCommandThenCheckForTasks(command, new Task[] { task2 }, new Task[] { task });
    }

    @Test
    public void updateNonRecurringEventToRecurringTaskWithDeadline() {
        int eventIndex = 1;

        String taskDescription = "attend CS2103T tutorial";
        LocalDateTime from = DateTimeUtil.parseDateString("15 Mar 2017, 12pm");
        LocalDateTime to = DateTimeUtil.parseDateString("15 Mar 2017, 1pm");
        String command = ADD + taskDescription + FROM + from + TO + to;
        Task task = new Task(taskDescription, from, to);
        runCommandThenCheckForTasks(command, new Task[] { task }, new Task[0]);

        String recurFrequencyString = "yearly";
        LocalDateTime recurEndDate = DateTimeUtil.parseDateString("15 Mar 2020, 1pm");
        command = UPDATE + eventIndex + BY + to
                + REPEAT + recurFrequencyString + REPEAT_UNTIL + recurEndDate;
        Task task2 = new Task(taskDescription, to);
        task2.setRecurring(recurEndDate, recurFrequencyString);
        runCommandThenCheckForTasks(command, new Task[] { task2 }, new Task[] { task });
    }

    @Test
    public void updateRecurringTaskWithDeadlineToNonRecurringTaskWithDeadline() {
        int eventIndex = 1;

        String taskDescription = "attend CS2103T tutorial";
        String recurFrequencyString = "daily";
        LocalDateTime to = DateTimeUtil.parseDateString("15 Mar 2017, 1pm");
        String command = ADD + taskDescription + BY + to + REPEAT + recurFrequencyString;
        Task task = new Task(taskDescription, to);
        task.setRecurring(recurFrequencyString);
        runCommandThenCheckForTasks(command, new Task[] { task }, new Task[0]);

        command = UPDATE + eventIndex + STOP_REPEATING;
        Task task2 = new Task(taskDescription, to);
        runCommandThenCheckForTasks(command, new Task[] { task2 }, new Task[] { task });
    }

    @Test
    public void updateRecurringEventToNonRecurringTaskWithDeadline() {
        int eventIndex = 1;

        String taskDescription = "attend CS2103T tutorial";
        String recurFrequencyString = "weekly";
        LocalDateTime from = DateTimeUtil.parseDateString("15 Mar 2017, 12pm");
        LocalDateTime to = DateTimeUtil.parseDateString("15 Mar 2017, 1pm");
        String command = ADD + taskDescription + FROM + from + TO + to + REPEAT + recurFrequencyString;
        Task task = new Task(taskDescription, from, to);
        task.setRecurring(recurFrequencyString);
        runCommandThenCheckForTasks(command, new Task[] { task }, new Task[0]);

        command = UPDATE + eventIndex + BY + to + STOP_REPEATING;
        Task task2 = new Task(taskDescription, to);
        runCommandThenCheckForTasks(command, new Task[] { task2 }, new Task[] { task });
    }

    @Test
    public void updateRecurringEventToNonRecurringFloatingTask() {
        int eventIndex = 1;

        String taskDescription = "attend CS2103T tutorial";
        String recurFrequencyString = "weekly";
        LocalDateTime from = DateTimeUtil.parseDateString("15 Mar 2017, 12pm");
        LocalDateTime to = DateTimeUtil.parseDateString("15 Mar 2017, 1pm");
        String command = ADD + taskDescription + FROM + from + TO + to + REPEAT + recurFrequencyString;
        Task task = new Task(taskDescription, from, to);
        task.setRecurring(recurFrequencyString);
        runCommandThenCheckForTasks(command, new Task[] { task }, new Task[0]);

        command = UPDATE + eventIndex + FLOATING + STOP_REPEATING;
        Task task2 = new Task(taskDescription);
        runCommandThenCheckForTasks(command, new Task[] { task2 }, new Task[] { task });
    }

    @Test
    public void updateRecurringTask_invalidParams_shouldNotUpdate() {
        int eventIndex = 1;

        String taskDescription = "shower";
        String recurFrequencyString = "daily";
        String command = ADD + taskDescription + REPEAT + recurFrequencyString;
        Task task = new Task(taskDescription);
        task.setRecurring(recurFrequencyString);
        runCommandThenCheckForTasks(command, new Task[] { task }, new Task[0]);

        command = UPDATE + eventIndex + STOP_REPEATING + REPEAT + recurFrequencyString;
        runCommandThenCheckForTasks(command,
                "Input contains both recurring and stop recurring arguments at the same time.",
                new Task[] { task }, new Task[0]);

        LocalDateTime recurEndDate = DateTimeUtil.parseDateString("15 Mar 2020, 1pm");
        command = UPDATE + eventIndex + STOP_REPEATING + REPEAT_UNTIL + recurEndDate;
        runCommandThenCheckForTasks(command,
                "Input contains both recurring and stop recurring arguments at the same time.",
                new Task[] { task }, new Task[0]);
    }

    @Test
    public void updateTaskWithMultipleParameters_testResultMessage() {
        int index = 1;

        LocalDateTime from = DateTimeUtil.parseDateString("5 May 2017, 9pm");
        LocalDateTime to = DateTimeUtil.parseDateString("5 May 2017, 10pm");
        LocalDateTime recurUntil = DateTimeUtil.parseDateString("5 May 2019, 10pm");
        String command = UPDATE + index + " write code" + FROM + from + TO + to + PRIORITY + "high"
                + TAGS + "hello world" + REPEAT + "weekly" + REPEAT_UNTIL + recurUntil;
        runCommandThenCheckForResultMessage(command, "Updated task at index 1:\n" +
            "- Task type: \"TASK\" to \"EVENT\"\n" +
            "- Description: \"clean the house while Lewis is gone\" to \"write code\"\n" +
            "- Start date: \"\" to \"Fri, 05 May 2017 09:00 PM\"\n" +
            "- End date: \"\" to \"Fri, 05 May 2017 10:00 PM\"\n" +
            "- Priority: \"LOW\" to \"HIGH\"\n" +
            "- Repeat: \"\" to \"WEEKLY\"\n" +
            "- Repeat until: \"\" to \"Sun, 05 May 2019 10:00 PM\"\n" +
            "- Tags: \"lewis work\" to \"hello world\"");

        command = UPDATE + index + FLOATING + TAGS + STOP_REPEATING;
        runCommandThenCheckForResultMessage(command, "Updated task at index 1:\n" +
                "- Task type: \"EVENT\" to \"TASK\"\n" +
                "- Start date: \"Fri, 05 May 2017 09:00 PM\" to \"\"\n" +
                "- End date: \"Fri, 05 May 2017 10:00 PM\" to \"\"\n" +
                "- Repeat: \"WEEKLY\" to \"\"\n" +
                "- Repeat until: \"Sun, 05 May 2019 10:00 PM\" to \"\"");
    }

    //@@author
    @Test
    public void updateTaskAfterSwitching() {
        String clearCommand = "clear";
        commandBox.runCommand(clearCommand);
        String switchCommand = "switch t";
        commandBox.runCommand(switchCommand);
        String addCommand = ADD + "sth sth" + BY + "today";
        commandBox.runCommand(addCommand);
        int index = 1;
        String taskDescription = "get a life";
        String updateCommand = UPDATE + index + " " + taskDescription;
        commandBox.runCommand(updateCommand);

        assertEquals(tabBar.getHighlightedTabText(), "TODAY (1/1)");
    }
}
