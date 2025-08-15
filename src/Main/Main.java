package Main;
import manager.*;
import tasks.*;
import java.util.ArrayList;
public class Main {
    public static void main(String[] args) {
        //Опциональный пользовательский сценарий.
        InMemoryTaskManager manager = new InMemoryTaskManager();
        manager.createTask(new Task("Переехать на новую квартиру", "Детали главной задачи", Status.NEW));
        manager.createTask(new Task("Выпить бокал вина после переезда", "Детали винной задачи", Status.NEW));
        manager.createEpicTask(new Epictask("Погрузить вещи в машину", "Детали эпика по загрузке машины"));
        manager.createSubtask(new Subtask("Вызвать машину", "Детали подзадачи вызова машины", Status.NEW, 3));
        manager.createSubtask(new Subtask("Вынести вещи к машине", "Спустить вещи на лифте", Status.NEW, 3));
        manager.createSubtask(new Subtask("Погрузить машину", "Детали подзадачит по погрузке машины", Status.NEW, 3));
        manager.createEpicTask(new Epictask("Убраться в старой квартире", "Детали эпика по уборке старой квартиры"));
        ArrayList<Task> tasksCopies= new ArrayList<>();
        for (Task t : manager.getTaskList()){
            tasksCopies.add(new Task(t.getTaskId(), t.getName(), t.getDetails(), t.getStatus()));
        }
        for (Epictask e : manager.getEpicTaskList()){
            tasksCopies.add(new Epictask(e.getTaskId(), e.getName(), e.getDetails()));
        }
        for (Subtask s : manager.getSubTaskList()){
            tasksCopies.add(new Epictask(s.getTaskId(), s.getName(), s.getDetails()));
        }
        manager.getTaskById(2);
        manager.getTaskById(1);
        manager.getEpictaskById(7);
        manager.getEpictaskById(3);
        manager.getSubtaskById(4);
        manager.getSubtaskById(5);
        manager.getSubtaskById(6);
        manager.getSubtaskById(5);
        manager.getSubtaskById(5);
        manager.getSubtaskById(4);
        manager.getSubtaskById(6);
        manager.getEpictaskById(7);
        manager.getTaskById(2);
        System.out.println("История 1:");
        for (Task task : manager.getHistory()) {
            System.out.println(task);
        }
        manager.deleteTaskById(2);
        System.out.println("История 2:");
        for (Task task : manager.getHistory()) {
            System.out.println(task);
        }
        manager.deleteEpictaskById(3);
        System.out.println("История 3:");
        for (Task task : manager.getHistory()) {
            System.out.println(task);
        }
    }
        /*
        manager.createTask(new Task( "Переехать на новую квартиру", "Детали главной задачи", Status.NEW));
        manager.createEpicTask(new Epictask("Погрузить вещи в машину", "Детали эпика по загрузке машины"));
        manager.createEpicTask(new Epictask("Убраться в старой квартире", "Детали эпика по уборке старой квартиры"));
        manager.createSubtask(new Subtask("Вызвать машину", "Детали подзадачи вызова машины", Status.NEW,2));
        manager.createSubtask(new Subtask("Вынести вещи к машине", "Спустить вещи на лифте", Status.NEW,2));
        manager.createSubtask(new Subtask("Погрузить машину", "Детали подзадачит по погрузке машины", Status.NEW,2));
        manager.createSubtask(new Subtask("Помыть полы", "Детали мытья полов", Status.NEW, 3));
        manager.createSubtask(new Subtask("Помыть посуду", "Детали мытья посуды", Status.NEW, 3));
        manager.createSubtask(new Subtask("Вынести мусор", "Детали выноса мусора", Status.NEW, 3));
        manager.getTaskById(1);
        manager.getEpictaskById(2);
        manager.getSubtaskById(8);
        printAllTasks(manager);
        */
    /*static void printAllTasks(inMemoryTaskManager manager) {
        System.out.println("Задачи:");
        for (Task task : manager.getTaskList()) {
            System.out.println(task);
        }
        System.out.println("Эпики:");
        for (Task epic : manager.getEpicTaskList()) {
            System.out.println(epic);
            for (Task task : manager.getSubtasksByEpicTask(epic)) {
                System.out.println("--> " + task);
            }
        }
        System.out.println("Подзадачи:");
        for (Task subtask : manager.getSubTaskList()) {
            System.out.println(subtask);
        }
        System.out.println("История:");
        for (Task task : manager.getHistory()) {
            System.out.println(task);
        }
    }
    */
}