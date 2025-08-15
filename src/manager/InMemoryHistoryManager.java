package manager;
import tasks.Task;
import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {
    private final Map<Integer, Node> nodesMap = new HashMap<>();
    private Node first;
    private Node last;

    @Override
    public void add(Task task) {
        if (task == null) {
            return;
        }
        final int id = task.getTaskId();
        removeNode(id);
        linkedLast(task);
        nodesMap.put(id, last);
    }

    @Override
    public ArrayList<Task> getHistory() {
        return getTasks();
    }

    @Override
    public void remove(int id) {
        removeNode(id);
    }
    public void linkedLast(Task task) {
        //создаем узел
        Node node = new Node(task, last, null);
        // если первый узел в списке ничего не содержит
        if (first == null) {
            first = node; //кладем в первый узел наш созданый узел
        } else {
            last.next = node; //приравниваем послений к созданному, т.к. он сейчас единственный в списке
        }
        last = node; //при условии, что узлы уже имеются, то последним делаем вновь созданный узел
    }

    public ArrayList<Task> getTasks() {
        ArrayList<Task> tasks = new ArrayList<>();
        Node fNode = first; // копируем первый узел во временный объект Node
        while (fNode != null) {
            tasks.add(fNode.task); //включаем все задачи по узлам в список задач
            fNode = fNode.next;
        }
        return tasks;
    }

    private void removeNode(int id) {
        final Node node = nodesMap.remove(id); //получаем объект узла по id
        if (node == null) {
            return;
        }
        final Node nodenext = node.next; //делаем следующим следующий за выбранным узел
        final Node nodeprev = node.prev; //делаем предыдущим предыдущий за выбранным узел

        if (nodeprev == null) { //если был исключен первый узел
            first = nodenext; //то новый первый становиться следующий за исключенным
        } else {
            nodeprev.next = nodenext; //иначе привязываем предшествующий с последующим от выбранного
        }

        if (nodenext == null) { //если был выбран последний узел
            last = nodeprev; //делаем последним предпоследний
        } else {
            nodenext.prev = nodeprev; //если был выбран узел в середине, то привязываем предыдущий и последующий узлы
        }

    }

}