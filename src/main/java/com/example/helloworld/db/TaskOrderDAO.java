package com.example.helloworld.db;

import com.example.helloworld.core.TaskOrder;
import com.google.common.base.Optional;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.SessionFactory;

public class TaskOrderDAO extends AbstractDAO<TaskOrder> {
    public TaskOrderDAO(SessionFactory factory) {
        super(factory);
    }

    public Optional<TaskOrder> findById(Long id) {
        return Optional.fromNullable(get(id));
    }


    public TaskOrder create(TaskOrder taskOrder) {
        return persist(taskOrder);
    }

    public TaskOrder update(TaskOrder taskOrder) {
        return persist(taskOrder);
    }

    public void delete(TaskOrder taskOrder) {
        currentSession().delete(taskOrder);
    }
}
