
import com.yandex.tracker.model.Epic;
import com.yandex.tracker.model.Subtask;
import com.yandex.tracker.service.InMemoryTaskManager;
import com.yandex.tracker.service.TaskStatus;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class EpicTest {

    @Test
    void epicShouldNotAddItselfAsSubtask() {
        Epic epic = new Epic("Epic 1", "Description 1");
        epic.setId(1); // Устанавливаем ID
        assertThrows(IllegalArgumentException.class, () -> {
            epic.addSubtaskId(1); // Добавляем себя как подзадачу
        });
    }


    @Test
    void epicShouldRetainDataAfterCreation() {
        Epic epic = new Epic("Epic 1", "Description 1");
        assertEquals("Epic 1", epic.getTitle(), "Epic title should be 'Epic 1'.");
        assertEquals("Description 1", epic.getDescription(), "Epic description should be 'Description 1'.");
        assertEquals(TaskStatus.NEW, epic.getStatus(), "Epic status should be NEW.");
    }

    @Test
    void epicStatusShouldBeNewIfAllSubtasksAreNew() {
        Epic epic = new Epic("Epic 1", "Description 1");
        epic.setId(1);

        Subtask subtask1 = new Subtask("Subtask 1", "Description 1", TaskStatus.NEW, epic.getId());
        subtask1.setId(2);

        Subtask subtask2 = new Subtask("Subtask 2", "Description 2", TaskStatus.NEW, epic.getId());
        subtask2.setId(3);

        epic.addSubtaskId(subtask1.getId());
        epic.addSubtaskId(subtask2.getId());

        // Проверка статуса эпика
        assertEquals(TaskStatus.NEW, epic.getStatus(), "Epic status should be NEW if all subtasks are NEW.");
    }

    @Test
    void epicStatusShouldBeDoneIfAllSubtasksAreDone() {
        InMemoryTaskManager taskManager = new InMemoryTaskManager(); // Инициализация TaskManager

        Epic epic = new Epic("Epic 1", "Description 1");
        taskManager.createEpic(epic); // Добавляем эпик в TaskManager

        Subtask subtask1 = new Subtask("Subtask 1", "Description 1", TaskStatus.DONE, epic.getId());
        Subtask subtask2 = new Subtask("Subtask 2", "Description 2", TaskStatus.DONE, epic.getId());

        taskManager.createSubtask(subtask1); // Добавляем первую подзадачу
        taskManager.createSubtask(subtask2); // Добавляем вторую подзадачу

        // Получаем актуальный эпик из TaskManager
        Epic updatedEpic = taskManager.getEpicById(epic.getId());

        // Проверка статуса эпика
        assertEquals(TaskStatus.DONE, updatedEpic.getStatus(), "Epic status should be DONE if all subtasks are DONE.");
    }

    @Test
    void epicStatusShouldBeInProgressIfSubtasksAreMixed() {
        Epic epic = new Epic("Epic 1", "Description 1");
        epic.setId(1);

        Subtask subtask1 = new Subtask("Subtask 1", "Description 1", TaskStatus.NEW, epic.getId());
        subtask1.setId(2);

        Subtask subtask2 = new Subtask("Subtask 2", "Description 2", TaskStatus.DONE, epic.getId());
        subtask2.setId(3);

        epic.addSubtaskId(subtask1.getId());
        epic.addSubtaskId(subtask2.getId());

        // Провер

    }
}