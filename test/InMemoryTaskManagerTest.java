import com.yandex.tracker.model.Epic;
import com.yandex.tracker.model.Subtask;
import com.yandex.tracker.model.Task;
import com.yandex.tracker.service.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest {
    private TaskManager taskManager;

    @BeforeEach
    public void setUp() {
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
    void deleteTaskById_shouldRemoveTaskAndHistory() {
        Task task = new Task("Test Task", "Test Description", TaskStatus.NEW);
        taskManager.createTask(task);
        int taskId = task.getId();

        // Add task to history
        taskManager.getTaskById(taskId);
        List<Task> historyBefore = taskManager.getHistory();
        assertEquals(1, historyBefore.size(), "History should contain the task before deletion.");

        // Delete the task
        taskManager.deleteTaskById(taskId);

        // Check task is removed from TaskManager
        assertNull(taskManager.getTaskById(taskId), "Task should be removed from TaskManager.");

        // Check history is updated
        List<Task> historyAfter = taskManager.getHistory();
        assertFalse(historyAfter.stream().anyMatch(t -> t.getId() == taskId),
                "Task should be removed from history after deletion.");
        assertEquals(0, historyAfter.size(), "History should be empty after task deletion.");
    }

    @Test
    void shouldDeleteEpicAndItsSubtasks() {
        Epic epic = new Epic("Epic 1", "Description 1");
        taskManager.createEpic(epic);

        Subtask subtask1 = new Subtask("Subtask 1", "Description 1", TaskStatus.NEW, epic.getId());
        taskManager.createSubtask(subtask1);

        Subtask subtask2 = new Subtask("Subtask 2", "Description 2", TaskStatus.NEW, epic.getId());
        taskManager.createSubtask(subtask2);

        // Удаляем эпик
        taskManager.deleteEpicById(epic.getId());

        // Проверяем, что эпик и подзадачи удалены
        assertNull(taskManager.getEpicById(epic.getId()), "Epic should be removed.");
        assertNull(taskManager.getSubtaskById(subtask1.getId()), "Subtask 1 should be removed.");
        assertNull(taskManager.getSubtaskById(subtask2.getId()), "Subtask 2 should be removed.");
    }

    @Test
    void shouldDeleteSubtaskAndUpdateEpicStatus() {
        InMemoryTaskManager taskManager = new InMemoryTaskManager();

        // Создаем эпик
        Epic epic = new Epic("Epic 1", "Description");
        taskManager.createEpic(epic);

        // Создаем две подзадачи для эпика
        Subtask subtask1 = new Subtask("Subtask 1", "Description", TaskStatus.DONE, epic.getId());
        Subtask subtask2 = new Subtask("Subtask 2", "Description", TaskStatus.IN_PROGRESS, epic.getId());
        taskManager.createSubtask(subtask1);
        taskManager.createSubtask(subtask2);

        // Проверка начального статуса эпика
        taskManager.updateEpicStatus(epic.getId());
        System.out.println("Initial Epic Status: " + taskManager.getEpicById(epic.getId()).getStatus());
        assertEquals(TaskStatus.IN_PROGRESS, taskManager.getEpicById(epic.getId()).getStatus(),
                "Epic status should initially be IN_PROGRESS.");

        // Удаляем первую подзадачу
        taskManager.deleteSubtaskById(subtask1.getId());

        // Получаем обновленный эпик и проверяем статус
        Epic updatedEpic = taskManager.getEpicById(epic.getId());
        System.out.println("Final Epic Status: " + updatedEpic.getStatus());

        assertEquals(TaskStatus.IN_PROGRESS, updatedEpic.getStatus(),
                "Epic status should be IN_PROGRESS after deleting a subtask.");
    }
}