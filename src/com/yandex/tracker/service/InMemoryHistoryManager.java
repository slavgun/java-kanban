package com.yandex.tracker.service;

import com.yandex.tracker.model.Task;
import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {

    private final Map<Integer, Node> historyMap = new HashMap<>();
    private Node head;
    private Node tail;

    private static class Node {
        Task task;
        Node next;
        Node prev;

        public Node(Task task) {
            this.task = task;
        }
    }

    private void linkLast(Task task) {
        Node newNode = new Node(task);

        if (tail == null) {
            head = newNode;
            tail = newNode;
        } else {
            tail.next = newNode;
            newNode.prev = tail;
            tail = newNode;
        }
        historyMap.put(task.getId(), newNode);
        System.out.println("Added task to history: " + task.getId());
    }

    private void removeNode(Node node) {
        if (node == null || node.task == null) return; // Safety check

        // Log task before removing
        System.out.println("Removing task from history: " + node.task.getId());

        if (node.prev != null) {
            node.prev.next = node.next;
        } else {
            head = node.next; // Update head if necessary
        }

        if (node.next != null) {
            node.next.prev = node.prev;
        } else {
            tail = node.prev; // Update tail if necessary
        }

        // Remove references to clear memory
        node.task = null;
        node.next = null;
        node.prev = null;
    }

    @Override
    public void add(Task task) {
        if (task == null) return;

        // Ensure no duplicates by removing existing nodes first
        if (historyMap.containsKey(task.getId())) {
            remove(task.getId());
        }
        linkLast(task);
    }

    @Override
    public void remove(int id) {
        Node node = historyMap.get(id);
        if (node != null) {
            removeNode(node); // Remove node from linked list
            historyMap.remove(id); // Ensure task is removed from map
        }
        System.out.println("Attempted to remove task with ID: " + id);
        System.out.println("Current history map size: " + historyMap.size());
    }

    @Override
    public List<Task> getHistory() {
        List<Task> history = new ArrayList<>();
        for (Node current = head; current != null; current = current.next) {
            if (current.task != null && historyMap.containsKey(current.task.getId())) {
                history.add(current.task);
            }
        }
        System.out.println("Current history size: " + history.size());
        return history;
    }
}