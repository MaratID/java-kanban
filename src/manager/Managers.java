package manager;


public class Managers  {

    public TaskManager getDefault(){
        TaskManager taskManager = new inMemoryTaskManager();
        return taskManager;
    }

    public static HistoryManager getDefaultHistory(){
        HistoryManager historyManager = new inMemoryHistoryManager();
        return historyManager;
    }
}
