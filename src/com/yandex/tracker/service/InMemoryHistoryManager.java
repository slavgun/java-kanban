package com.yandex.tracker.service;

import com.yandex.tracker.model.Task;
import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {
    private static class Node {
        Task task;
        Node next;
        Node prev;

        Node(Task task) {
            this.task = task;
        }
    }

    private final LinkedHashMap<Integer, Node> historyMap = new LinkedHashMap<>();
    private Node head;
    private Node tail;

    @Override
    public void add(Task task) {
        System.out.println("Adding task to history: " + task);
        if (task == null) return;

        // Remove existing node if present
        if (historyMap.containsKey(task.getId())) {
            remove(task.getId());
        }

        // Create and link new node
        Node node = new Node(task);
        historyMap.put(task.getId(), node);
        linkLast(node);
    }

    @Override
    public void remove(int id) {
        System.out.println("Removing task with ID from history: " + id);
        Node node = historyMap.remove(id);
        if (node != null) {
            removeNode(node);
        }
    }

    @Override
    public List<Task> getHistory() {
        System.out.println("Fetching history...");
        List<Task> history = new ArrayList<>();
        Node current = head;
        while (current != null) {
            history.add(current.task);
            current = current.next;
        }
        return history;
    }

    private void linkLast(Node node) {
        if (tail != null) {
            tail.next = node;
            node.prev = tail;
        } else {
            head = node;
        }
        tail = node;
    }

    private void removeNode(Node node) {
        if (node.prev != null) {
            node.prev.next = node.next;
        } else {
            head = node.next;
        }

        if (node.next != null) {
            node.next.prev = node.prev;
        } else {
            tail = node.prev;
        }
    }
}
