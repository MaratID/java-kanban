package ManagerTests;
import tasks.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

class TaskTest {
    
    @Test
    void shoultReturnTrueForTasksWithSameID(){
        Task task1 = new Task(1, "task1","task1Details", Status.NEW);
        Task task2 = new Task(1, "task1","task1Details", Status.IN_POGRESS);

        Assertions.assertTrue(task1.equals(task2), "Не прошло");
    }
}
