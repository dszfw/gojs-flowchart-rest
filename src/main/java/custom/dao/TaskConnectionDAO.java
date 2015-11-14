package custom.dao;

import custom.domain.*;
import custom.exception.dao.IdentifierSpecifiedForCreatingException;
import custom.exception.dao.TaskConnectionNotFound;
import custom.exception.dao.TaskNotFoundException;
import org.hibernate.SessionFactory;

import java.util.List;

public class TaskConnectionDAO extends BaseDAO<TaskConnection> {

    public TaskConnectionDAO(SessionFactory factory) {
        super(factory);
    }

    public TaskConnection create(TaskConnection connection) {
        if (connection.getId() != 0) {
            throw new IdentifierSpecifiedForCreatingException("ID was specified " +
                    "for new taskConnection, it will be created automatically");
        }
        // TODO check that tasks are exist!
        // TODO check that process are exist!
        return persist(connection);
    }

    public TaskConnection update(TaskConnection connection) {
        if (!isExist(connection.getId())) {
            throw new TaskConnectionNotFound();
        }
        // TODO check that tasks are exist!
        // TODO check that process are exist!
        return persist(connection);
    }

    public void delete(TaskConnection connection) {
        if (!isExist(connection.getId())) {
            throw new TaskConnectionNotFound();
        }
        currentSession().delete(connection);
    }

    public List<TaskConnection> findAll() {
        return list(namedQuery("custom.domain.TaskConnection.findAll"));
    }
}
