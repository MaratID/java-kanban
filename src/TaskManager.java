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

    private int generateID(){
        return generatorId++;
    }
    //получить список Задач
    public ArrayList<Task> getTaskList(){
        ArrayList<Task> list = new ArrayList<>();
        for (Integer key : tasks.keySet()) {
            list.add(tasks.get(key));
        }
        return list;
    }
    //получить список Эпиков
    public ArrayList<Epictask> getEpicTaskList(){
        ArrayList<Epictask> list = new ArrayList<>();
        for (Integer key : epicTasks.keySet()) {
            list.add(epicTasks.get(key));
        }
        return list;
    }
    //получить список подзадач
    public ArrayList<Subtask> getSubTaskList(){
        ArrayList<Subtask> list = new ArrayList<>();
        for (Integer key : subtasks.keySet()) {
            list.add(subtasks.get(key));
        }
        return list;
    }

    public void clearTasks(){
        tasks.clear();
    }

    public void clearEpics(){
        epicTasks.clear();
        subtasks.clear();
    }

    public void clearSubtasks(){
        for (Integer id : subtasks.keySet()){
            Subtask subtask = subtasks.get(id);
            int epicID = subtask.getEpicId();
            subtasks.remove(id);
            Epictask epic = epicTasks.get(epicID);
            epic.clearSubtaskIds();
            updateEpicStatus(epicID);
        }
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
        task.setTaskId(generateID());
        tasks.put(task.getTaskId(), task);
    }

    public void createEpicTask(Epictask epictask){
        //код для класса EpicTask
        epictask.setTaskId(generateID());
        epicTasks.put(epictask.getTaskId(), epictask);
    }

    public void createSubtask(Subtask subtask){
        //код для класса SubTask
        final int epicId = subtask.getEpicId();
        Epictask epic = epicTasks.get(epicId);
        if (epic == null) {
            return;
        }
        subtask.setTaskId(generateID());
        subtasks.put(subtask.getTaskId(), subtask);
        epic.addSubtaskIDs(subtask.getTaskId());
        updateEpicStatus(epicId);
    }


    public void renewTask(Task task){
        if (tasks.get(task.getTaskId()) == null) {
            return;
        }
        tasks.replace(task.getTaskId(), task);
    }

    public void renewEpictask(Epictask epictask){
        if(epicTasks.get(epictask.getTaskId())==null) {
            return;
        }
        epicTasks.replace(epictask.getTaskId(), epictask);
        updateEpicStatus(epictask.getTaskId());
    }

    public void renewSubtask(Subtask subtask){
        if (subtasks.get(subtask.getTaskId()) == null) {
            return;
        }
        final Epictask epic = epicTasks.get(subtask.getEpicId());
        subtasks.replace(subtask.getTaskId(), subtask);
        updateEpicStatus(subtask.getEpicId());
    }

    public void deleteTaskById(int id){
        tasks.remove(id);
    }

    public void deleteEpictaskById(int id){
       for (Integer sub : subtasks.keySet()) {
           Subtask subtask = subtasks.get(sub);
           if (subtask.getEpicId() == id) {
               subtasks.remove(sub);
           }
       }
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
    public ArrayList<Subtask> getSubtasksByEpicTask(Epictask epictask){
        ArrayList<Subtask> subtaskList = new ArrayList<>();
        int id = epictask.getTaskId();
        for (Integer sub : subtasks.keySet()) {
            Subtask subtask = subtasks.get(sub);
            if (subtask.getEpicId() == id) {
                subtaskList.add(subtask);
            }
        }
        return subtaskList;
    }

    private void updateEpicStatus(int epicId){
        Epictask epictask = epicTasks.get(epicId);
        if (epictask.getSubtasksIds().isEmpty()) {
            epictask.setStatus(Status.NEW);
        }
        int countNew = 0;
        int countDone = 0;
        for(Integer subtaskID : epictask.getSubtasksIds()) {
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
