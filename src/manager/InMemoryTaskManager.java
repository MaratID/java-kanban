package manager;
import tasks.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    //список Задач
    public final HashMap<Integer, Task> tasks = new HashMap<>();
    //списки Эпиков
    public final HashMap<Integer, Epictask> epicTasks = new HashMap<>();
    //список подзадач
    public final HashMap<Integer, Subtask> subtasks = new HashMap<>();
    public final TreeSet<Task> prioritizedTasks = new TreeSet<>(Comparator.comparing(Task::getTaskStartTime));
    public final HistoryManager historyManager = Managers.getDefaultHistory();
    public int generatorId = 0;

    public HistoryManager getHistoryManager() {
        return historyManager;
    }

    @Override
    public ArrayList<Task> getTaskList() {
        return (ArrayList<Task>) tasks.keySet().stream()
                .map(tasks::get)
                .toList();
    }

    @Override
    public ArrayList<Epictask> getEpicTaskList() {
        ArrayList<Epictask> list = new ArrayList<>();
        for (Integer key : epicTasks.keySet()) {
            list.add(epicTasks.get(key));
        }
        return list;
    }

    @Override
    public ArrayList<Subtask> getSubTaskList() {
        return (ArrayList<Subtask>) subtasks.keySet().stream()
                .map(subtasks::get)
                .toList();
    }

    @Override
    public void clearTasks() {
        tasks.keySet().stream()
                .forEach(historyManager::remove);
        tasks.clear();
    }

    @Override
    public void clearEpics() {
        epicTasks.entrySet().stream()
                .forEach(entry -> {
                    entry.getValue().getSubtasksIds().forEach(historyManager::remove);
                    historyManager.remove(entry.getKey());
                });
        epicTasks.clear();
        subtasks.clear();
    }
//вот тут 13.09.2025 11:41
    @Override
    public void clearSubtasks() {
        subtasks.keySet().stream()
                .forEach(historyManager::remove);
        for (Integer id : subtasks.keySet()) {
            Subtask subtask = subtasks.get(id);
            int epicID = subtask.getEpicId();
            subtasks.remove(id);
            Epictask epic = epicTasks.get(epicID);
            epic.clearSubtaskIds();
            updateEpicStatus(epicID);
        }
    }

    @Override
    public Task getTaskById(int id) {
        historyManager.add(tasks.get(id));
        return tasks.get(id);
    }

    @Override
    public Epictask getEpictaskById(int id) {
        historyManager.add(epicTasks.get(id));
        return epicTasks.get(id);
    }

    @Override
    public Subtask getSubtaskById(int id) {
        historyManager.add(subtasks.get(id));
        return subtasks.get(id);
    }

    @Override
    public void createTask(Task task) {
        int a = generateID();
        task.setTaskId(a);
        tasks.put(task.getTaskId(), task);
        add(task);
    }

    @Override
    public void createEpicTask(Epictask epictask) {
        //код для класса EpicTask
        epictask.setTaskId(generateID());
        epicTasks.put(epictask.getTaskId(), epictask);
        add(epictask);
    }

    @Override
    public void createSubtask(Subtask subtask) {
        //код для класса SubTask
        final int epicId = subtask.getEpicId();
        Epictask epic = epicTasks.get(epicId);
        
        Optional<Subtask> earliestSubtask = epic.getSubtasksIds().stream()
                .map(this::getSubtaskById)
                .min(Comparator.comparing(Subtask::getTaskStartTime));
        earliestSubtask.ifPresent(subtask1 -> epic.setEpickStartTime(subtask1.getTaskStartTime()));
        
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
        add(subtask);
    }

    @Override
    public void renewTask(Task task) {
        if (tasks.get(task.getTaskId()) == null) {
            return;
        }
        tasks.replace(task.getTaskId(), task);
    }

    @Override
    public void renewEpictask(Epictask epictask) {
        if (epicTasks.get(epictask.getTaskId()) == null) {
            return;
        }
        epicTasks.replace(epictask.getTaskId(), epictask);
        updateEpicStatus(epictask.getTaskId());
    }

    @Override
    public void renewSubtask(Subtask subtask) {
        if (subtasks.get(subtask.getTaskId()) == null) {
            return;
        }
        final Epictask epic = epicTasks.get(subtask.getEpicId());
        subtasks.replace(subtask.getTaskId(), subtask);
        updateEpicStatus(subtask.getEpicId());
    }

    @Override
    public void deleteTaskById(int id) {
        historyManager.remove(id);
        tasks.remove(id);
    }

    @Override
    public void deleteEpictaskById(int id) {
        for (Integer sid : epicTasks.get(id).getSubtasksIds()) {
            historyManager.remove(sid);
        }
        historyManager.remove(id);
        Iterator<Integer> iterator = subtasks.keySet().iterator();
        while (iterator.hasNext()) {
            Integer i = iterator.next();
            Subtask subtask = subtasks.get(i);
            if (subtask.getEpicId() == id) {
                iterator.remove();
            }
        }
        epicTasks.remove(id);
    }

    @Override
    public void deleteSubtaskById(int id) {
        historyManager.remove(id);
        Subtask subtask = subtasks.remove(id);
        if (subtask == null) {
            return;
        }
        Epictask epictask = epicTasks.get(subtask.getTaskId());
        epictask.removeSubtask(id);
        updateEpicStatus(epictask.getTaskId());
    }

    @Override
    public ArrayList<Task> getHistory() {
        return historyManager.getHistory();
    }

    public ArrayList<Subtask> getSubtasksByEpicTask(Task epictask) {
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

    private void updateEpicStatus(int epicId) {
        Epictask epictask = epicTasks.get(epicId);
        if (epictask.getSubtasksIds().isEmpty()) {
            epictask.setStatus(Status.NEW);
        }
        int countNew = 0;
        int countDone = 0;
        for (Integer subtaskID : epictask.getSubtasksIds()) {
            Status subtaskStatus = subtasks.get(subtaskID).getStatus();
            if (subtaskStatus.equals(Status.NEW)) {
                countNew++;
            } else if (subtaskStatus.equals(Status.DONE)) {
                countDone++;
            }
            if (countNew == epictask.getSubtasksIds().size()) {
                epictask.setStatus(Status.NEW);
            } else if (countDone == epictask.getSubtasksIds().size()) {
                epictask.setStatus(Status.DONE);
                epictask.getEpickEndTime();
            } else {
                epictask.setStatus(Status.IN_POGRESS);
            }
        }
    }

    private int generateID() {
        return ++generatorId;
    }

    private boolean overLap(Task task1, Task task2) {
        LocalDateTime start1 = task1.getTaskStartTime();
        LocalDateTime end1 = task1.getEndTime();

        LocalDateTime start2 = task2.getTaskStartTime();
        LocalDateTime end2 = task2.getEndTime();

        boolean OverLap = start2.isAfter(end1) || start1.isAfter(end2);
        return !OverLap;
        //!!!!!!!!!!!!!!!продумать как не пересекаться эпикам!!!
    }
    
    private void add(Task task) {
        Optional<Task> overLapping = prioritizedTasks.stream()
                .filter(existTask -> overLap(task, existTask))
                .findFirst();
        try {
            if (overLapping.isPresent()) {
                String message = "Задачи пересекаются";
                throw new TaskValidationExeption(message);
            }
        } catch (TaskValidationExeption e) {
            throw new RuntimeException(e);
        }
        prioritizedTasks.add(task);
    }
    
    private TreeSet<Task> getPrioritizedTasks(Task task) {
        return prioritizedTasks;
    }
    //тест
}