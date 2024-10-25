package com.yandex.tracker;

import com.yandex.tracker.model.*;
import com.yandex.tracker.service.TaskStatus;
import com.yandex.tracker.service.TaskManager;

public class Main {
    public static void main(String[] args) {
        TaskManager manager = new TaskManager();

        // Создание задач
        Task task1 = new Task("Задача 1", "Описание 1", TaskStatus.NEW);
        Task task2 = new Task("Задача 2", "Описание 2", TaskStatus.IN_PROGRESS);
        manager.createTask(task1);
        manager.createTask(task2);

        // Создание эпиков и подзадач
        Epic epic1 = new Epic("Эпик 1", "Описание эпика 1");
        manager.createEpic(epic1);
        Subtask subtask1 = new Subtask("Подзадача 1", "Описание подзадачи 1", TaskStatus.NEW, epic1.getId());
        Subtask subtask2 = new Subtask("Подзадача 2", "Описание подзадачи 2", TaskStatus.DONE, epic1.getId());
        manager.createSubtask(subtask1);
        manager.createSubtask(subtask2);

        Epic epic2 = new Epic("Эпик 2", "Описание эпика 2");
        manager.createEpic(epic2);
        Subtask subtask3 = new Subtask("Подзадача 3", "Описание подзадачи 3", TaskStatus.IN_PROGRESS, epic2.getId());
        manager.createSubtask(subtask3);

        // Вывод всех задач, эпиков и подзадач
        System.out.println("Задачи: " + manager.getAllTasks());
        System.out.println("Эпики: " + manager.getAllEpics());
        System.out.println("Подзадачи: " + manager.getAllSubtasks());

        // Изменение статусов и вывод
        task1.setStatus(TaskStatus.DONE);
        manager.updateTask(task1);
        subtask1.setStatus(TaskStatus.DONE);
        manager.updateSubtask(subtask1);

        System.out.println("Обновленные задачи: " + manager.getAllTasks());
        System.out.println("Обновленные эпики: " + manager.getAllEpics());
        System.out.println("Обновленные подзадачи: " + manager.getAllSubtasks());

        // Удаление задачи и эпика
        manager.deleteTaskById(task2.getId());
        manager.deleteEpicById(epic1.getId());

        System.out.println("После удаления - Задачи: " + manager.getAllTasks());
        System.out.println("После удаления - Эпики: " + manager.getAllEpics());
        System.out.println("После удаления - Подзадачи: " + manager.getAllSubtasks());
    }
}