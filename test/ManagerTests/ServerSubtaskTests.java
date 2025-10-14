package ManagerTests;
import com.google.gson.Gson;
import main.HttpTaskServer;
import manager.InMemoryTaskManager;
import manager.TaskManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epictask;
import tasks.Status;
import tasks.Subtask;
import tasks.Task;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


public class ServerSubtaskTests {
    TaskManager manager = new InMemoryTaskManager();
    HttpTaskServer taskServer = new HttpTaskServer(manager);
    Gson gson = HttpTaskServer.getGson();

    public ServerSubtaskTests() throws IOException {
    }

    @BeforeEach
    void startServer(){
        manager.clearTasks();
        manager.clearSubtasks();
        manager.clearEpics();
        taskServer.start();
    }

    @AfterEach
    void stopServer(){
        taskServer.stop();
    }

    @Test
    public void shouldReturnPrioritizedList() throws IOException, InterruptedException {
        Task task = new Task("Task1", "Det1", Duration.ofMinutes(1), LocalDateTime.now());
        String taskJson = gson.toJson(task);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks");
        URI priorUrl = URI.create("http://localhost:8080/prioritized");
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(taskJson)).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        HttpRequest priorRequest = HttpRequest.newBuilder().uri(priorUrl).GET().build();
        HttpResponse<String> priorResponse = client.send(priorRequest, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, priorResponse.statusCode());
    }

    @Test
    public void shouldReturnHistory() throws IOException, InterruptedException {
        Task task = new Task("Task1", "Det1", Duration.ofMinutes(1), LocalDateTime.now());
        String taskJson = gson.toJson(task);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks");
        URI urlID = URI.create("http://localhost:8080/tasks/1");
        URI historyUrl = URI.create("http://localhost:8080/history");
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(taskJson)).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        //Получение задачи
        HttpRequest request2 = HttpRequest.newBuilder().uri(urlID).GET().build();
        HttpResponse<String> getResponse = client.send(request2, HttpResponse.BodyHandlers.ofString());

        HttpRequest historyRequest = HttpRequest.newBuilder().uri(historyUrl).GET().build();
        HttpResponse<String> historyResponse = client.send(historyRequest, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, historyResponse.statusCode());
        ArrayList<Task> historyList = manager.getHistory();
        assertEquals("Task1", historyList.get(0).getName());

    }


    @Test
    public void shouldcreateEpicAndSubtask() throws IOException, InterruptedException {
        // создаём задачу
        Epictask epic = new Epictask("EpicName1", "EpicDetails1", Duration.ofMinutes(5), LocalDateTime.now());
        Subtask subtask = new Subtask("Subtest 1", "Testing subtask 1",
               Status.NEW, 1, Duration.ofMinutes(5), LocalDateTime.now());
        // конвертируем её в JSON
        String epicTaskJson = gson.toJson(epic);
        String subtaskJson = gson.toJson(subtask);

        // создаём HTTP-клиент и запрос
        HttpClient client = HttpClient.newHttpClient();
        URI urlSub = URI.create("http://localhost:8080/subtasks");
        URI urlEpic = URI.create("http://localhost:8080/epics");
        HttpRequest requestE = HttpRequest.newBuilder().uri(urlEpic).POST(HttpRequest.BodyPublishers.ofString(epicTaskJson)).build();
        HttpRequest requestS = HttpRequest.newBuilder().uri(urlSub).POST(HttpRequest.BodyPublishers.ofString(subtaskJson)).build();

        //нужно поразбираться почему я не могу привязаться к эпику!!!!!!!!!
        // вызываем рест, отвечающий за создание задач
        HttpResponse<String> responseE = client.send(requestE, HttpResponse.BodyHandlers.ofString());
        HttpResponse<String> responseS = client.send(requestS, HttpResponse.BodyHandlers.ofString());
        List<Epictask> epicsFromManager = manager.getEpicTaskList();
        // проверяем код ответа
        assertEquals(201, responseS.statusCode());
        assertEquals(201, responseE.statusCode());

        // проверяем, что создалась одна задача с корректным именем
        List<Subtask> subTasksFromManager = manager.getSubTaskList();

        assertNotNull(subTasksFromManager, "Задачи не возвращаются");
        assertEquals(1, subTasksFromManager.size(), "Некорректное количество задач");
        assertEquals("Subtest 1", subTasksFromManager.get(0).getName(), "Некорректное имя задачи");
    }

    @Test
    public void shouldcreateTaskAndGetTasks() throws IOException, InterruptedException {
        Task task = new Task("Test 2", "Testing task 2",
                Status.NEW, Duration.ofMinutes(5), LocalDateTime.now());
        // конвертируем её в JSON
        String taskJson = gson.toJson(task);

        // создаём HTTP-клиент и запрос
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks");
        URI urlForId = URI.create("http://localhost:8080/tasks/1");
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(taskJson)).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        //Получение задачи
        HttpRequest request2 = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> getResponse = client.send(request2, HttpResponse.BodyHandlers.ofString());
        List<Task> tasksFromManager = manager.getTaskList();
        //Получение задачи по id
        HttpRequest request3 = HttpRequest.newBuilder().uri(urlForId).GET().build();
        HttpResponse<String> getIdResponse = client.send(request3, HttpResponse.BodyHandlers.ofString());


        assertEquals(200, getResponse.statusCode());
        assertEquals(200, getIdResponse.statusCode());
    }

    @Test
    public void shouldDeleteTask() throws IOException, InterruptedException {
        Task task = new Task("Test 2", "Testing task 2",
                Status.NEW, Duration.ofMinutes(5), LocalDateTime.now());
        // конвертируем её в JSON
        String taskJson = gson.toJson(task);

        // создаём HTTP-клиент и запрос
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks");
        URI urlD = URI.create("http://localhost:8080/tasks/1");
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(taskJson)).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        List<Task> tasksFromManager = manager.getTaskList();

        HttpRequest requestDel = HttpRequest.newBuilder().uri(urlD).DELETE().build();
        HttpResponse<String> getDelResponse = client.send(requestDel, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, getDelResponse.statusCode());
    }
}