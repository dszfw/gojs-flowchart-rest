package com.example.helloworld.db;

import com.example.helloworld.core.*;
import com.google.common.base.Optional;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.SessionFactory;

import java.util.List;

public class TaskDAO extends AbstractDAO<Task> {
    public TaskDAO(SessionFactory factory) {
        super(factory);
    }

    public Optional<Task> findById(Long id) {
        return Optional.fromNullable(
                uniqueResult(namedQuery("com.example.helloworld.core.Task.findByIdJoinProcesses")
                        .setParameter("id", id)));
    }

    public List<Task> findAll() {
        return list(namedQuery("com.example.helloworld.core.Task.findAllJoinProcesses"));
    }

    public Task create(Task task) {
        for (TaskOrder order : task.getOrders()) {
            order.setTask(task);
        }
        return persist(task);
    }

    public void delete(Task task) {
        currentSession().delete(task);
    }
}