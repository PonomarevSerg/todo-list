package model;

import java.sql.Date;

public class Task {
    private final int id;
    private final String name;
    private final java.sql.Date createDateTime;
    private final Date deadline;
    private final User creator;
    private User assignTo;
    private final int priority;
    private String state;


    public void setState(String state) {
        this.state = state;
    }

    public void setAssignTo(User assignTo) {
        this.assignTo = assignTo;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Task(int id, String name, Date createDateTime, Date deadline, User creator, User assignTo, int priority, String state) {
        this.id = id;
        this.name = name;
        this.createDateTime = createDateTime;
        this.deadline = deadline;
        this.creator = creator;
        this.assignTo = assignTo;
        this.priority = priority;
        this.state = state;
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", createDateTime=" + createDateTime +
                ", deadline=" + deadline +
                ", creator=" + creator +
                ", assignTo=" + assignTo +
                ", priority=" + priority +
                ", state='" + state + '\'' +
                '}';
    }
}
