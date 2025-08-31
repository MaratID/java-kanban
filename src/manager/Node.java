package manager;
import tasks.Task;

public class Node {

    Task task;
    Node prev;
    Node next;

    public Node(Task task, Node prev, Node next) {
        this.task = task;
        this.prev = prev;
        this.next = next;
    }

    @Override
    public String toString() {
        return String.format("{task=%s, next=%s}",
                this.task,
                this.next);
    }
}