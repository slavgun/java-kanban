
import com.yandex.tracker.model.Subtask;
import com.yandex.tracker.service.TaskStatus;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SubtaskTest {

    @Test
    void subtaskShouldNotBeItsOwnEpic() {
        Subtask subtask = new Subtask("Subtask 1", "Description 1", TaskStatus.NEW, 1);
        subtask.setId(1); // Устанавливаем ID
        assertThrows(IllegalArgumentException.class, () -> {
            subtask.setEpicId(1); // Пробуем установить себя как epicId
        });
    }

    @Test
    void subtaskShouldRetainDataAfterCreation() {
        Subtask subtask = new Subtask("Subtask 1", "Description 1", TaskStatus.NEW, 1);
        assertEquals("Subtask 1", subtask.getTitle(), "Subtask title should be 'Subtask 1'.");
        assertEquals("Description 1", subtask.getDescription(), "Subtask description should be 'Description 1'.");
        assertEquals(TaskStatus.NEW, subtask.getStatus(), "Subtask status should be NEW.");
    }
}