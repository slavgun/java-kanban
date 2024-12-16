
package com.yandex.tracker.model;

import com.yandex.tracker.service.TaskStatus;

public class Subtask extends Task {
    private int epicId;

    public Subtask(String title, String description, TaskStatus status, int epicId) {
        super(title, description, status);
        Integer id = this.getId();
        System.out.println("Subtask ID: " + id + ", Epic ID: " + epicId);
        if (id != null && id.equals(epicId)) {
            System.out.println("Throwing exception: Subtask cannot be its own epic.");
            throw new IllegalArgumentException("Subtask cannot be its own epic.");
        }
        this.epicId = epicId;
    }


    public int getEpicId() {
        return epicId;
    }

    public void setEpicId(int epicId) {
        Integer id = this.getId();
        if (id != null && id.equals(epicId)) {
            throw new IllegalArgumentException("Subtask cannot be its own epic.");
        }
        this.epicId = epicId;
    }

    @Override
    public String toString() {
        return "Subtask{" +
                "id=" + getId() +
                ", title='" + getTitle() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", status=" + getStatus() +
                ", epicId=" + epicId +
                '}';
    }
}
