package manager;
import java.io.File;

public class Managers  {

    public TaskManager getDefault() {
        return new FileBackedTaskManager(new File("files/tasks.csv"));
    }

    public static HistoryManager getDefaultHistory() {
        HistoryManager historyManager = new InMemoryHistoryManager();
        return historyManager;
    }
}