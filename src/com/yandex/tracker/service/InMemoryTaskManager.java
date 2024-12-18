package com.yandex.tracker.service;

import com.yandex.tracker.model.Task;
import com.yandex.tracker.model.Epic;
import com.yandex.tracker.model.Subtask;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryTaskManager implements TaskManager {
    private final HashMap<Integer, Task> tasks = new HashMap<>();
    private final HashMap<Integer, Epic> epics = new HashMap<>();
    private final HashMap<Integer, Subtask> subtasks = new HashMap<>();
    private final HistoryManager historyManager = Managers.getDefaultHistory();
    private int idCounter = 0;

    // Приватный метод для генерации ID
    private int generateId() {
        return ++idCounter;
    }

    @Override
    public void createTask(Task task) {
        int id = generateId();
        task.setId(id);
        tasks.put(id, task);
        historyManager.add(task);
    }

    @Override
    public void createEpic(Epic epic) {
        int id = generateId();
        epic.setId(id);
        epics.put(id, epic);
    }

    @Override
    public void createSubtask(Subtask subtask) {
        subtask.setId(generateId());
        subtasks.put(subtask.getId(), subtask);

        Epic epic = epics.get(subtask.getEpicId());
        if (epic != null) {
            epic.addSubtaskId(subtask.getId());
            updateEpicStatus(epic.getId());
        }
    }

    public void updateEpicStatus(int epicId) {
        Epic epic = epics.get(epicId);
        if (epic == null) return;

        List<Subtask> epicSubtasks = getSubtasksOfEpic(epicId);

        if (epicSubtasks.isEmpty()) {
            epic.setStatus(TaskStatus.NEW);
        } else {
            boolean allDone = epicSubtasks.stream().allMatch(subtask -> subtask.getStatus() == TaskStatus.DONE);
            boolean allNew = epicSubtasks.stream().allMatch(subtask -> subtask.getStatus() == TaskStatus.NEW);

            if (allDone) {
                epic.setStatus(TaskStatus.DONE);
            } else if (allNew) {
                epic.setStatus(TaskStatus.NEW);
            } else {
                epic.setStatus(TaskStatus.IN_PROGRESS);
            }
        }
    }

    @Override
    public Map<Integer, Task> getTasks() {
        return tasks;
    }


    @Override
    public Task getTaskById(int id) {
        Task task = tasks.get(id);
        if (task != null) {
            historyManager.add(task); // Добавляем задачу в историю
        }
        return task;
    }

    @Override
    public Epic getEpicById(int id) {
        Epic epic = epics.get(id);
        if (epic != null) {
            historyManager.add(epic); // Добавляем эпик в историю
        }
        return epic;
    }

    @Override
    public Subtask getSubtaskById(int id) {
        Subtask subtask = subtasks.get(id);
        if (subtask != null) {
            historyManager.add(subtask); // Добавляем подзадачу в историю
        }
        return subtask;
    }

    @Override
    public Map<Integer, Epic> getEpics() {
        return epics;
    }

    @Override
    public List<Task> getAllTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public List<Epic> getAllEpics() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public List<Subtask> getAllSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    @Override
    public void deleteTaskById(int id) {
        if (tasks.containsKey(id)) {
            historyManager.remove(id); // Удаляем из истории
            tasks.remove(id);
        }
    }

    @Override
    public void deleteSubtaskById(int id) {
        Subtask subtask = subtasks.remove(id);
        if (subtask != null) {
            Epic epic = epics.get(subtask.getEpicId());
            if (epic != null) {
                epic.getSubtaskIds().remove(Integer.valueOf(id));
                updateEpicStatus(epic.getId());
            }
            historyManager.remove(id);
        }
    }


    @Override
    public Map<Integer, Subtask> getSubtasks() {
        return subtasks;
    }

    @Override
    public void deleteEpicById(int id) {
        if (epics.containsKey(id)) {
            // Удаляем все подзадачи эпика из истории и мапы
            for (int subtaskId : epics.get(id).getSubtaskIds()) {
                historyManager.remove(subtaskId); // Удаляем подзадачу из истории
                subtasks.remove(subtaskId);       // Удаляем подзадачу из мапы
            }

            // Удаляем сам эпик
            historyManager.remove(id); // Удаляем эпик из истории
            epics.remove(id);
        }
    }

    @Override
    public void updateTask(Task task) {
        if (tasks.containsKey(task.getId())) {
            tasks.put(task.getId(), task);
        }
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        if (subtasks.containsKey(subtask.getId())) {
            subtasks.put(subtask.getId(), subtask);
            updateEpicStatus(subtask.getEpicId());
        }
    }


    @Override
    public List<Subtask> getSubtasksOfEpic(int epicId) {
        Epic epic = epics.get(epicId);
        List<Subtask> result = new ArrayList<>();
        if (epic != null) {
            for (int subtaskId : epic.getSubtaskIds()) {
                result.add(subtasks.get(subtaskId));
            }
        }
        return result;
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }
}