import java.util.ArrayList;
import java.util.HashMap;


public class TaskManager {
    //список Задач
    private final HashMap<Integer, Task> tasks = new HashMap<>();
    //списки Эпиков
    private final HashMap<Integer, Epictask> epicTasks = new HashMap<>();
    //список подзадач
    private final HashMap<Integer, Subtask> subtasks = new HashMap<>();
    private int generatorId = 0;


    //получить список Задач
    public void getTaskList(){
        System.out.println(tasks);
    }
    //получить список Эпиков
    public void getEpicTaskList(){
        System.out.println(epicTasks);
    }
    //получить список подзадач
    public void getSubTaskList(){
        System.out.println(subtasks);
    }

    //получить список подзадач в конкретном Эпике
    public void getSubTaskListInEpic(int epicID){
        System.out.println(epicTasks.get(epicID));
    }



    public void clearTasks(){
        tasks.clear();
    }

    public void clearEpics(){
        epicTasks.clear();
    }

    public void clearSubtasks(){
        subtasks.clear();
    }

    //получение по Id
    public Task getTaskById(int id){
       return tasks.get(id);
    }

    public Epictask getEpictaskById(int id){
        return epicTasks.get(id);
    }

    public Subtask getSubtaskById(int id){
        return subtasks.get(id);
    }

    //создание Задач
    public void createTask(Task task) {
        final int id = ++generatorId;
        task.setTaskId(id);
        tasks.put(id, task);
    }

    public void createEpicTask(Epictask epictask){
        //код для класса EpicTask
        final int id = ++generatorId;
        epictask.setTaskId(id);
        epicTasks.put(id, epictask);
    }

    public void createSubtask(Subtask subtask){
        //код для класса SubTask
        final int epicId = subtask.getEpicId();
        Epictask epic = epicTasks.get(epicId);
        if (epic == null) {
            return;
        }
        final int id = ++generatorId; // откуда это?
        subtask.setTaskId(id);
        subtasks.put(id, subtask);
        epic.addSubtaskIDs(subtask.getTaskId());
        updateEpicStatus(epicId);
    }


    public void renewTask(Task task){
        final int id = task.getTaskId();
        Task toChangedTask = tasks.get(id);
        if (toChangedTask == null) {
            return;
        }
        tasks.put(id, task);
    }
    public void renewEpictask(Epictask epictask){
        final int id = epictask.getTaskId();
        Epictask toChangedEpictask = epicTasks.get(id);
        if (toChangedEpictask == null) {
            return;
        }
        epicTasks.put(id, epictask);
    }
    public void renewSubtask(Subtask subtask){
        final int id = subtask.getTaskId();
        final int epicID = subtask.getEpicId();
        final Subtask savedSubtask = subtasks.get(id);
        if (savedSubtask == null) {
            return;
        }
        final Epictask epic = epicTasks.get(epicID);
        if (epic == null) {
            return;
        }
        subtasks.put(id, subtask);
        updateEpicStatus(epicID);
    }

    public void deleteTaskById(int id){
        tasks.remove(id);
    }

    public void deleteEpictaskById(int id){
        epicTasks.remove(id);
    }

    public void deleteSubtaskById(int id){
        Subtask subtask = subtasks.remove(id);
        if (subtask == null) {
            return;
        }
        Epictask epictask = epicTasks.get(subtask.getTaskId());
        epictask.removeSubtask(id);
        updateEpicStatus(epictask.getTaskId());
    }

    //дополнительные методы
    private void getSubtaskByEpicTask(Epictask epictask){
        final int id = epictask.getTaskId();
        System.out.println(epicTasks.get(id));
    }

    private void updateEpicStatus(int epicId){
        Epictask epictask = epicTasks.get(epicId);
        if (epictask.getSubtasksIds().isEmpty()) {
            epictask.setStatus(Status.NEW);
        }
        int countNew = 0;
        int countDone = 0;
        for(Integer subtaskID : epictask.subtasksIds) {
            Status subtaskStatus = subtasks.get(subtaskID).getStatus();
            if (subtaskStatus.equals(Status.NEW)){
                countNew++;
            } else if (subtaskStatus.equals(Status.DONE)) {
                countDone++;
            }

            if (countNew == epictask.getSubtasksIds().size()) {
                epictask.setStatus(Status.NEW);
            } else if (countDone == epictask.getSubtasksIds().size()) {
                epictask.setStatus(Status.DONE);
            } else {
                epictask.setStatus(Status.IN_POGRESS);
            }
        }
    }
}
