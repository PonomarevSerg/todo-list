package com.ponomarev;

import com.ponomarev.service.TaskService;
import com.ponomarev.service.UserService;
import com.ponomarev.util.PostgresConnection;

public class App {
    public static void main(String[] args) throws ClassNotFoundException {
        var url = args[0];
        var name = args[1];
        var password = args[2];

        Class.forName("org.postgresql.Driver");

        var postgresConnection = new PostgresConnection(url, name, password);
        var userRepository = new UserRepository(postgresConnection);
        var userService = new UserService(userRepository);
        var taskRepository = new TaskRepository(postgresConnection);
        var taskService = new TaskService(taskRepository, userRepository);

        taskService.findAll().forEach(System.out::println);
        //taskService.save();

        //System.out.println(taskService.findById());
        //System.out.println(taskService.existsById());
        //taskService.deleteById();

        //taskService.findAll().forEach(System.out::println);
        userService.findAll().forEach(System.out::println);



//        ====================================================

//        final String DB_USERNAME = "postgresql";
//        final String DB_PASSWORD = "625225";
//        final String DB_URL = "jdbc:postgresql://localhost:5432/todo_db?currentSchema=public";
//        Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);

//        try (Scanner sc = new Scanner(System.in)) {
//            while (true) {
//                System.out.println("\nВыберите действие:");
//                System.out.println("Введите \"1\" -> Показать список всех задач");
//                System.out.println("Введите \"2\" -> Создать задачу");
//                System.out.println("Введите \"3\" -> Изменить задачу");
//                System.out.println("Введите \"4\" -> Выход");
//
//                switch (sc.nextInt()) {
//                    case (1) -> taskService.showTasksList();
//                    case (2) -> taskService.createTask(sc);
//                    case (3) -> taskService.changeTask(sc);
//                    case (4) -> System.exit(0);
//                }
//            }
//        }
    }
}
