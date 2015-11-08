package com.example.helloworld.db;

import com.example.helloworld.core.Process;
import com.example.helloworld.core.TaskOrder;
import com.google.common.base.Optional;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.SessionFactory;

import java.util.List;

public class ProcessDAO extends AbstractDAO<Process> {
    public ProcessDAO(SessionFactory factory) {
        super(factory);
    }

    public Optional<Process> findById(Long id) {
        return Optional.fromNullable(
                uniqueResult(namedQuery("com.example.helloworld.core.Process.findByIdJoinTasks")
                        .setParameter("id", id)));
    }

    public List<Process> findAll() {
        return list(namedQuery("com.example.helloworld.core.Process.findAllJoinTasks"));
    }

    public Process create(Process process) {
        for (TaskOrder order : process.getOrders()) {
            order.setProcess(process);
        }
        return persist(process);
    }

    public void delete(Process process) {
        currentSession().delete(process);
    }
}
