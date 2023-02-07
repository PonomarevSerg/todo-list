package com.ponomarev.service;

import com.ponomarev.model.entity.User;
import com.ponomarev.UserRepository;
import lombok.RequiredArgsConstructor;
import com.ponomarev.model.entity.Task;
import com.ponomarev.TaskRepository;

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
        Task task = taskRepository.createTask();
        try (Scanner scanner = new Scanner(System.in)) {
            List<User> all = userRepository.findAll();
            all.forEach(System.out::println);
            System.out.println("Введите ID пользователя которому необходимо поручить задачу: ");
            int assignTo = scanner.nextInt();
            task.setAssignTo(all.get(assignTo));
            System.out.println("Введите ID пользователя который создал задачу: ");
            int creator = scanner.nextInt();
            task.setCreator(all.get(creator));
        }
        taskRepository.save(task);
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
