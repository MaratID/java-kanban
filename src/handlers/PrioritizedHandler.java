package handlers;
import com.sun.net.httpserver.HttpExchange;
import manager.TaskManager;
import java.io.IOException;

public class PrioritizedHandler extends BaseHttpHandler {
    private final TaskManager taskManager;
    public PrioritizedHandler(TaskManager taskManager) {
        super();
        this.taskManager = taskManager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if ("GET".equals(exchange.getRequestMethod())) {
            String prioritized = taskManager.getPrioritizedTasks().toString();
            sendText(exchange, prioritized, 200);
        } else {
            sendNotFound(exchange);
        }
    }
}