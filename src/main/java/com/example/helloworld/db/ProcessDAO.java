package com.example.helloworld.db;

import com.example.helloworld.core.Process;
import com.example.helloworld.core.ProcessTask;
import com.google.common.base.Optional;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.SessionFactory;

import java.util.List;

public class ProcessDAO extends AbstractDAO<Process> {

    private TaskDAO taskDAO;

    public void setTaskDAO(TaskDAO taskDAO) {
        this.taskDAO = taskDAO;
    }

    public ProcessDAO(SessionFactory factory) {
        super(factory);
    }

    public Optional<Process> findById(Long id) {
        return Optional.fromNullable(get(id));
    }

    public List<Process> findAll() {
        return list(namedQuery("com.example.helloworld.core.Process.findAll"));
    }

    public Process create(Process process) {
        for (ProcessTask assoc : process.getTaskAssoc()) {
            long taskId = assoc.getTask().getId();
            assoc.setTask(taskDAO.findById(taskId).get());
            assoc.setProcess(process);
        }
        return persist(process);
    }

    public void delete(Process process) {
        currentSession().delete(process);
    }
}
