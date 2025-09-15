package ManagerTests;
import tasks.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import java.time.Duration;
import java.time.LocalDateTime;

class SubtaskTest {

    @Test
    void shoultReturnTrueForSubtaskWithSameID() {
        Subtask subtask1 = new Subtask(2, "task1", "task1Details", Status.NEW, 2, 
                Duration.ofMinutes(10), 
                LocalDateTime.of(2025, 9,11, 10,10,0));
        Subtask subtask2 = new Subtask(2, "task1", "task1Details", Status.IN_POGRESS, 2, 
                Duration.ofMinutes(10), 
                LocalDateTime.of(2025, 9,11, 10,10,0));
        Assertions.assertTrue(subtask1.equals(subtask2), "Не прошло");
    }
    //тест
}