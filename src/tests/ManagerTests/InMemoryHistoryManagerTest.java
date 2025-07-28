package tests.ManagerTests;
import tasks.*;
import Manager.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import java.util.ArrayList;

public class InMemoryHistoryManagerTest {
    InMemoryTaskManager taskmanager = new InMemoryTaskManager();
    InMemoryHistoryManager historyManager = new InMemoryHistoryManager();
    @Test
    void shouldReturnEqualListsAfterTaskStatusChanging(){
        ArrayList<Task> chekList = new ArrayList<>();
        Task task = new Task(1, "task Name", "task Details", Status.NEW);
        taskmanager.createTask(task);
        taskmanager.getTaskById(1);
        chekList.add(task);
        Task task1 = new Task(1, "task Name", "task Details2", Status.IN_POGRESS);
        taskmanager.renewTask(task);
        taskmanager.getTaskById(1);
        chekList.add(task);

        Assertions.assertArrayEquals(taskmanager.getHistory().toArray(), chekList.toArray(), "Списки не равны");
    }
}
