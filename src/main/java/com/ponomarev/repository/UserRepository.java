package com.ponomarev.repository;

import lombok.AllArgsConstructor;
import com.ponomarev.model.entity.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

@AllArgsConstructor
public class UserRepository implements CrudRepository<User, Long> {

    @Override
    public List<User> findAll() {
        return null;
    }

    @Override
    public Optional<User> findById(Long id) {
        return Optional.empty();
    }

    public User createUser() {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.println("Введите имя пользователя: ");
            return User.builder()
                    .name(scanner.nextLine())
                    .build();
        }
    }

    @Override
    public User save(User entity) {
        return entity;
    }

    @Override
    public boolean existsById(Long id) {
        return false;
    }

    @Override
    public void deleteById(Long id) {
    }

    public Iterable<User> createUserList() {
        List<User> userList = new ArrayList<>();
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.println("Введите имя пользователя: ");
            System.out.println("\"Выход\" для завершения");
            while (scanner.nextLine().equals("Выход")) {
                userList.add(User.builder()
                        .name(scanner.nextLine())
                        .build());
            }
        }
        return userList;
    }

    @Override
    public Iterable<User> saveAll(Iterable<User> entities) {
        entities = createUserList();
        for (User entity : entities) {
            save(entity);
        }
        return entities;
    }

    @Override
    public void deleteAll() {
    }
}
