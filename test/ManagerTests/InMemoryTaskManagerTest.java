package ManagerTests;
import tasks.*;
import manager.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import java.util.ArrayList;

class InMemoryTaskManagerTest {

    InMemoryTaskManager taskManager = new InMemoryTaskManager();

    @Test
    void createTask() {
        Task task = new Task("Test addNewTask", "Test addNewTask description", Status.NEW);
        taskManager.createTask(task);
        final int taskId = task.getTaskId();
        final Task savedTask = taskManager.getTaskById(taskId);
        Assertions.assertNotNull(savedTask, "Задача не найдена.");
        Assertions.assertEquals(task, savedTask, "Задачи не совпадают.");
        final ArrayList<Task> tasks = taskManager.getTaskList();
        Assertions.assertNotNull(tasks, "Задачи не возвращаются.");
        Assertions.assertEquals(1, tasks.size(), "Неверное количество задач.");
        Assertions.assertEquals(task, tasks.get(0), "Задачи не совпадают.");
    }

    @Test
    void ShouldNotCreateSubtask() {
        Epictask epictask = new Epictask(1, "epictask", "epictaskDetails");
        taskManager.createEpicTask(epictask);
        Subtask subtask = new Subtask(2, "epictask", "epictaskDetails", Status.NEW, 1);
        taskManager.createSubtask(subtask);
        int invalidEpicId = 3;
        Subtask subtask1 = new Subtask (invalidEpicId, "Подзадача 2", "Описание подзадачи 2",
                Status.NEW, invalidEpicId);
        //When/Then создаем подзадачу и ожидаем исключение
        Assertions.assertThrows(IllegalArgumentException.class, () -> taskManager.createSubtask(subtask1));
    }

    @Test
    void ShouldNotDoSubtaskItsEpic() {
        Epictask epictask = new Epictask(1, "epictask", "epictaskDetails");
        taskManager.createEpicTask(epictask);
        Subtask subtask = new Subtask(1, "subtask1", "subtask1details", Status.NEW, 1);
        Assertions.assertThrows(IllegalArgumentException.class, ()-> taskManager.createSubtask(subtask));
    }

    @Test
    void shouldReturnEqualForDifferentTaskType() {
        Task task = new Task("Test task", "Test Task details");
        taskManager.createTask(task);
        Epictask epic = new Epictask("Test Epictask", "Test Epictask details");
        taskManager.createEpicTask(epic);
        Subtask subtask = new Subtask("Test SubtaskInepic", "Test SubtaskInepic details", Status.NEW, epic.getTaskId());
        taskManager.createSubtask(subtask);
        Assertions.assertEquals(task, taskManager.getTaskById(task.getTaskId()), "Таски не равны");
        Assertions.assertEquals(epic, taskManager.getEpictaskById(epic.getTaskId()), "Эпики не равны");
        Assertions.assertEquals(subtask, taskManager.getSubtaskById(subtask.getTaskId()), "Субтаски не равны");
    }

    @Test
    void shouldReturnTwoTasksForGenerateAndGivenId() {
        Task task1 = new Task(1,"Test task1", "Test Task details");
        Task task2 = new Task("Test task2", "Test Task details");
        Task task3 = new Task("Test task3", "Test Task details");
        ArrayList<Task> tasksChekList = new ArrayList<>();
        tasksChekList.add(task1);
        tasksChekList.add(task2);
        tasksChekList.add(task3);
        taskManager.createTask(task1);
        taskManager.createTask(task2);
        taskManager.createTask(task3);
        Assertions.assertArrayEquals(tasksChekList.toArray(), taskManager.getTaskList().toArray(), "Списки Тасков не равны");
    }

    @Test
    void shouldNotCnahgeTaskFieldsAfterTaskManager() {
        Task task = new Task(1,"Test task1", "Test Task details", Status.NEW);
        taskManager.createTask(task);
        ArrayList<Task> list = taskManager.getTaskList();
        Assertions.assertEquals(task.toString(), list.getFirst().toString(), "Поля Задачи не равны");
    }

    @Test
    void shouldNotStayUnactualSubtasksinEpictasks() {
        InMemoryTaskManager manager = new InMemoryTaskManager();
        manager.createEpicTask(new Epictask("Epicname1", "Epicname1 details"));
        manager.createSubtask(new Subtask(2,"Subtask1Name", "Subtask1 Details", Status.NEW,
            manager.getEpicTaskList().getFirst().getTaskId()));
        //заменяем статус Субтаска внутри эпика на IN_POGRESS
        Subtask subtask = manager.getSubtaskById(2);
        subtask.setStatus(Status.IN_POGRESS);
        manager.renewSubtask(subtask);
        Assertions.assertEquals(manager.getEpicTaskList().getFirst().getStatus(), Status.IN_POGRESS,
                "Статусы не совпали");
    }
}