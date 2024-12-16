package com.yandex.tracker.test;

import com.yandex.tracker.model.Task;
import com.yandex.tracker.service.HistoryManager;
import com.yandex.tracker.service.InMemoryHistoryManager;
import com.yandex.tracker.service.TaskStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {
    private HistoryManager historyManager;

    @BeforeEach
    public void setUp() {
        historyManager = new InMemoryHistoryManager();
    }

    @Test
    void historyManagerShouldSavePreviousVersionOfTask() {
        Task task = new Task("Task 1", "Description 1", TaskStatus.NEW);
        historyManager.add(task);
        assertEquals(1, historyManager.getHistory().size(), "History should contain one task.");
        assertEquals(task, historyManager.getHistory().get(0), "History should contain the correct task.");
    }

    @Test
    void historyShouldLimitToTenTasks() {
        for (int i = 1; i <= 12; i++) {
            Task task = new Task("Task " + i, "Description " + i, TaskStatus.NEW);
            historyManager.add(task);
        }
        assertEquals(10, historyManager.getHistory().size(), "History should contain only the last 10 tasks.");
    }
}