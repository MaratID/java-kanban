package handlers;
import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import main.HttpTaskServer;
import manager.TaskManager;
import tasks.Task;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.stream.Collectors;

public class TasksHandler extends BaseHttpHandler {
    private final TaskManager taskManager;
    private final Gson gson = HttpTaskServer.getGson();

    public TasksHandler(TaskManager taskManager) {
        super();
        this.taskManager = taskManager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        // Обработка запроса на задачи (GET, POST, DELETE)
        String method = exchange.getRequestMethod();
        switch (method) {
            case "GET":
                getTasks(exchange);
                break;
            case "POST":
                handlePost(exchange);
                break;
            case "DELETE":
                deleteTask(exchange);
                break;
            default:
                sendNotFound(exchange);
        }
    }

    private void getTasks(HttpExchange exchange)throws IOException {
        String[] path = exchange.getRequestURI().getPath().substring(1).split("/");
        if (path.length == 1 && path[0].equals("tasks")) {
            //проверка на пустоту списка задач
            try  {
                String list = taskManager.getTaskList().toString();
                sendText(exchange, list, 200);
            } catch (NullPointerException e) {
                sendText(exchange, "Список задач пуст", 400);
            }
        } else if (isNumeric(path[1])) {
            try {
                //вернуть задачу по id
                String task = taskManager.getTaskById(Integer.parseInt(path[1])).toString();
                sendText(exchange, task, 200);
            } catch (NullPointerException e) {
                sendNotFound(exchange);
            }
        }
    }

    private void handlePost(HttpExchange exchange) throws IOException {
        Optional<String> body =  parseBody(exchange.getRequestBody());
        //проверка на пустоту тела запроса
        if (body.isEmpty()) {
            sendText(exchange, "Тело запроса пустое", 400);
            return;
        }
        Task task = gson.fromJson(body.get(), Task.class);       //getTaskFromBody(body.get());
        try  {
            if (!taskManager.getTaskList().isEmpty()) {
                taskManager.renewTask(task);
                sendText(exchange, "Задача обновлена", 201);
            } else {
                taskManager.createTask(task);
                sendText(exchange, "Задача создана", 201);
            }
        } catch (RuntimeException e) {
            sendHasOverlaps(exchange);
        }
    }

    private void deleteTask(HttpExchange exchange) throws IOException {
        String[] path = exchange.getRequestURI().getPath().substring(1).split("/");
        Optional<String> body =  parseBody(exchange.getRequestBody());
        if (body.equals("")) {
            sendText(exchange, "Тело запроса пустое", 400);
            return;
        }
        Task task = taskManager.getTaskById(Integer.parseInt(path[1]));
        if (taskManager.getTaskList().contains(task)) {
            taskManager.deleteTaskById(task.getTaskId());
            sendText(exchange, "Задача удалена", 200);
        } else {
            sendText(exchange, "Такая задача отсутствует в списке", 404);
        }
    }

    private Optional<String> parseBody(InputStream bodyInputStream) {
        String body = new BufferedReader(
                new InputStreamReader(bodyInputStream, StandardCharsets.UTF_8))
                .lines()
                .collect(Collectors.joining());
        return body.describeConstable();
    }

    private static boolean isNumeric(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /*private Task getTaskFromBody(String jsonString){
        JsonObject json = JsonParser.parseString(jsonString).getAsJsonObject();
        int id = json.get("id").getAsInt();
        String name = json.get("name").getAsString();
        String details = json.get("details").getAsString();
        int duration = json.get("duration").getAsInt();
        String startTime = json.get("startTime").getAsString();
        DateTimeFormatter form = DateTimeFormatter.ofPattern("yyyy-MM-dd:HH:mm:ss");
        return new Task(id, name, details, Duration.ofMinutes(duration),LocalDateTime.parse(startTime, form));
    }*/
}