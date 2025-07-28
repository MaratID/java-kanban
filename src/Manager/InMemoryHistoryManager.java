package Manager;

import tasks.Task;

import java.util.ArrayList;

public class InMemoryHistoryManager implements HistoryManager{

    private final ArrayList<Task> tasksHistory = new ArrayList<>(10);


    @Override
    public ArrayList<Task> getHistory() {
        return tasksHistory; //тут продумать
    }
    @Override
    public void add(Task task) {

    }
    @Override
    public void updateHistory(Task task){
        if(tasksHistory.size() > 10){
            tasksHistory.removeFirst();
        }
        tasksHistory.add(task);
    }
}
