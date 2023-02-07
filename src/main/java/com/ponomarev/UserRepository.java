package com.ponomarev;

import lombok.AllArgsConstructor;
import com.ponomarev.model.entity.User;
import com.ponomarev.util.PostgresConnection;

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
    private final PostgresConnection postgresConnection;

    @Override
    public List<User> findAll() {
        try (var connection = postgresConnection.connection();
             var statement = connection.createStatement()) {

            List<User> userList = new ArrayList<>();
            var resultSet = statement.executeQuery("SELECT * FROM users");

            while (resultSet.next()) {
                userList.add(User.builder()
                                .id(resultSet.getInt("id"))
                                .name(resultSet.getString("name"))
                                .build());
            }

            return userList;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<User> findById(Long id) {
        try (var connection = postgresConnection.connection();
             var statement = connection.prepareStatement("SELECT * FROM users where users.id = ?")) {
            statement.setLong(1, id);
            var resultSet = statement.executeQuery();

            while (resultSet.next()) {
                return Optional.of(User.builder()
                        .id(resultSet.getInt("id"))
                        .name(resultSet.getString("name"))
                        .build());
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
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
        try (Connection connection = postgresConnection.connection();
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO users (name) VALUES (?)")) {
            preparedStatement.setString(1, entity.getName());
            preparedStatement.executeUpdate();
            connection.commit();

            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                entity.setId(generatedKeys.getInt(1));
            }

        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return entity;
    }

    @Override
    public boolean existsById(Long id) {
        try (var connection = postgresConnection.connection();
             var statement = connection.prepareStatement("SELECT * FROM users where users.id = ?")) {
            statement.setLong(1, id);
            var resultSet = statement.executeQuery();

            while (resultSet.next()) {
                return true;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    @Override
    public void deleteById(Long id) {
        try (var connection = postgresConnection.connection();
             var statement = connection.prepareStatement("DELETE FROM users WHERE users.id = ?")) {
            statement.setLong(1, id);
            statement.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
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
        try (var connection = postgresConnection.connection()) {
            var statement = connection.prepareStatement("UPDATE tasks SET user_creator = null");
            statement.executeUpdate();
            statement = connection.prepareStatement("UPDATE tasks SET user_assign_to = null");
            statement.executeUpdate();
            statement = connection.prepareStatement("DELETE FROM users");
            statement.executeUpdate();
            statement = connection.prepareStatement("ALTER SEQUENCE user_table_id_seq RESTART WITH 1");
            statement.executeUpdate();
            connection.commit();
            statement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
