package com.example.helloworld.db;

import com.example.helloworld.core.*;
import com.google.common.base.Optional;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.SessionFactory;

import java.util.List;

public class TaskDAO extends AbstractDAO<Task> {

    private ProcessDAO processDAO;

    public TaskDAO(SessionFactory factory) {
        super(factory);
    }

    public void setProcessDAO(ProcessDAO processDAO) {
        this.processDAO = processDAO;
    }

    public Optional<Task> findById(Long id) {
        return Optional.fromNullable(get(id));
    }

    public List<Task> findAll() {
        return list(namedQuery("com.example.helloworld.core.Task.findAll"));
    }

    public Task create(Task task) {
        assocWithProcess(task);
        return persist(task);
    }

    public Task update(Task task) {
        assocWithProcess(task);
        return persist(task);
    }

    public void delete(Task task) {
        currentSession().delete(task);
    }

    private void assocWithProcess(Task task) {
        for (ProcessTask assoc : task.getProcessAssoc()) {
            long processId = assoc.getProcess().getId();
            // TODO npe
            assoc.setProcess(processDAO.findById(processId).get());
            assoc.setTask(task);
        }
    }
}
