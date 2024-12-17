package test;

import com.yandex.tracker.model.Epic;
import com.yandex.tracker.model.Subtask;
import com.yandex.tracker.model.Task;
import com.yandex.tracker.service.Managers;
import com.yandex.tracker.service.TaskManager;
import com.yandex.tracker.service.TaskStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest {
    private TaskManager taskManager;

    @BeforeEach
    public void setUp() {
        taskManager = Managers.getDefault();
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
    void shouldLimitHistoryToTenTasks() {
        for (int i = 1; i <= 12; i++) {
            Task task = new Task("Task " + i, "Description " + i, TaskStatus.NEW);
            taskManager.createTask(task);
            taskManager.getTaskById(task.getId()); // Accessing to add to history
        }

        assertEquals(10, taskManager.getHistory().size(), "History should contain only the last 10 tasks.");
    }

    @Test
    public void deleteTaskById_shouldRemoveTaskAndHistory() {
        int taskId = 1;
        Task task = new Task("Test Task", "Description");
        tasks.put(taskId, task);
        historyManager.add(task);

        deleteTaskById(taskId);

        assertFalse(tasks.containsKey(taskId));
        assertFalse(historyManager.contains(taskId));
    }

    @Test
    public void deleteEpicById_shouldRemoveEpicSubtasksAndHistory() {
        int epicId = 2;
        Epic epic = new Epic("Test Epic", "Description");
        int subtaskId = 3;
        Subtask subtask = new Subtask("Test Subtask", "Description", epicId);

        epics.put(epicId, epic);
        subtasks.put(subtaskId, subtask);
        epic.getSubtaskIds().add(subtaskId);
        historyManager.add(epic);
        historyManager.add(subtask);

        deleteEpicById(epicId);

        assertFalse(epics.containsKey(epicId));
        assertFalse(subtasks.containsKey(subtaskId));
        assertFalse(historyManager.contains(epicId));
        assertFalse(historyManager.contains(subtaskId));
    }

    @Test
    public void deleteSubtaskById_shouldRemoveSubtaskAndHistory() {
        int subtaskId = 4;
        Subtask subtask = new Subtask("Test Subtask", "Description", 2);
        subtasks.put(subtaskId, subtask);
        historyManager.add(subtask);

        deleteSubtaskById(subtaskId);

        assertFalse(subtasks.containsKey(subtaskId));
        assertFalse(historyManager.contains(subtaskId));
    }
}