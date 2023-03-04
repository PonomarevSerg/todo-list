package com.ponomarev.service;

import com.ponomarev.model.entity.User;
import com.ponomarev.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import com.ponomarev.model.entity.Task;
import com.ponomarev.repository.TaskRepository;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

@RequiredArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    public List<Task> findAll() {
        return taskRepository.findAll();
    }

    //Exception in thread "main" java.util.NoSuchElementException. Scanner
    public void save() {
    }

    public Optional<Task> findById() {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.println("Введите ID задачи: ");
            return taskRepository.findById(scanner.nextInt());
        }
    }

    public boolean existsById() {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.println("Введите ID задачи: ");
            return taskRepository.existsById(scanner.nextInt());
        }
    }

    public void deleteById() {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.println("Введите ID задачи: ");
            taskRepository.deleteById(scanner.nextInt());
        }
    }

    public void deleteAll() {
        taskRepository.deleteAll();
    }
}
