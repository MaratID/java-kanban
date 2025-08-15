package tasks;
import java.util.ArrayList;

public class Epictask extends Task {
    private ArrayList<Integer> subtasksIds = new ArrayList<>();
    public Epictask(String name, String details) {
        super(name, details);
        this.subtasksIds = new ArrayList<>();
    }

    public Epictask(int id, String name, String details) {
        super(id, name, details);
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
}