package ManagerTests;
import tasks.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import java.time.Duration;
import java.time.LocalDateTime;

class TaskTest {

    @Test
    void shoultReturnTrueForTasksWithSameID() {
        Task task1 = new Task(1, "task1","task1Details", Status.NEW, Duration.ofMinutes(10),
                LocalDateTime.of(2025, 9,11, 10,10,0));
        Task task2 = new Task(1, "task1","task1Details", Status.IN_POGRESS, Duration.ofMinutes(10),
                LocalDateTime.of(2025, 9,11, 10,10,0));
        Assertions.assertTrue(task1.equals(task2), "Не прошло");
    }
    //тест
}