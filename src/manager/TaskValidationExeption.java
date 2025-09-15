package manager;
import java.io.IOException;

public class TaskValidationExeption extends Exception {
    public TaskValidationExeption(String message) {
        super(message);
    }
}