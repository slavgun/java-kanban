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
        System.out.println("Added task to historyMap: " + task.getId());
    }

    private void removeNode(Node node) {
        if (node == null || node.task == null) return; // Safety check

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

        System.out.println("Removing task from historyMap: " + node.task.getId());

        // Clear references
        node.task = null;
        node.next = null;
        node.prev = null;
    }

    @Override
    public void add(Task task) {
        if (task == null) return;
        historyMap.remove(task.getId());
        historyMap.put(task.getId(), new Node(task));
        System.out.println("Adding to historyMap: " + task);
        if (task == null) return;

        // Remove duplicates
        if (historyMap.containsKey(task.getId())) {
            System.out.println("Duplicate detected for task ID: " + task.getId());
            remove(task.getId());
        }
        linkLast(task);
    }

    @Override
    public void remove(int id) {
        System.out.println("Attempting to remove task with ID: " + id);
        Node node = historyMap.remove(id);
        if (node != null) {
            removeNode(node); // Clean node completely
            System.out.println("Successfully removed task with ID: " + id);
        } else {
            System.out.println("Task with ID: " + id + " was not found in historyMap.");
        }
        System.out.println("Current historyMap map size: " + historyMap.size());
    }

    @Override
    public List<Task> getHistory() {
        List<Task> historyMap = new ArrayList<>();
        Node current = head;
        while (current != null) {
            if (current.task != null) {
                historyMap.add(current.task);
            }
            current = current.next;
        }
        System.out.println("Current historyMap size: " + historyMap.size());
        return historyMap;
    }
}