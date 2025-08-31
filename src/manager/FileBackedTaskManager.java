package manager;

import tasks.Epictask;
import tasks.Subtask;
import tasks.Task;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class FileBackedTaskManager extends InMemoryTaskManager {
    private final File file;

    public FileBackedTaskManager(File file) {
        this.file = file;
    }

    @Override
    public void createTask(Task task) {
        super.createTask(task);
        save();
    }

    @Override
    public void createEpicTask(Epictask epictask) {
        super.createEpicTask(epictask);
        save();
    }

    @Override
    public void createSubtask(Subtask subtask) {
        super.createSubtask(subtask);
        save();
    }

    @Override
    public Task getTaskById(int id) {
        final Task task = super.getTaskById(id);
        save();
        return task;
    }

    @Override
    public Epictask getEpictaskById(int id) {
        final Epictask epic = super.getEpictaskById(id);
        save();
        return epic;
    }

    @Override
    public Subtask getSubtaskById(int id) {
        final Subtask subtask = super.getSubtaskById(id);
        save();
        return subtask;
    }

    @Override
    public void clearTasks() {
        super.clearTasks();
        save();
    }

    @Override
    public void clearEpics() {
        super.clearEpics();
        save();
    }

    @Override
    public void clearSubtasks() {
        super.clearSubtasks();
        save();
    }

    @Override
    public void renewTask(Task task) {
        super.renewTask(task);
        save();
    }

    @Override
    public void renewEpictask(Epictask epictask) {
        super.renewEpictask(epictask);
        save();
    }

    @Override
    public void renewSubtask(Subtask subtask) {
        super.renewSubtask(subtask);
        save();
    }

    @Override
    public void deleteTaskById(int id) {
        super.deleteTaskById(id);
        save();
    }

    @Override
    public void deleteEpictaskById(int id) {
        super.deleteEpictaskById(id);
        save();
    }

    @Override
    public void deleteSubtaskById(int id) {
        super.deleteSubtaskById(id);
        save();
    }

    private void save() {
        try (BufferedWriter writer = Files.newBufferedWriter(file.toPath(), StandardCharsets.UTF_8)) {
            writer.write(CSVSaveManager.getheader());
            writer.newLine();

            for (Map.Entry<Integer, Task> entry : tasks.entrySet()) {
                final Task task = entry.getValue();
                writer.write(CSVSaveManager.toString(task));
                writer.newLine();
            }

            for (Map.Entry<Integer, Epictask> entry : epicTasks.entrySet()) {
                final Epictask epic = entry.getValue();
                writer.write(CSVSaveManager.toString(epic));
                writer.newLine();
            }

            for (Map.Entry<Integer, Subtask> entry : subtasks.entrySet()) {
                final Subtask subtask = entry.getValue();
                writer.write(CSVSaveManager.toString(subtask));
                writer.newLine();
            }

            writer.newLine();
        } catch (IOException e) {
            try {
                throw new ManagerSaveException("Не сохранено в файл: " + file.getName(), e);
            } catch (ManagerSaveException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    public static FileBackedTaskManager loadFromFile (File file) {
        FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager(file);
        try {
            final String archiveCSV = Files.readString(file.toPath(), StandardCharsets.UTF_8);
            final String[] lines = archiveCSV.split(System.lineSeparator());
            int generatorID = 0;
            List<Integer> history = Collections.emptyList();
            for (int i = 1; i < lines.length; i++) {
                String line = lines[i];
                if (line.isEmpty()) {
                    history.add(CSVSaveManager.historyFromString(lines[i + 1]));
                    break;
                }
                String[] subLines = line.split(",");
                final Task task = CSVSaveManager.fromString(line);
                final int id = task.getTaskId();
                if (id > generatorID) {
                    generatorID = id;
                }
                fileBackedTaskManager.createTask(task);
            }
            for (Subtask s : fileBackedTaskManager.getSubTaskList()) {
                final Subtask subtask = s;
                final Epictask epic = fileBackedTaskManager.getEpicTaskList().get(subtask.getEpicId());
                epic.addSubtaskIDs(subtask.getTaskId());
            }

            for (Integer taskID : history) {
                fileBackedTaskManager.historyManager.add(fileBackedTaskManager.getTaskById(taskID));
            }
            fileBackedTaskManager.generatorId = generatorID;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return fileBackedTaskManager;
    }

}