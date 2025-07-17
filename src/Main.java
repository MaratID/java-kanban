

public class Main {

    public static void main(String[] args) {

        System.out.println("Поехали!");

        TaskManager taskManager = new TaskManager();


        Task task1 = new Task("Задача 1", "Описание задачи 1", Status.NEW);
        taskManager.createTask(task1);


        Task task2 = new Task("Задача 2", "Описание задачи 2", Status.IN_POGRESS);
        taskManager.createTask(task2);

        Epictask epictask1 = new Epictask("Эпик1", "Описание эпика 1");
        Epictask epictask2 = new Epictask("Эпик2", "Описание эпика 2");

        taskManager.createEpicTask(epictask1);
        taskManager.createEpicTask(epictask2);

        Subtask subtask1 = new Subtask("Subtask1", "Details_Subtask1", Status.NEW, 1); //проверить номера
        Subtask subtask2 = new Subtask("Subtask2", "Details_Subtask2", Status.IN_POGRESS, 1);
        Subtask subtask3 = new Subtask("Subtask3", "Details_Subtask3", Status.DONE, 2);

        taskManager.createSubtask(subtask1);
        taskManager.createSubtask(subtask2);
        taskManager.createSubtask(subtask3);

        //добавить проверяющие методы

        System.out.println(task1);
        System.out.println(task2);
        System.out.println(epictask1);
        System.out.println(epictask2);
        System.out.println(subtask1);
        System.out.println(subtask2);
        System.out.println(subtask3);
    }
}
