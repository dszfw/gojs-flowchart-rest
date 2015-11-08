package com.example.helloworld.db;

import com.example.helloworld.core.TaskConnection;
import com.google.common.base.Optional;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.SessionFactory;

public class TaskConnectionDAO extends AbstractDAO<TaskConnection> {
    public TaskConnectionDAO(SessionFactory factory) {
        super(factory);
    }

    public Optional<TaskConnection> findById(Long id) {
        return Optional.fromNullable(get(id));
    }

    public TaskConnection create(TaskConnection connection) {
        return persist(connection);
    }

    public TaskConnection update(TaskConnection connection) {
        return persist(connection);
    }

    public void delete(TaskConnection connection) {
        currentSession().delete(connection);
    }
}
