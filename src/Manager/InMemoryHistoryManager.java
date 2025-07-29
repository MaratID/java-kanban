package Manager;

import tasks.Task;

import java.util.ArrayList;

public class InMemoryHistoryManager implements HistoryManager{

    private final ArrayList<Task> tasksHistory = new ArrayList<>();


    @Override
    public ArrayList<Task> getHistory() {
        if(tasksHistory.size() > 10) {
            for (int i = 0; i < tasksHistory.size()-10; i++) {
                tasksHistory.removeFirst();
            }
        }
        return tasksHistory; //тут продумать
    }
    @Override
    public void add(Task task) {
        tasksHistory.add(task);
    }

}
