package ManagerTests;
import tasks.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

class SubtaskTest {


    @Test
    void shoultReturnTrueForSubtaskWithSameID() {
        Subtask subtask1 = new Subtask(2, "task1", "task1Details", Status.NEW, 2);
        Subtask subtask2 = new Subtask(2, "task1", "task1Details", Status.IN_POGRESS, 2);

        Assertions.assertTrue(subtask1.equals(subtask2), "Не прошло");
    }
}
