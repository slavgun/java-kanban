package test;

import com.yandex.tracker.model.Epic;
import com.yandex.tracker.model.Subtask;
import com.yandex.tracker.service.TaskStatus;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class EpicTest {

    @Test
    void epicShouldNotAddItselfAsSubtask() {
        Epic epic = new Epic("Epic 1", "Description 1");
        epic.setId(1); // Устанавливаем ID для эпика

        assertThrows(IllegalArgumentException.class, () -> {
            Subtask subtask = new Subtask("Subtask 1", "Description 1", TaskStatus.NEW, epic.getId());
            subtask.setEpicId(epic.getId()); // Попытка сделать эпик своей подзадачей
        }, "Epic cannot be added as its own subtask.");
    }

    @Test
    void epicShouldRetainDataAfterCreation() {
        Epic epic = new Epic("Epic 1", "Description 1");
        assertEquals("Epic 1", epic.getTitle(), "Epic title should be 'Epic 1'.");
        assertEquals("Description 1", epic.getDescription(), "Epic description should be 'Description 1'.");
        assertEquals(TaskStatus.NEW, epic.getStatus(), "Epic status should be NEW.");
    }
}