package seedu.toluist.model;

import java.util.ArrayList;

public class CommandHistoryList {
    private ArrayList<String> commandHistory;
    private int historyPointer = 0;

    public CommandHistoryList() {
        commandHistory = new ArrayList<String>();
    }

    public ArrayList<String> getAsArrayList() {
        return commandHistory;
    }

    public void recordCommand(String command) {
        commandHistory.add(command);
        historyPointer = commandHistory.size();
    }

    public int movePointerDown() {
        if (historyPointer != commandHistory.size()) {
            historyPointer++;
        }
        return historyPointer;
    }

    public int movePointerUp() {
        if (historyPointer != 0) {
            historyPointer--;
        }
        return historyPointer;
    }

    public int size() {
        return commandHistory.size();
    }
}
