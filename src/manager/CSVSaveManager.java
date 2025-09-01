package manager;
import tasks.*;

public class CSVSaveManager {

    public static String toString(Task task) {
        Task task1 = new Task(0, "n", "d");
        Epictask epic1 = new Epictask("nE", "dE");
        Subtask s = new Subtask("1", "2", Status.NEW, 0);
        String taskLine = "";
        if (task.getClass() == s.getClass()) {
            taskLine = "" + task.getTaskId() + "," + TaskTypes.TYPE_SUBTASK + "," +
                    task.getName() + "," + task.getStatus() + "," + task.getDetails() + "," + ((Subtask) task).getEpicId();
        } else if (task.getClass() == task1.getClass()) {
            taskLine = "" + task.getTaskId() + "," + TaskTypes.TYPE_TASK + "," +
                    task.getName() + "," + task.getStatus() + "," + task.getDetails();
        } else {
            taskLine = "" + task.getTaskId() + "," + TaskTypes.TYPE_EPIC + "," +
                    task.getName() + "," + task.getStatus() + "," + task.getDetails();
        }
        return taskLine;
    }

    public static String getheader() {
        String header = "id,type,name,status,description,epic";
        return header;
    }

    public static Task fromString(String value) {
        String[] newLine = value.split(",");
        Status st;
        if (newLine[3].equals("NEW")) {
            st = Status.NEW;
        } else if (newLine[3].equals("IN_PROGRESS")) {
            st = Status.IN_POGRESS;
        } else {
            st = Status.DONE;
        }
        if (newLine[1].equals("TYPE_TASK")) {
           return new Task(Integer.parseInt(newLine[0]), newLine[2], newLine[4], st);
        } else if (newLine[1].equals("TYPE_EPIC")) {
           return new Epictask(Integer.parseInt(newLine[0]), newLine[2], newLine[4]);
        } else {
            return new Subtask(Integer.parseInt(newLine[0]), newLine[2], newLine[4], st, Integer.parseInt(newLine[5]));
        }
    }

    public static Integer historyFromString(String value) {
        return  Integer.parseInt(value.substring(0, value.indexOf(",")));
    }
    //тест
}