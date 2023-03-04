package com.ponomarev.repository;

import lombok.RequiredArgsConstructor;
import com.ponomarev.model.entity.Task;
import com.ponomarev.model.entity.User;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Scanner;



@RequiredArgsConstructor
public class TaskRepository implements CrudRepository<Task, Integer> {

    @Override
    public List<Task> findAll() {
        return null;
    }

    public Task createTask() {
        return null;
    }

    @Override
    public Task save(Task task) {
        return null;
    }

    @Override
    public boolean existsById(Integer id) {
        return false;
    }

    @Override
    public void deleteById(Integer id) {

    }

    @Override
    public Iterable<Task> saveAll(Iterable<Task> entities) {
        return null;
    }

    @Override
    public void deleteAll() {

    }


    @Override
    public Optional<Task> findById(Integer id) {
        return Optional.empty();
    }
}
