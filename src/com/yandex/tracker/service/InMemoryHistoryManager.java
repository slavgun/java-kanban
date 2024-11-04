package com.yandex.tracker.service;

import com.yandex.tracker.model.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    private final List<Task> history = new ArrayList<>();

    @Override
    public void add(Task task) {
        if (history.size() >= 10) {
            history.remove(0); // Удаляем самый старый элемент
        }
        history.add(task);
    }

    @Override
    public List<Task> getHistory() {
        return history;
    }
}