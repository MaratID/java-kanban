package manager;
import tasks.*;
import java.util.ArrayList;

public interface TaskManager {
    //получить список Задач
    ArrayList<Task> getTaskList();
    //получить список Эпиков

    ArrayList<Epictask> getEpicTaskList();
    //получить список подзадач

    ArrayList<Subtask> getSubTaskList();

    void clearTasks();

    void clearEpics();

    void clearSubtasks();

    //получение по Id
    Task getTaskById(int id);

    Epictask getEpictaskById(int id);

    Subtask getSubtaskById(int id);

    //создание Задач
    void createTask(Task task);

    void createEpicTask(Epictask epictask);

    void createSubtask(Subtask subtask);

    //обновление задач
    void renewTask(Task task);

    void renewEpictask(Epictask epictask);

    void renewSubtask(Subtask subtask);

    //удаление по id
    void deleteTaskById(int id);

    void deleteEpictaskById(int id);

    void deleteSubtaskById(int id);

    public ArrayList<Task> getHistory();
    //тест
}