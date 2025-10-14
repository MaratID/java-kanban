package main;
import java.io.IOException;
import manager.InMemoryTaskManager;
import manager.TaskManager;

public class Main {
    public static void main(String[] args) throws IOException {
        TaskManager taskManager = new InMemoryTaskManager();
        try {
            HttpTaskServer httpTaskServer = new HttpTaskServer(taskManager);
            httpTaskServer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}