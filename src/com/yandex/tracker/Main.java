package com.yandex.tracker;

import com.yandex.tracker.model.Epic;
import com.yandex.tracker.model.Subtask;
import com.yandex.tracker.model.Task;
import com.yandex.tracker.service.Managers;
import com.yandex.tracker.service.TaskManager;
import com.yandex.tracker.service.TaskStatus;

public class Main {
    public static void main(String[] args) {
        TaskManager manager = Managers.getDefault();

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

        // Вывод всех задач и истории
        printAllTasks(manager);

        // Изменение статусов и вывод
        task1.setStatus(TaskStatus.DONE);
        manager.updateTask(task1);
        subtask1.setStatus(TaskStatus.DONE);
        manager.updateSubtask(subtask1);

        System.out.println("\nОбновленные задачи:");
        printAllTasks(manager);

        // Удаление задачи и эпика
        manager.deleteTaskById(task2.getId());
        manager.deleteEpicById(epic1.getId());

        System.out.println("\nПосле удаления:");
        printAllTasks(manager);
    }

    private static void printAllTasks(TaskManager manager) {
        System.out.println("Задачи:");
        for (Task task : manager.getAllTasks()) {
            System.out.println(task);
        }
        System.out.println("Эпики:");
        for (Epic epic : manager.getAllEpics()) {
            System.out.println(epic);
            for (Subtask subtask : manager.getSubtasksOfEpic(epic.getId())) {
                System.out.println("--> " + subtask);
            }
        }
        System.out.println("Подзадачи:");
        for (Subtask subtask : manager.getAllSubtasks()) {
            System.out.println(subtask);
        }

        System.out.println("История:");
        for (Task task : manager.getHistory()) {
            System.out.println(task);
        }
    }
}