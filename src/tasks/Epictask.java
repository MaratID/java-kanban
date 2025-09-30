package tasks;

import java.time.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Optional;

public class Epictask extends Task {

    private ArrayList<Integer> subtasksIds = new ArrayList<>();
    private LocalDateTime epickEndTime;

    public Epictask(String name, String details, Duration duration,
                    LocalDateTime taskStartTime) {
        super(name, details, duration,taskStartTime);
        this.subtasksIds = new ArrayList<>();
    }

    public Epictask(int id, String name, String details, Duration duration,
                    LocalDateTime taskStartTime) {
        super(id, name, details,duration,taskStartTime);
        this.subtasksIds = new ArrayList<>();
    }

    public ArrayList<Integer> getSubtasksIds() {
        return subtasksIds;
    }

    public LocalDateTime getEpickEndTime() {
        return epickEndTime;
    }

    public void addSubtaskIDs(int id) {
        subtasksIds.add(id);
    }

    public void clearSubtaskIds() {
        subtasksIds.clear();
    }

    public void removeSubtask(int id) {
        subtasksIds.remove(Integer.valueOf(id));
    }

    public void setEpickTimeParameters(Subtask subtask, HashMap<Integer, Subtask> subtasks) {
        Optional<Subtask> earliestSubtask = subtasks.values().stream()
                .min(Comparator.comparing(Subtask::getTaskStartTime));
        earliestSubtask.ifPresent(value -> super.setTaskStartTime(value.getTaskStartTime()));
        Optional<Subtask> lastestSubtask = subtasks.values().stream()
                .max(Comparator.comparing(Subtask::getEndTime));
        lastestSubtask.ifPresent(value -> setEpickEndTime(value.getEndTime()));
        super.setTaskDuration(Duration.between(earliestSubtask.get().getTaskStartTime(),
                lastestSubtask.get().getEndTime()));
    }

    public void setEpickEndTime(LocalDateTime localDateTime) {
        this.epickEndTime = localDateTime;
    }

    @Override
    public String toString() {
        return "Epictask{" +
                "name='" + super.getName() + '\'' +
                ", details='" + super.getDetails() + '\'' +
                ", taskId=" + super.getTaskId() + '\'' +
                ", status=" + super.getStatus() + '\'' +
                "subtasksIds=" + subtasksIds +
                '}';
    }
    //тест
}