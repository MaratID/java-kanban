package handlers;
import com.sun.net.httpserver.HttpExchange;
import manager.TaskManager;
import java.io.IOException;

public class HistoryHandler extends BaseHttpHandler {
    private final TaskManager historyManager;

    public HistoryHandler(TaskManager historyManager) {
        super();
        this.historyManager = historyManager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        if ("GET".equals(exchange.getRequestMethod())) {

            String task = historyManager.getHistory().toString();
            sendText(exchange, task, 200);
        } else {
            sendNotFound(exchange);
        }
    }
}