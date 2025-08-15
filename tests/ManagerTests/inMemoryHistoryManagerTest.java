package ManagerTests;
import tasks.*;
import manager.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import java.util.ArrayList;

public class inMemoryHistoryManagerTest {
    inMemoryTaskManager taskmanager = new inMemoryTaskManager();
    inMemoryHistoryManager historyManager = new inMemoryHistoryManager();
    @Test
    void shouldReturnEqualListsAfterTaskStatusChanging(){
        ArrayList<Task> chekList = new ArrayList<>();
        Task task = new Task(1, "task Name", "task Details", Status.NEW);
        taskmanager.createTask(task);
        taskmanager.getTaskById(1);


        Task task1 = new Task(1, "task Name", "task Details2", Status.IN_POGRESS);
        taskmanager.renewTask(task1);
        taskmanager.getTaskById(1);
        chekList.add(task1);

        Assertions.assertArrayEquals(taskmanager.getHistory().toArray(), chekList.toArray(), "Списки не равны");
    }

    @Test
    void shouldReturnEmptyListAfterTaskRemoveFromHistory(){
        ArrayList<Task> historyOfTasks = new ArrayList<>();
        Task task = new Task(1, "testTask Name", "task Details", Status.NEW);
        inMemoryTaskManager manager = new inMemoryTaskManager();
        manager.createTask(task);
        manager.getTaskById(1);
        historyOfTasks = manager.getHistory();

        manager.clearTasks();

        Assertions.assertTrue(manager.getHistory().isEmpty(), "Список не пустой");

    }
}
