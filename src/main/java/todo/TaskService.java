package todo;

import java.sql.SQLException;
import java.util.Scanner;

public class TaskService {
    TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) throws SQLException {
        this.taskRepository = taskRepository;
    }

    public void showTasksList() throws SQLException {
        taskRepository.collectTasksInMap().values().forEach(System.out::println);
    }

    public void createTask(Scanner scanner) throws SQLException {
        taskRepository.createTask(scanner);
    }


    public void changeTask(Scanner scanner) throws SQLException {
        System.out.println("Выберите задачу которую необходимо изменить: ");
        taskRepository.collectTasksInMap().values().forEach(System.out::println);
        int taskId = scanner.nextInt();
        System.out.println("Выберите действие: ");
        System.out.println("Введите \"1\" -> Поручить задачу пользователю");
        System.out.println("Введите \"2\" -> Изменить статус задачи");
        System.out.println("Введите \"3\" -> Изменить приоритет задачи");
        switch (scanner.nextInt()) {
            case (1) -> taskRepository.assignTaskToUser(scanner, taskRepository.collectTasksInMap().get(taskId));
            case (2) -> taskRepository.changeTaskState(scanner, taskRepository.collectTasksInMap().get(taskId));
            case (3) -> taskRepository.changeTaskPriority(scanner, taskRepository.collectTasksInMap().get(taskId));
        }
    }
}
