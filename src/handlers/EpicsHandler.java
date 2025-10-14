package handlers;
import com.sun.net.httpserver.HttpExchange;
import manager.TaskManager;
import tasks.Epictask;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Collectors;

public class EpicsHandler extends BaseHttpHandler {
    private final TaskManager taskManager;

    public EpicsHandler(TaskManager taskManager) {
        super();
        this.taskManager = taskManager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        // Обработка запроса на задачи (GET, POST, DELETE)
        String method = exchange.getRequestMethod();
        switch (method) {
            case "GET":
                getEpics(exchange);
                break;
            case "POST":
                handlePost(exchange);
                break;
            case "DELETE":
                deleteEpic(exchange);
                break;
            default:
                sendNotFound(exchange);
        }
    }

    private void getEpics(HttpExchange exchange)throws IOException { //добавить про субтаски
        String[] path = exchange.getRequestURI().getPath().substring(1).split("/");
        if (path.length == 1 && path[0].equals("epics")) {
            //проверка на пустоту списка задач
            try  {
                String list = taskManager.getEpicTaskList().toString();
                sendText(exchange, list, 200);
            } catch (NullPointerException e) {
                sendText(exchange, "Список задач пуст", 400);
            }
        } else if (path.length == 2 && isNumeric(path[1])) {
            try {
                //вернуть задачу по id
                String epic = taskManager.getEpictaskById(Integer.parseInt(path[1])).toString();
                sendText(exchange, epic, 200);
            } catch (NullPointerException e) {
                sendNotFound(exchange);
            }
        } else if (path.length == 3 && isNumeric(path[1]) && path[2].equals("subtasks")) {
            try {
                Epictask epic = taskManager.getEpictaskById(Integer.parseInt(path[1]));
                ArrayList<Integer> subIds = epic.getSubtasksIds();
                ArrayList<String> subtasks = subIds.stream()
                        .map(id -> taskManager.getSubtaskById(id).toString())
                        .collect(Collectors.toCollection(ArrayList::new));
                sendText(exchange, subtasks.toString(), 200);
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
        Epictask epictask = gson.fromJson(body.get(), Epictask.class);
        try  {
            if (!taskManager.getEpicTaskList().isEmpty()) {
                taskManager.renewEpictask(epictask);
                sendText(exchange, "Задача обновлена", 201);
            } else {
                taskManager.createEpicTask(epictask);
                sendText(exchange, "Задача создана", 201);
            }
        } catch (RuntimeException e) {
            sendHasOverlaps(exchange);
        }
    }

    private void deleteEpic(HttpExchange exchange) throws IOException {
        String[] path = exchange.getRequestURI().getPath().substring(1).split("/");
        Optional<String> body =  parseBody(exchange.getRequestBody());
        if (body.isEmpty()) {
            sendText(exchange, "Тело запроса пустое", 400);
            return;
        }
        Epictask epictask = taskManager.getEpictaskById(Integer.parseInt(path[1]));
        if (taskManager.getEpicTaskList().contains(epictask)) {
            taskManager.deleteEpictaskById(epictask.getTaskId());
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