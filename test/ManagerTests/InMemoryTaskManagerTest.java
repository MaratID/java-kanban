package ManagerTests;
import tasks.*;
import manager.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.TreeSet;


class InMemoryTaskManagerTest {

    InMemoryTaskManager taskManager = new InMemoryTaskManager();

   @Test
    void createTask() {
        Task task = new Task("Test addNewTask", "Test addNewTask description", Status.NEW,
                Duration.ofDays(1), LocalDateTime.now());
        taskManager.createTask(task);
        final int taskId = task.getTaskId();
        final Task savedTask = taskManager.getTaskById(taskId);
        Assertions.assertNotNull(savedTask, "Задача не найдена.");
        Assertions.assertEquals(task, savedTask, "Задачи не совпадают.");
        final ArrayList<Task> tasks = taskManager.getTaskList();
        Assertions.assertNotNull(tasks, "Задачи не возвращаются.");
        Assertions.assertEquals(1, tasks.size(), "Неверное количество задач.");
        Assertions.assertEquals(task, tasks.getFirst(), "Задачи не совпадают.");
    }

    @Test
    void ShouldNotCreateSubtask() {
        Epictask epictask = new Epictask(1, "epictask", "epictaskDetails", Duration.ofMinutes(1),
                LocalDateTime.of(2025, 9, 1, 0,0,0));
        taskManager.createEpicTask(epictask);
        Subtask subtask = new Subtask(2, "epictask", "epictaskDetails", Status.NEW, 1,
                Duration.ofMinutes(1), LocalDateTime.of(2025, 9, 1, 0,2,0));
        taskManager.createSubtask(subtask);
        int invalidEpicId = 3;
        Subtask subtask1 = new Subtask ("Подзадача 2", "Описание подзадачи 2",
                Status.NEW, invalidEpicId, Duration.ofMinutes(1), 
                LocalDateTime.of(2025, 9, 1, 0,10,0));
        //When/Then создаем подзадачу и ожидаем исключение
        Assertions.assertThrows(IllegalArgumentException.class, () -> taskManager.createSubtask(subtask1));
    }

    @Test
    void ShouldNotDoSubtaskItsEpic() {
        Epictask epictask = new Epictask(1, "epictask", "epictaskDetails", Duration.ofDays(1),
                LocalDateTime.now());
        taskManager.createEpicTask(epictask);
        Subtask subtask = new Subtask(1, "subtask1", "subtask1details", Status.NEW, 1,
                Duration.ofDays(1), LocalDateTime.now());
        Assertions.assertThrows(IllegalArgumentException.class, ()-> taskManager.createSubtask(subtask));
    }

    @Test
    void shouldReturnEqualForDifferentTaskType() {
        Task task = new Task("Test task", "Test Task details", Duration.ofMinutes(1),
                LocalDateTime.of(2025, 9,16,0,0,0));
        taskManager.createTask(task);
        Epictask epic = new Epictask("Test Epictask", "Test Epictask details", Duration.ofMinutes(1),
                LocalDateTime.of(2025, 9,15,0,0,0));
        taskManager.createEpicTask(epic);
        Subtask subtask = new Subtask("Test SubtaskInepic", "Test SubtaskInepic details", Status.NEW,
                epic.getTaskId(), Duration.ofMinutes(1), 
                LocalDateTime.of(2025,9,16,0,10,0));
        taskManager.createSubtask(subtask);
        Assertions.assertEquals(task, taskManager.getTaskById(task.getTaskId()), "Таски не равны");
        Assertions.assertEquals(epic, taskManager.getEpictaskById(epic.getTaskId()), "Эпики не равны");
        Assertions.assertEquals(subtask, taskManager.getSubtaskById(subtask.getTaskId()), "Субтаски не равны");
    }

    @Test
    void shouldReturnTwoTasksForGenerateAndGivenId() {
        Task task1 = new Task(1,"Test task1", "Test Task details",Duration.ofMinutes(1),
                LocalDateTime.of(2025, 9, 1, 0,0,0));
        Task task2 = new Task("Test task2", "Test Task details", Duration.ofMinutes(1),
                LocalDateTime.of(2025,9,1,0,10,0));
        Task task3 = new Task("Test task3", "Test Task details", Duration.ofMinutes(1),
                LocalDateTime.of(2025,9,1,0,15,0));
        ArrayList<Task> tasksChekList = new ArrayList<>();
        tasksChekList.add(task1);
        tasksChekList.add(task2);
        tasksChekList.add(task3);
        taskManager.createTask(task1);
        taskManager.createTask(task2);
        taskManager.createTask(task3);
        Assertions.assertArrayEquals(tasksChekList.toArray(), taskManager.getTaskList().toArray(),
                "Списки Тасков не равны");
    }

    @Test
    void shouldNotCnahgeTaskFieldsAfterTaskManager() {
        Task task = new Task(1,"Test task1", "Test Task details", Status.NEW, Duration.ofDays(1),
                LocalDateTime.now());
        taskManager.createTask(task);
        ArrayList<Task> list = taskManager.getTaskList();
        Assertions.assertEquals(task.toString(), list.getFirst().toString(), "Поля Задачи не равны");
    }

    @Test
    void shouldNotStayUnactualSubtasksinEpictasks() {
        InMemoryTaskManager manager = new InMemoryTaskManager();
        manager.createEpicTask(new Epictask("Epicname1", "Epicname1 details", Duration.ofMinutes(1),
                LocalDateTime.of(2025, 9, 1, 0,0,0)));
        manager.createSubtask(new Subtask(2,"Subtask1Name", "Subtask1 Details", Status.NEW,
            manager.getEpicTaskList().getFirst().getTaskId(), Duration.ofMinutes(1), 
                LocalDateTime.of(2025, 9, 1, 0,10,0)));
        //заменяем статус Субтаска внутри эпика на IN_POGRESS
        Subtask subtask = manager.getSubtaskById(2);
        subtask.setStatus(Status.IN_POGRESS);
        manager.renewSubtask(subtask);
        Assertions.assertEquals(manager.getEpicTaskList().getFirst().getStatus(), Status.IN_POGRESS,
                "Статусы не совпали");
    }

    @Test
    void shouldReturnTrueForNotOverLappedTasks() {
        Task testTask1 = new Task("Проснуться", "Детали просыпания", Status.NEW, Duration.ofMinutes(1),
                LocalDateTime.of(2025, 9,14,8,0,0));
        Task testTask2 = new Task("Умыться", "Детали умывания", Status.NEW, Duration.ofMinutes(5),
                LocalDateTime.of(2025, 9,14,8,3,0));
        taskManager.createTask(testTask1);
        taskManager.createTask(testTask2);
        TreeSet<Task> testSet = new TreeSet<>(Comparator.comparing(Task::getTaskStartTime));
        testSet.add(testTask1);
        testSet.add(testTask2);

        TreeSet<Task> pT = taskManager.prioritizedTasks;
        Assertions.assertEquals(testSet,pT,"Списки не совпадают");
    }

    @Test
    void shouldReturnEqualSetForNotOverlappedEpicks() {
        Epictask e1 = new Epictask("Epick1", "Epick1Details", Duration.ofMinutes(1),
                LocalDateTime.of(2025, 9,14,0,0,0));
        Epictask e2 = new Epictask("Epick2", "Epick2Details", Duration.ofMinutes(1),
                LocalDateTime.of(2025, 9,14,3,0,0));

        taskManager.createEpicTask(e1);
        taskManager.createEpicTask(e2);
        ArrayList<Epictask> i = taskManager.getEpicTaskList();
        Subtask s1e1 = new Subtask("subEpick1", "subEpick1_Details", Status.NEW,
                i.getFirst().getTaskId(),Duration.ofMinutes(1),
                LocalDateTime.of(2025, 9,14,0,5,0));
        Subtask s2e2 = new Subtask("subEpick2", "subEpick2_Details", Status.NEW,
                i.get(1).getTaskId(),Duration.ofMinutes(1),
                LocalDateTime.of(2025, 9,14,3,5,0));
        taskManager.createSubtask(s1e1);
        taskManager.createSubtask(s2e2);
        TreeSet<Task> testSet = new TreeSet<>(Comparator.comparing(Task::getTaskStartTime));
        testSet.add(e1);
        testSet.add(e2);
        testSet.add(s1e1);
        testSet.add(s2e2);


        TreeSet<Task> pT = taskManager.prioritizedTasks;
        Assertions.assertEquals(testSet,pT,"Списки не совпадают");
    }
    //тест
}