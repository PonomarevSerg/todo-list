package com.ponomarev;

import lombok.RequiredArgsConstructor;
import com.ponomarev.model.entity.Task;
import com.ponomarev.model.entity.User;
import com.ponomarev.util.PostgresConnection;

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

    private final PostgresConnection postgresConnection;

    @Override
    public List<Task> findAll() {
        try (var connection = postgresConnection.connection();
             var statement = connection.createStatement()) {

            var resultSet = statement.executeQuery("""
                    select *  from tasks 
                    inner join users on 
                    tasks.user_creator = users.id
                    order by tasks.id
                    """);

            List<Task> taskList = new ArrayList<>();

            while (resultSet.next()) {
                taskList.add(Task.builder()
                        .id(resultSet.getInt("id"))
                        .name(resultSet.getString("name"))
                        .creator(User.builder()
                                .id(resultSet.getInt("user_creator"))
                                .name(resultSet.getString(10))
                                .build())
                        .createDateTime(resultSet.getTimestamp("create_date_time").toLocalDateTime())
                        .deadline(resultSet.getTimestamp("deadline").toLocalDateTime())
                        .assignTo(User.builder()
                                .id(resultSet.getInt("user_assign_to"))
                                .build())
                        .priority(resultSet.getInt("priority"))
                        .state(resultSet.getString("state"))
                        .build());
            }
            return taskList;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public Task createTask() {
        Locale.setDefault(Locale.US);
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.println("Введите имя, приоритет и статус задачи: ");
            return Task.builder()
                    .name(scanner.nextLine())
                    .createDateTime(LocalDateTime.now())
                    .deadline(LocalDateTime.now().plus(Period.of(0,0,5)))
                    .priority(scanner.nextInt())
                    .state(scanner.next())
                    .build();
        }
    }

    @Override
    public Task save(Task task) {
        try (Connection connection = postgresConnection.connection();
             PreparedStatement preparedStatement = connection.prepareStatement("""
            INSERT INTO tasks (name, create_date_time, deadline, user_creator, state, user_assign_to, priority) VALUES (?,?,?,?,?,?,?)
            """)) {

            preparedStatement.setString(1, task.getName());
            preparedStatement.setDate(2, java.sql.Date.valueOf(task.getCreateDateTime().toLocalDate()));
            preparedStatement.setDate(3, java.sql.Date.valueOf(task.getDeadline().toLocalDate()));
            preparedStatement.setLong(4, task.getCreator().getId());
            preparedStatement.setString(5, task.getState());
            preparedStatement.setLong(6, task.getAssignTo().getId());
            preparedStatement.setInt(7, task.getPriority());
            preparedStatement.executeUpdate();
            connection.commit();

            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                task.setId(generatedKeys.getInt(1));
            }

        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return task;
    }



    @Override
    public Optional<Task> findById(Integer id) {
        try (var connection = postgresConnection.connection();
             var statement = connection.prepareStatement("select * from tasks " +
                     "inner join users " +
                     "on tasks.user_creator = users.id where tasks.id = ?")) {
            statement.setLong(1, id);
            var resultSet = statement.executeQuery();

            while (resultSet.next()) {
                return Optional.of(Task.builder()
                        .id(resultSet.getInt("id"))
                        .name(resultSet.getString("name"))
                        .creator(User.builder()
                                .id(resultSet.getInt("user_creator"))
                                .name(resultSet.getString(10))
                                .build())
                        .createDateTime(resultSet.getTimestamp("create_date_time").toLocalDateTime())
                        .deadline(resultSet.getTimestamp("deadline").toLocalDateTime())
                        .assignTo(User.builder()
                                .id(resultSet.getInt("user_assign_to"))
                                .build())
                        .priority(resultSet.getInt("priority"))
                        .state(resultSet.getString("state"))
                        .build());
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }

    @Override
    public boolean existsById(Integer id) {
        try (var connection = postgresConnection.connection();
             var statement = connection.prepareStatement("SELECT * FROM tasks where tasks.id = ?")) {
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
    public void deleteById(Integer id) {
        try (var connection = postgresConnection.connection();
             var statement = connection.prepareStatement("DELETE FROM tasks WHERE tasks.id = ?")) {
            statement.setInt(1, id);
            statement.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Iterable<Task> saveAll(Iterable<Task> entities) {
        return null;
    }

    @Override
    public void deleteAll() {
        try (var connection = postgresConnection.connection()) {
            var statement = connection.prepareStatement("DELETE FROM tasks");
            statement.executeUpdate();
            statement = connection.prepareStatement("ALTER SEQUENCE task_table_id_seq RESTART WITH 1");
            statement.executeUpdate();
            connection.commit();
            statement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /*public void createTask(Scanner scanner) throws SQLException {
        System.out.println("Введите название задачи: ");
        scanner.nextLine();
        String taskname = scanner.nextLine();

        Date currentDateJava = new Date();
        java.sql.Date currentDateSQL = new java.sql.Date(currentDateJava.getTime());
        System.out.println("Дата создания: " + currentDateSQL);

        System.out.println("Введите deadline дату: ");
        String dateInString = scanner.nextLine();
        java.sql.Date deadline = java.sql.Date.valueOf(dateInString);

        System.out.println("Введите id пользователя который дал задачу: ");
        System.out.println(collectUsersInMap().values());
        int creatorUserId = scanner.nextInt();

        System.out.println("Введите id пользователя которому передана задача: ");
        int assignToUserId = scanner.nextInt();

        connection.setAutoCommit(false);
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO task_table" +
                    " VALUES (?, ?, ?, ?, ?, ?)");
            preparedStatement.setString(1, taskname);
            preparedStatement.setDate(2, currentDateSQL);
            preparedStatement.setDate(3, deadline);
            preparedStatement.setInt(4, creatorUserId);
            preparedStatement.setString(5, "В работе");
            preparedStatement.setInt(6, assignToUserId);
            preparedStatement.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            connection.rollback();
        }
    }

    public void assignTaskToUser(Scanner scanner, Task task) throws SQLException {
        System.out.println("Выберите пользователя, которому необходимо поручить задачу: ");
        collectUsersInMap().values().forEach(System.out::println);
        int userId = scanner.nextInt();
        collectTasksInMap().get(task.getId()).setAssignTo(collectUsersInMap().get(userId));

        connection.setAutoCommit(false);
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE task_table SET user_assign_to = ? WHERE id = ?");
            preparedStatement.setInt(1, userId);
            preparedStatement.setInt(2, task.getId());
            preparedStatement.executeUpdate();
            System.out.println("Задача " + collectTasksInMap().get(task.getId()).getName() + " успешно поручена пользователю " + collectUsersInMap().get(userId).getName());
            connection.commit();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            connection.rollback();
        }
    }

    public void changeTaskState(Scanner scanner, Task task) throws SQLException {
        System.out.println("Выберите статус задачи: ");
        System.out.println("Введите \"1\" -> Задача \"В работе\"");
        System.out.println("Введите \"2\" -> Задача \"Завершена\"");
        System.out.println("Введите \"3\" -> Задача \"Отменена\"");
        String state = null;
        switch (scanner.nextInt()) {
            case (1) -> state = "В работе";
            case (2) -> state = "Завершена";
            case (3) -> state = "Отменена";
        }
        collectTasksInMap().get(task.getId()).setState(state);
        connection.setAutoCommit(false);

        try {
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE task_table SET state = ? WHERE id = ?");
            preparedStatement.setString(1, state);
            preparedStatement.setInt(2, task.getId());
            preparedStatement.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            connection.rollback();
        }
    }
    public void changeTaskPriority(Scanner scanner, Task task) throws SQLException {
        System.out.println("Введите приоритет задачи от 1-10: ");
        int priority = scanner.nextInt();
        connection.setAutoCommit(false);

        try {
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE task_table SET priority = ? WHERE id = ?");
            preparedStatement.setInt(1, priority);
            preparedStatement.setInt(2, task.getId());
            preparedStatement.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            connection.rollback();
        }
    }*/
}
