package handlers;
import com.sun.net.httpserver.HttpExchange;
import manager.TaskManager;
import tasks.Subtask;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.stream.Collectors;

public class SubtasksHandler extends BaseHttpHandler {
    private final TaskManager taskManager;

    public SubtasksHandler(TaskManager taskManager) {
        super();
        this.taskManager = taskManager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        // Обработка запроса на задачи (GET, POST, DELETE)
        String method = exchange.getRequestMethod();
        switch (method) {
            case "GET":
                getSubtasks(exchange);
                break;
            case "POST":
                handlePost(exchange);
                break;
            case "DELETE":
                deleteSubtask(exchange);
                break;
            default:
                sendNotFound(exchange);
        }
    }

    private void getSubtasks(HttpExchange exchange)throws IOException {
        String[] path = exchange.getRequestURI().getPath().substring(1).split("/");
        if (path.length == 1 && path[0].equals("subtasks")) {
            //проверка на пустоту списка задач
            try  {
                String list = taskManager.getSubTaskList().toString();
                sendText(exchange, list, 200);
            } catch (NullPointerException e){
                sendText(exchange, "Список подзадач пуст", 400);
            }
        } else if (isNumeric(path[1])){
            try {
                //вернуть задачу по id
                String subtask = taskManager.getSubtaskById(Integer.parseInt(path[1])).toString();
                sendText(exchange, subtask, 200);
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
        Subtask subtask = gson.fromJson(body.get(), Subtask.class);
        try  {
            if (!taskManager.getSubTaskList().isEmpty()) {
                taskManager.renewSubtask(subtask);
                sendText(exchange, "Задача обновлена", 201);
            } else {
                taskManager.createSubtask(subtask);
                sendText(exchange, "Задача создана", 201);
            }
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        } catch (RuntimeException e) {
            sendHasOverlaps(exchange);
        }  {
            
        }
    }

    private void deleteSubtask(HttpExchange exchange) throws IOException {
        String[] path = exchange.getRequestURI().getPath().substring(1).split("/");
        Optional<String> body =  parseBody(exchange.getRequestBody());
        if (body.isEmpty()) {
            sendText(exchange, "Тело запроса пустое", 400);
            return;
        }
        Subtask subtask = taskManager.getSubtaskById(Integer.parseInt(path[1]));
        if (taskManager.getSubTaskList().contains(subtask)) {
            taskManager.deleteSubtaskById(subtask.getTaskId());
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
}