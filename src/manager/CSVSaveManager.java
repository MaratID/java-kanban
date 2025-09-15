package manager;
import tasks.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CSVSaveManager {
    public static String toString(Task task) {
        Task task1 = new Task(0, "n", "d", Duration.ofMinutes(10), LocalDateTime.of(2025, 9,
                11, 10,10,0));
        Epictask epic1 = new Epictask("nE", "dE", Duration.ofMinutes(0), LocalDateTime.now());
        Subtask s = new Subtask("1", "2", Status.NEW, 0,Duration.ofMinutes(10), LocalDateTime.of(2025, 9,
                12, 12,11,10));
        String taskLine = "";
        if (task.getClass() == s.getClass()) {
            taskLine = String.format("%d,%d,%s,%s,%s,%d,%s,%s",
                    task.getTaskId(),
                    TaskTypes.TYPE_SUBTASK,
                    task.getName(),
                    task.getStatus(),
                    task.getDetails(),
                    ((Subtask) task).getEpicId(),
                    task.getTaskDuration().toMinutes(),
                    task.getTaskStartTime().format(DateTimeFormatter.ofPattern("yyyy-MMMM-dd , HH:mm"))
            );
        } else if (task.getClass() == task1.getClass()) {
            taskLine = String.format("%d,%d,%s,%s,%s,%s,%s",
                    task.getTaskId(),
                    TaskTypes.TYPE_TASK,
                    task.getName(),
                    task.getStatus(),
                    task.getDetails(),
                    task.getTaskDuration().toMinutes(),
                    task.getTaskStartTime().format(DateTimeFormatter.ofPattern("yyyy-MMMM-dd , HH:mm"))
            );
        } else {
            taskLine = String.format("%d,%d,%s,%s,%s,%s,%s",
                    task.getTaskId(),
                    TaskTypes.TYPE_EPIC,
                    task.getName(),
                    task.getStatus(),
                    task.getDetails(),
                    task.getTaskDuration(),
                    task.getTaskStartTime()
            );
        }
        return taskLine;
    }

    public static String getheader() {
        String header = "id,type,name,status,description,epic, duration, startTime";
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
           return new Task(Integer.parseInt(newLine[0]), newLine[2], newLine[4], st,
                   Duration.ofMinutes(Long.parseLong(newLine[6])), LocalDateTime.parse(newLine[7],
                   DateTimeFormatter.ofPattern("yyyy-MMMM-dd , HH:mm")));
        } else if (newLine[1].equals("TYPE_EPIC")) {
           return new Epictask(Integer.parseInt(newLine[0]), newLine[2], newLine[4], Duration.ofMinutes(Long.parseLong(newLine[6])), LocalDateTime.parse(newLine[7],
                   DateTimeFormatter.ofPattern("yyyy-MMMM-dd , HH:mm")));
        } else {
            return new Subtask(Integer.parseInt(newLine[0]), newLine[2], newLine[4], st, Integer.parseInt(newLine[5]),
                    Duration.ofMinutes(Long.parseLong(newLine[6])), LocalDateTime.parse(newLine[7],
                    DateTimeFormatter.ofPattern("yyyy-MMMM-dd , HH:mm")));
        }
    }

    public static Integer historyFromString(String value) {
        return  Integer.parseInt(value.substring(0, value.indexOf(",")));
    }
    //тест
}