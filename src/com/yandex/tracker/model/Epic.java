
package com.yandex.tracker.model;

import com.yandex.tracker.service.TaskStatus;

import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {
    private List<Integer> subtaskIds;

    public Epic(String title, String description) {
        super(title, description, TaskStatus.NEW);
        this.subtaskIds = new ArrayList<>();
    }

    public List<Integer> getSubtaskIds() {
        return subtaskIds;
    }

    public void addSubtaskId(int subtaskId) {
        Integer id = this.getId();
        System.out.println("Epic ID: " + id + ", Subtask ID: " + subtaskId);
        if (id != null && id.equals(subtaskId)) {
            System.out.println("Throwing exception: Epic cannot add itself as a subtask.");
            throw new IllegalArgumentException("Epic cannot add itself as a subtask.");
        }
        subtaskIds.add(subtaskId);
    }


    public void removeSubtaskId(int subtaskId) {
        subtaskIds.remove((Integer) subtaskId);
    }

    @Override
    public String toString() {
        return "Epic{" +
                "id=" + getId() +
                ", title='" + getTitle() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", status=" + getStatus() +
                ", subtaskIds=" + subtaskIds +
                '}';
    }
}
