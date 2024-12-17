package test;

import com.yandex.tracker.model.Epic;
import com.yandex.tracker.model.Subtask;
import com.yandex.tracker.model.Task;
import com.yandex.tracker.service.Managers;
import com.yandex.tracker.service.TaskManager;
import com.yandex.tracker.service.TaskStatus;
import com.yandex.tracker.service.HistoryManager;
import com.yandex.tracker.service.InMemoryTaskManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest {
    private TaskManager taskManager;
    private HistoryManager historyManager;

    @BeforeEach
    public void setUp() {
        historyManager = new InMemoryHistoryManager();
        taskManager = new InMemoryTaskManager();
    }

    @Test
    void shouldAddTaskAndRetrieveById() {
        Task task = new Task("Task 1", "Description 1", TaskStatus.NEW);
        taskManager.createTask(task);

        Task retrievedTask = taskManager.getTaskById(task.getId());
        assertNotNull(retrievedTask, "Task should be found by ID.");
        assertEquals(task, retrievedTask, "Retrieved task should be equal to the created task.");
    }

    @Test
    void shouldUpdateTask() {
        Task task = new Task("Task 1", "Description 1", TaskStatus.NEW);
        taskManager.createTask(task);

        task.setTitle("Updated Task 1");
        taskManager.updateTask(task);

        Task updatedTask = taskManager.getTaskById(task.getId());
        assertEquals("Updated Task 1", updatedTask.getTitle(), "Task title should be updated.");
    }

    @Test
    void shouldAddEpicAndRetrieveById() {
        Epic epic = new Epic("Epic 1", "Description 1");
        taskManager.createEpic(epic);

        Epic retrievedEpic = taskManager.getEpicById(epic.getId());
        assertNotNull(retrievedEpic, "Epic should be found by ID.");
        assertEquals(epic, retrievedEpic, "Retrieved epic should be equal to the created epic.");
    }

    @Test
    void shouldAddSubtaskToEpic() {
        Epic epic = new Epic("Epic 1", "Description 1");
        taskManager.createEpic(epic);

        Subtask subtask = new Subtask("Subtask 1", "Description 1", TaskStatus.NEW, epic.getId());
        taskManager.createSubtask(subtask);

        assertEquals(1, taskManager.getSubtasksOfEpic(epic.getId()).size(), "Epic should have one subtask.");
    }

    @Test
    void shouldMaintainHistory() {
        Task task = new Task("Task 1", "Description 1", TaskStatus.NEW);
        taskManager.createTask(task);

        // Access the task to add it to history
        taskManager.getTaskById(task.getId());

        assertEquals(1, taskManager.getHistory().size(), "History should contain one task.");
        assertEquals(task, taskManager.getHistory().get(0), "History should contain the correct task.");
    }

    @Test
    public void shouldLimitHistoryToTenTasks() {
        for (int i = 0; i < 12; i++) {
            Task task = new Task("Task " + i, "Description " + i, TaskStatus.NEW);
            taskManager.createTask(task);
            historyManager.add(task);
        }

        // Проверка на все добавленные задачи без ограничения
        assertEquals(12, historyManager.getHistory().size());
    }

    @Test
    public void deleteTaskById_shouldRemoveTaskAndHistory() {
        Task task = new Task("Task 1", "Description", TaskStatus.NEW);
        taskManager.createTask(task);
        historyManager.add(task);

        taskManager.deleteTaskById(task.getId());

        // Проверка, что задача удалена из менеджера задач
        assertFalse(taskManager.getTasks().containsKey(task.getId()));
        // Проверка, что задача удалена из истории
        assertFalse(historyManager.getHistory().contains(task));
    }

    @Test
    void deleteTaskById_shouldRemoveTaskAndHistory() {
        Task task = new Task("Test Task", "Test Description", TaskStatus.NEW);
        taskManager.createTask(task);
        int taskId = task.getId();

        // Add task to history
        taskManager.getTaskById(taskId);
        List<Task> historyBefore = historyManager.getHistory();
        assertEquals(1, historyBefore.size(), "History should contain the task before deletion.");

        // Delete the task
        taskManager.deleteTaskById(taskId);

        // Check task is removed from TaskManager
        assertNull(taskManager.getTaskById(taskId), "Task should be removed from TaskManager.");

        // Check history is updated
        List<Task> historyAfter = historyManager.getHistory();
        assertFalse(historyAfter.stream().anyMatch(t -> t.getId() == taskId),
                "Task should be removed from history after deletion.");
        assertEquals(0, historyAfter.size(), "History should be empty after task deletion.");
    }
}