package ManagerTests;
import manager.InMemoryTaskManager;
import tasks.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import java.time.Duration;
import java.time.LocalDateTime;


class EpictaskTest {

    @Test
    void shoultReturnTrueForEpictasksWithSameID() {
        Epictask epictask1 = new Epictask(2,"task1", "task1Details", Duration.ofSeconds(1), LocalDateTime.now());
        Epictask epictask2 = new Epictask(2,"task1", "task1Details", Duration.ofSeconds(1), LocalDateTime.now());
        Assertions.assertTrue(epictask1.equals(epictask2), "Не прошло");
    }

    @Test
    void shouldreturnNewStatusForEpickTask() {
        InMemoryTaskManager memoryManager = new InMemoryTaskManager();
        Epictask epictask = new Epictask("Завести машину", "Завести поворотом ключа", Duration.ofSeconds(2), LocalDateTime.now());
        memoryManager.createEpicTask(epictask);
        int id = memoryManager.epicTasks.entrySet().iterator().next().getKey();

        Subtask subtask1 = new Subtask("Поворот коюча", "Поодержать в конце поворота", Status.NEW, id,
                Duration.ofSeconds(2), LocalDateTime.now());
        Subtask subtask2 = new Subtask("Вращение стартера", "Машину немного потрясет", Status.NEW, id,
                Duration.ofSeconds(2), subtask1.getTaskStartTime().plusSeconds(2));

        memoryManager.createSubtask(subtask1);
        memoryManager.createSubtask(subtask2);
        Assertions.assertEquals(memoryManager.epicTasks.entrySet().iterator().next().getValue().getStatus(),
                Status.NEW, "Статусы разные");
    }

    @Test
    void shouldreturnDoneStatusForEpickTask() {
        InMemoryTaskManager memoryManager = new InMemoryTaskManager();
        Epictask epictask = new Epictask("Завести машину", "Завести поворотом ключа",
                Duration.ofSeconds(1), LocalDateTime.now());
        memoryManager.createEpicTask(epictask);
        int id = memoryManager.epicTasks.entrySet().iterator().next().getKey();

        Subtask subtask1 = new Subtask("Поворот коюча", "Поодержать в конце поворота", Status.DONE, id,
                Duration.ofSeconds(2), LocalDateTime.now());
        Subtask subtask2 = new Subtask("Вращение стартера", "Машину немного потрясет", Status.DONE, id,
                Duration.ofSeconds(2), subtask1.getTaskStartTime().plusSeconds(2));

        memoryManager.createSubtask(subtask1);
        memoryManager.createSubtask(subtask2);
        Assertions.assertEquals(memoryManager.epicTasks.entrySet().iterator().next().getValue().getStatus(),
                Status.DONE, "Статусы разные");
    }

    @Test
    void shouldreturnINPROGRESSStatusForEpickTask() {
        InMemoryTaskManager memoryManager = new InMemoryTaskManager();
        Epictask epictask = new Epictask("Завести машину", "Завести поворотом ключа",
                Duration.ofSeconds(1), LocalDateTime.now());
        memoryManager.createEpicTask(epictask);
        int id = memoryManager.epicTasks.entrySet().iterator().next().getKey();
        //NEW DONE
        Subtask subtask1 = new Subtask("Поворот коюча", "Поодержать в конце поворота", Status.DONE, id,
                Duration.ofSeconds(2), LocalDateTime.now());
        Subtask subtask2 = new Subtask("Вращение стартера", "Машину немного потрясет", Status.NEW, id,
                Duration.ofSeconds(2), subtask1.getTaskStartTime().plusSeconds(2));
        memoryManager.createSubtask(subtask1);
        memoryManager.createSubtask(subtask2);
        Status newDone = memoryManager.epicTasks.entrySet().iterator().next().getValue().getStatus();
        //In_Progress
        Subtask subtask3 = new Subtask("Поворот коюча", "Поодержать в конце поворота", Status.IN_POGRESS, id,
                Duration.ofSeconds(2), LocalDateTime.now());
        Subtask subtask4 = new Subtask("Вращение стартера", "Машину немного потрясет", Status.IN_POGRESS, id,
                Duration.ofSeconds(2), subtask1.getTaskStartTime().plusSeconds(3));
        memoryManager.createSubtask(subtask3);
        memoryManager.createSubtask(subtask4);
        Status inProgress = memoryManager.epicTasks.entrySet().iterator().next().getValue().getStatus();

        Assertions.assertEquals(newDone, Status.IN_POGRESS, "Статусы разные");
        Assertions.assertEquals(inProgress, Status.IN_POGRESS, "Статусы разные");
    }

    @Test
    void shouldGetLargestTimeDurationOfEpick() {
        InMemoryTaskManager memoryManager = new InMemoryTaskManager();
        Epictask epictask = new Epictask("Epic-test-Name", "Epic-test-Details",
                Duration.ofDays(1),
                LocalDateTime.of(2025,9,20,0,0,0));
        memoryManager.createEpicTask(epictask);
        int id = memoryManager.epicTasks.entrySet().iterator().next().getKey();
        Subtask subtask1 = new Subtask("Subtask1-test-Name", "Subtask1-test-Details", Status.NEW, id,
                Duration.ofHours(1), LocalDateTime.of(2025, 9,21,0,0,0));
        Subtask subtask2 = new Subtask("Subtask2-test-Name", "Subtask2-test-Details", Status.NEW, id,
                Duration.ofHours(2), LocalDateTime.of(2025, 9,21,6,0,0));
        memoryManager.createSubtask(subtask1);
        memoryManager.createSubtask(subtask2);
        Assertions.assertEquals(Duration.ofHours(8), epictask.getTaskDuration(),
                "Неверно рассчитана продолжительность");
    }
    //тест
}