package test;

import com.yandex.tracker.model.Task;
import com.yandex.tracker.service.InMemoryHistoryManager;
import com.yandex.tracker.service.TaskStatus;

class InMemoryHistoryManagerTest {
    public static void main(String[] args) {
        InMemoryHistoryManager historyManager = new InMemoryHistoryManager();

        // Создание задач
        Task task1 = new Task("Task 1", "Description for Task 1", TaskStatus.NEW);
        Task task2 = new Task("Task 2", "Description for Task 2", TaskStatus.IN_PROGRESS);
        Task task3 = new Task("Task 3", "Description for Task 3", TaskStatus.DONE);

        // Добавление задач
        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task3);

        // Вывод истории после добавления
        System.out.println("History after adding tasks:");
        historyManager.getHistory().forEach(task -> System.out.println(task.getTitle()));

        // Удаление задачи
        historyManager.remove(task2.getId());
        System.out.println("History after removing Task 2:");
        historyManager.getHistory().forEach(task -> System.out.println(task.getTitle()));

        // Повторное добавление задачи
        historyManager.add(task2);
        System.out.println("History after re-adding Task 2:");
        historyManager.getHistory().forEach(task -> System.out.println(task.getTitle()));
    }
}