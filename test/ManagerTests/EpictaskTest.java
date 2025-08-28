package ManagerTests;
import tasks.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

class EpictaskTest {


    @Test
    void shoultReturnTrueForEpictasksWithSameID() {
        Epictask epictask1 = new Epictask(2,"task1", "task1Details");
        Epictask epictask2 = new Epictask(2,"task1", "task1Details");

        Assertions.assertTrue(epictask1.equals(epictask2), "Не прошло");
    }
}
