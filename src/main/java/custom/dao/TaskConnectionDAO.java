package custom.dao;

import custom.domain.*;
import org.hibernate.SessionFactory;

import java.util.List;

public class TaskConnectionDAO extends BaseDAO<TaskConnection> {

    public TaskConnectionDAO(SessionFactory factory) {
        super(factory);
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

    public List<TaskConnection> findAll() {
        return list(namedQuery("custom.domain.TaskConnection.findAll"));
    }
}
