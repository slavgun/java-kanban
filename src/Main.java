public class Main {
    public static void main(String[] args) {
        TaskManager manager = new TaskManager();

        // Создание задач
        Task task1 = new Task(manager.generateId(), "Task 1", "Description 1", TaskStatus.NEW);
        Task task2 = new Task(manager.generateId(), "Task 2", "Description 2", TaskStatus.IN_PROGRESS);
        manager.createTask(task1);
        manager.createTask(task2);

        // Создание эпиков и подзадач
        Epic epic1 = new Epic(manager.generateId(), "Epic 1", "Epic Description 1");
        Subtask subtask1 = new Subtask(manager.generateId(), "Subtask 1", "Subtask Description 1", TaskStatus.NEW, epic1.getId());
        Subtask subtask2 = new Subtask(manager.generateId(), "Subtask 2", "Subtask Description 2", TaskStatus.DONE, epic1.getId());
        manager.createEpic(epic1);
        manager.createSubtask(subtask1);
        manager.createSubtask(subtask2);

        Epic epic2 = new Epic(manager.generateId(), "Epic 2", "Epic Description 2");
        Subtask subtask3 = new Subtask(manager.generateId(), "Subtask 3", "Subtask Description 3", TaskStatus.IN_PROGRESS, epic2.getId());
        manager.createEpic(epic2);
        manager.createSubtask(subtask3);

        // Печать всех задач, эпиков и подзадач
        System.out.println("Tasks: " + manager.getAllTasks());
        System.out.println("Epics: " + manager.getAllEpics());
        System.out.println("Subtasks: " + manager.getAllSubtasks());

        // Изменение статусов и печать
        task1.setStatus(TaskStatus.DONE);
        manager.updateTask(task1);
        subtask1.setStatus(TaskStatus.DONE);
        manager.updateSubtask(subtask1);

        System.out.println("Updated Tasks: " + manager.getAllTasks());
        System.out.println("Updated Epics: " + manager.getAllEpics());
        System.out.println("Updated Subtasks: " + manager.getAllSubtasks());

        // Удаление задачи и эпика
        manager.deleteTaskById(task2.getId());
        manager.deleteEpicById(epic1.getId());

        System.out.println("After Deletion - Tasks: " + manager.getAllTasks());
        System.out.println("After Deletion - Epics: " + manager.getAllEpics());
        System.out.println("After Deletion - Subtasks: " + manager.getAllSubtasks());
    }
}