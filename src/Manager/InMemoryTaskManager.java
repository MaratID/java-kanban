package Manager;
import tasks.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;


public class InMemoryTaskManager implements TaskManager {
    //список Задач
    private final HashMap<Integer, Task> tasks = new HashMap<>();
    //списки Эпиков
    private final HashMap<Integer, Epictask> epicTasks = new HashMap<>();
    //список подзадач
    private final HashMap<Integer, Subtask> subtasks = new HashMap<>();


    private final HistoryManager historyManager = Managers.getDefaultHistory();


    private int generatorId = 0;


    //получить список Задач
    @Override
    public ArrayList<Task> getTaskList(){
        ArrayList<Task> list = new ArrayList<>();
        for (Integer key : tasks.keySet()) {
            list.add(tasks.get(key));
        }
        return list;
    }
    //получить список Эпиков
    @Override
    public ArrayList<Epictask> getEpicTaskList(){
        ArrayList<Epictask> list = new ArrayList<>();
        for (Integer key : epicTasks.keySet()) {
            list.add(epicTasks.get(key));
        }
        return list;
    }
    //получить список подзадач
    @Override
    public ArrayList<Subtask> getSubTaskList(){
        ArrayList<Subtask> list = new ArrayList<>();
        for (Integer key : subtasks.keySet()) {
            list.add(subtasks.get(key));
        }
        return list;
    }
    //очищение списков задач
    @Override
    public void clearTasks(){
        for(Integer id : tasks.keySet()){
            historyManager.remove(id);
        }
        tasks.clear();
    }

    @Override
    public void clearEpics(){
        for(Integer id : epicTasks.keySet()){
            for(Integer Sid : epicTasks.get(id).getSubtasksIds()){
                historyManager.remove(Sid);
            }
            historyManager.remove(id);
        }
        epicTasks.clear();
        subtasks.clear();
    }

    @Override
    public void clearSubtasks(){
        for(Integer id : subtasks.keySet()){
            historyManager.remove(id);
        }
        for (Integer id : subtasks.keySet()){
            Subtask subtask = subtasks.get(id);
            int epicID = subtask.getEpicId();
            subtasks.remove(id);
            Epictask epic = epicTasks.get(epicID);
            epic.clearSubtaskIds();
            updateEpicStatus(epicID);
        }
    }

    //Получение по id. Методы для просмотра в getHistory()

    @Override
    public Task getTaskById(int id){
        historyManager.add(tasks.get(id));
        return tasks.get(id);
    }

    @Override
    public Epictask getEpictaskById(int id){
        historyManager.add(epicTasks.get(id));
        return epicTasks.get(id);
    }

    @Override
    public Subtask getSubtaskById(int id){
        historyManager.add(subtasks.get(id));
        return subtasks.get(id);
    }

    //создание Задач
    @Override
    public void createTask(Task task) {
        int a = generateID();
        task.setTaskId(a);
        tasks.put(task.getTaskId(), task);
    }

    @Override
    public void createEpicTask(Epictask epictask){
        //код для класса EpicTask
        epictask.setTaskId(generateID());
        epicTasks.put(epictask.getTaskId(), epictask);
    }

    @Override
    public void createSubtask(Subtask subtask){
        //код для класса SubTask
        final int epicId = subtask.getEpicId();
        Epictask epic = epicTasks.get(epicId);
        if (epic == null) {
            throw new IllegalArgumentException("Эпик с таким ID " + epicId + " не существует");
        }
        if (subtask.getTaskId() == subtask.getEpicId()) {
            throw new IllegalArgumentException("Субтаск не может быть для себя эпиком");
        }
        subtask.setTaskId(generateID());
        subtasks.put(subtask.getTaskId(), subtask);
        epic.addSubtaskIDs(subtask.getTaskId());
        updateEpicStatus(epicId);
    }


    @Override
    public void renewTask(Task task){
        if (tasks.get(task.getTaskId()) == null) {
            return;
        }
        tasks.replace(task.getTaskId(), task);
    }

    @Override
    public void renewEpictask(Epictask epictask){
        if(epicTasks.get(epictask.getTaskId())==null) {
            return;
        }
        epicTasks.replace(epictask.getTaskId(), epictask);
        updateEpicStatus(epictask.getTaskId());
    }

    @Override
    public void renewSubtask(Subtask subtask){
        if (subtasks.get(subtask.getTaskId()) == null) {
            return;
        }
        final Epictask epic = epicTasks.get(subtask.getEpicId());
        subtasks.replace(subtask.getTaskId(), subtask);
        updateEpicStatus(subtask.getEpicId());
    }

    @Override
    public void deleteTaskById(int id){
        historyManager.remove(id);
        tasks.remove(id);
    }

    @Override
    public void deleteEpictaskById(int id){
        for(Integer Sid : epicTasks.get(id).getSubtasksIds()){
            historyManager.remove(Sid);
        }
        historyManager.remove(id);
        Iterator<Integer> iterator = subtasks.keySet().iterator();
        while(iterator.hasNext()){
            Integer i = iterator.next();
            Subtask subtask = subtasks.get(i);
            if(subtask.getEpicId() == id){
                iterator.remove();
            }
        }
        epicTasks.remove(id);
    }

    @Override
    public void deleteSubtaskById(int id){
        historyManager.remove(id);
        Subtask subtask = subtasks.remove(id);
        if (subtask == null) {
            return;
        }
        Epictask epictask = epicTasks.get(subtask.getTaskId());
        epictask.removeSubtask(id);
        updateEpicStatus(epictask.getTaskId());
    }

    //дополнительные методы

    @Override
    public ArrayList<Task> getHistory() {
        return historyManager.getHistory();
    }

    public ArrayList<Subtask> getSubtasksByEpicTask(Task epictask){
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


    //дополнительные методы из 4 спринта

    private void updateEpicStatus(int epicId) {
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

    private int generateID(){
        return ++generatorId;
    }
}
