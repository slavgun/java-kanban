package com.yandex.tracker.service;
import java.util.Map;

import com.yandex.tracker.model.Task;
import com.yandex.tracker.model.Epic;
import com.yandex.tracker.model.Subtask;
import java.util.List;

public interface TaskManager {
    void createTask(Task task);

    void createEpic(Epic epic);

    void createSubtask(Subtask subtask);

    Task getTaskById(int id);

    Epic getEpicById(int id);

    Subtask getSubtaskById(int id);

    List<Task> getAllTasks();

    List<Epic> getAllEpics();

    List<Subtask> getAllSubtasks();

    void deleteTaskById(int id);

    void deleteEpicById(int id);

    void deleteSubtaskById(int id);

    void updateTask(Task task);// Добавлено

    void updateSubtask(Subtask subtask);// Добавлено

    List<Subtask> getSubtasksOfEpic(int epicId);// Добавлено

    List<Task> getHistory();

    Map<Integer, Task> getTasks();

    Map<Integer, Epic> getEpics();

    Map<Integer, Subtask> getSubtasks();
}
