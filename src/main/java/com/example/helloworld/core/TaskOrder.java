package com.example.helloworld.core;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

@Entity
@Table(name = "task_order")
public class TaskOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "taskId")
    private Task task;

    @Column(name = "taskOrder")
    private long taskOrder;

    public TaskOrder() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public long getTaskOrder() {
        return taskOrder;
    }

    public void setTaskOrder(long taskOrder) {
        this.taskOrder = taskOrder;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TaskOrder)) return false;

        TaskOrder taskOrder = (TaskOrder) o;

        if (getId() != taskOrder.getId()) return false;
        return getTaskOrder() == taskOrder.getTaskOrder();

    }

    @Override
    public int hashCode() {
        int result = (int) (getId() ^ (getId() >>> 32));
        result = 31 * result + (int) (getTaskOrder() ^ (getTaskOrder() >>> 32));
        return result;
    }
}
