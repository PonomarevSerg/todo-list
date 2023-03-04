package com.ponomarev.service;

import com.ponomarev.model.entity.User;
import com.ponomarev.repository.UserRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Scanner;

@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public void save() {
        User user = userRepository.createUser();
        userRepository.save(user);
    }

    public boolean existsById() {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.println("Введите ID пользователя: ");
            return userRepository.existsById(scanner.nextLong());
        }
    }

    public void deleteById() {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.println("Введите ID пользователя: ");
            userRepository.deleteById(scanner.nextLong());
        }
    }

    public void deleteAll() {
        userRepository.deleteAll();
    }

    //Exception in thread "main" java.util.NoSuchElementException: No line found
    public void saveAll() {
        userRepository.saveAll(userRepository.createUserList());
    }
}