package tasks;

public class Subtask extends Task{
    private int epicId;

    public Subtask(int id, String name, String details, Status taskStatus, int epicID) {
        super(id, name, details, taskStatus);
        this.epicId = epicID;
    }

    public Subtask(String name, String details, Status taskStatus, int epicID) {
        super(name, details, taskStatus);
        this.epicId = epicID;
    }

    public int getEpicId() {
        return epicId;
    }

    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }

    @Override
    public String toString() {
        return "Subtask{" +
                "name='" + super.getName() + '\'' +
                ", details='" + super.getDetails() + '\'' +
                ", taskId=" + super.getTaskId() + '\'' +
                ", status=" + super.getStatus() +'\'' +
                "epicId=" + epicId +
                '}';
    }
}
