package custom.dao;

import custom.domain.Process;
import custom.domain.ProcessTask;
import com.google.common.base.Optional;
import custom.domain.Task;
import custom.exception.dao.IdentifierSpecifiedForCreatingException;
import custom.exception.dao.ProcessNotFoundException;
import custom.exception.dao.TaskNotFoundException;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.SessionFactory;

import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

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
        return list(namedQuery("custom.domain.Process.findAll"));
    }

    public Process create(Process process) {
        if (process.getId() != 0) {
            throw new IdentifierSpecifiedForCreatingException("ID was specified " +
                    "for new process, it will be created automatiscally");
        }
        assocWithTask(process);
        return persist(process);
    }

    public Process update(Process process) {
        if (!isExist(process.getId())) {
            throw new ProcessNotFoundException();
        }
        assocWithTask(process);
        return persist(process);
    }

    public void delete(Process process) {
        if (!isExist(process.getId())) {
            throw new ProcessNotFoundException();
        }
        currentSession().delete(process);
    }

    public boolean isExist(long id) {
        Optional<Process> process = findById(id);
        if (process.isPresent()) {
            currentSession().evict(process.get());
            return true;
        }
        return false;
    }

    private void assocWithTask(Process process) {
        for (ProcessTask assoc : process.getTaskAssoc()) {
            long taskId = assoc.getTask().getId();
            Optional<Task> task = taskDAO.findById(taskId);
            if (!task.isPresent()) {
                throw new TaskNotFoundException("Task with given Id doesn't exist");
            }
            assoc.setTask(task.get());
            assoc.setProcess(process);
        }
    }
}
