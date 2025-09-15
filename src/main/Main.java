package main;
import manager.*;
import tasks.*;
import java.io.File;
import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager(new File("files/tasks.csv"));
        /*fileBackedTaskManager.createTask(new Task("Переехать на новую квартиру", "Детали главной задачи",
                Status.NEW));
        fileBackedTaskManager.createTask(new Task("Выпить бокал вина после переезда",
                "Детали винной задачи", Status.NEW));
        fileBackedTaskManager.createEpicTask(new Epictask("Погрузить вещи в машину",
                "Детали эпика по загрузке машины"));
        fileBackedTaskManager.createSubtask(new Subtask("Вызвать машину", "Детали подзадачи вызова машины",
                Status.NEW, 3));
        fileBackedTaskManager.createSubtask(new Subtask("Вынести вещи к машине", "Спустить вещи на лифте",
                Status.NEW, 3));
        fileBackedTaskManager.createSubtask(new Subtask("Погрузить машину",
                "Детали подзадачит по погрузке машины", Status.NEW, 3));
        fileBackedTaskManager.createEpicTask(new Epictask("Убраться в старой квартире",
                "Детали эпика по уборке старой квартиры"));*/
        FileBackedTaskManager fileBackedTaskManager1 =
                FileBackedTaskManager.loadFromFile(new File("files/tasks.csv"));
        fileBackedTaskManager1.getTaskById(2);
        //тест
    }
}