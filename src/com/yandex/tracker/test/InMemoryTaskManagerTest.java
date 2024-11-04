package com.yandex.tracker.test;

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
    void inMemoryTaskManagerShouldAddTasksAndFindById() {
        Task task = new Task("Task 1", "Description 1", TaskStatus.NEW);
        taskManager.createTask(task);
        Task foundTask = taskManager.getTaskById(task.getId());
        assertNotNull(foundTask, "Task should be found by ID.");
        assertEquals(task, foundTask, "Found task should be equal to the created task.");
    }

    @Test
    void tasksWithSameIdShouldNotConflict() {
        Task task1 = new Task("Task 1", "Description 1", TaskStatus.NEW);
        Task task2 = new Task("Task 2", "Description 2", TaskStatus.NEW);
        taskManager.createTask(task1);
        taskManager.createTask(task2);
        assertNotEquals(task1.getId(), task2.getId(), "Tasks should have different IDs.");
    }

    @Test
    void taskShouldNotChangeWhenAddedToManager() {
        Task task = new Task("Task 1", "Description 1", TaskStatus.NEW);
        taskManager.createTask(task);
        int originalId = task.getId();
        String originalTitle = task.getTitle();
        String originalDescription = task.getDescription();
        Task savedTask = taskManager.getTaskById(originalId);
        assertEquals(originalTitle, savedTask.getTitle(), "Task title should remain the same.");
        assertEquals(originalDescription, savedTask.getDescription(), "Task description should remain the same.");
    }
}