package seedu.address.controller;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.dispatcher.CommandResult;
import seedu.address.model.TodoList;
import seedu.address.model.task.Task;

/**
 * Created by louis on 21/2/17.
 */
public class AddTaskController extends Controller {

    private static String ADD_TASK_RESULT_MESSAGE = "New task added";

    public CommandResult execute(String command) {
        final TodoList todoList = TodoList.getInstance();
        Task newTask;
        try {
            newTask = new Task(command);
            todoList.addTask(newTask);
            renderer.render(todoList);
        } catch (IllegalValueException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return new CommandResult(ADD_TASK_RESULT_MESSAGE);
    }
}
