package ManagerTests;
import manager.FileBackedTaskManager;
import tasks.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

public class FileBackedTaskManagerTest {

    File tempFile = File.createTempFile("tasksTest", null);
    FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager(new File(String.valueOf(tempFile.toPath())));

    public FileBackedTaskManagerTest() throws IOException {
    }

    @Test
    void shoultReturnEqualTasksIds() throws IOException {
        Task task = new Task("Купить мешок цемента", "Марка цемента", Status.NEW);
        Task task1 = new Task("Купить мешок картошки", "Картошка краснодарская", Status.NEW);
        fileBackedTaskManager.createTask(task);
        Assertions.assertEquals(task,fileBackedTaskManager.getTaskById(task.getTaskId()), "Задачи не совпали");
    }

    @Test
    void shouldChekForFewTasksInFile() throws IOException {
        Task task = new Task("Купить мешок цемента", "Марка цемента", Status.NEW);
        Task task1 = new Task("Купить мешок картошки", "Картошка краснодарская", Status.NEW);
        fileBackedTaskManager.createTask(task);
        fileBackedTaskManager.createTask(task1);
        String archiveCSV = Files.readString(tempFile.toPath(), StandardCharsets.UTF_8);
        final String[] lines = archiveCSV.split(System.lineSeparator());
        Assertions.assertEquals(3, lines.length, "В файле иное количество задач");
    }

    @Test
    void shouldLoadFromFile() throws IOException {
        Task task = new Task("Купить мешок цемента", "Марка цемента", Status.NEW);
        fileBackedTaskManager.createTask(task);
        FileBackedTaskManager fileBackedTaskManagerTest =
                FileBackedTaskManager.loadFromFile(new File(String.valueOf(tempFile.toPath())));
        Assertions.assertEquals(fileBackedTaskManager.getTaskById(task.getTaskId()),
                fileBackedTaskManagerTest.getTaskById(task.getTaskId()), "Задачи не совпадают");
    }
    //тест
}