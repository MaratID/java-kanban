package tasks;
import manager.InMemoryTaskManager;

import java.time.*;
import java.util.ArrayList;

public class Epictask extends Task {

    private ArrayList<Integer> subtasksIds = new ArrayList<>();
    private Duration epickDuration;
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

    public void addSubtaskIDs(int id) {
        subtasksIds.add(id);
    }

    public void clearSubtaskIds() {
        subtasksIds.clear();
    }

    public void removeSubtask(int id) {
        subtasksIds.remove(Integer.valueOf(id));
    }
    
    public void getEpickEndTime() {
        this.epickDuration = Duration.between(super.getTaskStartTime(), LocalDateTime.of(LocalDate.now(), LocalTime.now()));
        this.epickEndTime = super.getTaskStartTime().plusMinutes(epickDuration.toMinutes());
    }

    public void setEpickStartTime(LocalDateTime epickStartTime) {
        super.setTaskStartTime(epickStartTime);
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