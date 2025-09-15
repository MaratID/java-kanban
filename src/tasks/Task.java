package tasks;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

public class Task {

    private String name;
    private String details;
    private int taskId = 0;
    private Status status;
    private Duration taskDuration;
    private LocalDateTime taskStartTime;

    public Task(String name, String details, Duration duration, LocalDateTime taskStartTime) {
        this.name = name;
        this.details = details;
        this.taskDuration = duration;
        this.taskStartTime = taskStartTime;
    }

    public Task(int id, String name, String details, Duration duration, LocalDateTime taskStartTime) {
        this.name = name;
        this.details = details;
        this.taskId = id;
        this.taskDuration = duration;
        this.taskStartTime = taskStartTime;
    }

    public Task(int id, String name, String details, Status taskStatus, Duration duration, LocalDateTime taskStartTime) {
        this.name = name;
        this.details = details;
        this.taskId = id;
        this.status = taskStatus;
        this.taskDuration = duration;
        this.taskStartTime = taskStartTime;
    }

    public Task(String name, String details, Status taskStatus, Duration duration, LocalDateTime taskStartTime) {
        this.name = name;
        this.details = details;
        this.status = taskStatus;
        this.taskDuration = duration;
        this.taskStartTime = taskStartTime;
    }

    public Task(String name, String details) {
        this.name = name;
        this.details = details;
    }

    public Task(int id, String name, String details) {
        this.name = name;
        this.details = details;
        this.taskId = id;
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public Duration getTaskDuration() {
        return taskDuration;
    }

    public LocalDateTime getTaskStartTime() {
        return taskStartTime;
    }

    public LocalDateTime getEndTime(){
        return taskStartTime.plusMinutes(taskDuration.toMinutes());
    }

    public void setTaskStartTime(LocalDateTime taskStartTime) {
        this.taskStartTime = taskStartTime;
    }

    @Override
    public int hashCode() {
        return Objects.hash(taskId);
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return taskId == task.taskId;
    }

    public String toString() {
        return "Task{" +
                "name='" + name + '\'' +
                ", details='" + details + '\'' +
                ", taskId=" + taskId +
                ", status=" + status +
                '}';
    }
    //тест
}