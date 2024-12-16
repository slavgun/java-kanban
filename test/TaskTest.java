package test;

import com.yandex.tracker.model.Task;
import com.yandex.tracker.service.TaskStatus;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TaskTest {

    @Test
    void taskInstancesShouldBeEqualIfIdsAreEqual() {
        Task task1 = new Task("Task 1", "Description 1", TaskStatus.NEW);
        Task task2 = new Task("Task 1", "Description 1", TaskStatus.NEW);
        task1.setId(1);
        task2.setId(1);
        assertEquals(task1, task2, "Tasks should be equal if their IDs are the same.");
    }

    @Test
    void taskShouldRetainDataAfterCreation() {
        Task task = new Task("Task 1", "Description 1", TaskStatus.NEW);
        assertEquals("Task 1", task.getTitle(), "Task title should be 'Task 1'.");
        assertEquals("Description 1", task.getDescription(), "Task description should be 'Description 1'.");
        assertEquals(TaskStatus.NEW, task.getStatus(), "Task status should be NEW.");
    }
}