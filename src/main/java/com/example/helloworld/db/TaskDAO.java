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
        return Optional.fromNullable(get(id));
    }

    public List<Task> findAll() {
        return list(namedQuery("com.example.helloworld.core.Task.findAll"));
    }

    public Task create(Task task) {
        return persist(task);
    }

    public Task update(Task task) {
        return persist(task);
    }

    public void delete(Task task) {
        currentSession().delete(task);
    }
}
