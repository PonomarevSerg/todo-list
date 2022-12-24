import todo.TaskRepository;
import todo.TaskService;

import java.sql.*;
import java.util.*;

public class App {
    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        Class.forName("org.postgresql.Driver");

        TaskRepository taskRepository = new TaskRepository();
        TaskService taskService = new TaskService(taskRepository);

//        final String DB_USERNAME = "postgresql";
//        final String DB_PASSWORD = "625225";
//        final String DB_URL = "jdbc:postgresql://localhost:5432/todo_db?currentSchema=public";
//        Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);

        try (Scanner sc = new Scanner(System.in)) {
            while (true) {
                System.out.println("\nВыберите действие:");
                System.out.println("Введите \"1\" -> Показать список всех задач");
                System.out.println("Введите \"2\" -> Создать задачу");
                System.out.println("Введите \"3\" -> Изменить задачу");
                System.out.println("Введите \"4\" -> Выход");

                switch (sc.nextInt()) {
                    case (1) -> taskService.showTasksList();
                    case (2) -> taskService.createTask(sc);
                    case (3) -> taskService.changeTask(sc);
                    case (4) -> System.exit(0);
                }
            }
        }
    }
}
