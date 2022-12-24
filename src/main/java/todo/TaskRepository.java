package todo;

import model.Task;
import model.User;

import java.sql.*;
import java.util.Date;
import java.util.HashMap;
import java.util.Scanner;

public class TaskRepository {

    Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/todo_db?currentSchema=public&user=postgres&password=625225");

    public TaskRepository() throws SQLException {
    }

    public HashMap<Integer, User> collectUsersInMap() throws SQLException {
        Statement mapAllUsersStatement = connection.createStatement();
        HashMap<Integer, User> mapAllUsers = new HashMap<>();
        ResultSet resultAllUsers = mapAllUsersStatement.executeQuery("SELECT * FROM user_table");

        while (resultAllUsers.next()) {
            mapAllUsers.put(resultAllUsers.getInt("id"), new User(resultAllUsers.getInt("id"),
                    resultAllUsers.getString("name")));
        }
        return mapAllUsers;
    }

    public HashMap<Integer, Task> collectTasksInMap() throws SQLException {
        Statement mapAllTasksStatement = connection.createStatement();
        HashMap<Integer, Task> mapAllTasks = new HashMap<>();
        ResultSet resultMapAllTasks = mapAllTasksStatement.executeQuery("SELECT * FROM task_table");

        while (resultMapAllTasks.next()) {
            mapAllTasks.put(resultMapAllTasks.getInt("id"),
                    new Task(resultMapAllTasks.getInt("id"),
                            resultMapAllTasks.getString("name"),
                            resultMapAllTasks.getDate("create_date_time"),
                            resultMapAllTasks.getDate("deadline"),
                            collectUsersInMap().get(resultMapAllTasks.getInt("user_creator")),
                            collectUsersInMap().get(resultMapAllTasks.getInt("user_assign_to")),
                            resultMapAllTasks.getInt("priority"),
                            resultMapAllTasks.getString("state")));
        }
        return mapAllTasks;
    }

    public void createTask(Scanner scanner) throws SQLException {
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
    }
}
