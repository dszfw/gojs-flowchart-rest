package custom.dao;

import custom.domain.Process;
import com.google.common.base.Optional;
import custom.domain.ProcessTask;
import custom.domain.Task;
import custom.exception.dao.IdentifierSpecifiedForCreatingException;
import custom.exception.dao.ProcessNotFoundException;
import custom.exception.dao.TaskNotFoundException;
import org.hibernate.SessionFactory;

import java.util.List;

public class TaskDAO extends BaseDAO<Task> {

    private ProcessDAO processDAO;

    public TaskDAO(SessionFactory factory) {
        super(factory);
    }

    public void setProcessDAO(ProcessDAO processDAO) {
        this.processDAO = processDAO;
    }

    public List<Task> findAll() {
        return list(namedQuery("custom.domain.Task.findAll"));
    }

    public Task create(Task task) {
        if (task.getId() != 0) {
            throw new IdentifierSpecifiedForCreatingException("ID was specified " +
                    "for new task, it will be created automatically");
        }
        assocWithProcess(task);
        return persist(task);
    }

    public Task update(Task task) {
        if (!isExist(task.getId())) {
            throw new TaskNotFoundException();
        }
        assocWithProcess(task);
        return persist(task);
    }

    public void delete(Task task) {
        if (!isExist(task.getId())) {
            throw new TaskNotFoundException();
        }
        currentSession().delete(task);
    }

    private void assocWithProcess(Task task) {
        for (ProcessTask assoc : task.getProcessAssoc()) {
            long processId = assoc.getProcess().getId();
            Optional<Process> processOptional = processDAO.findById(processId);
            if (!processOptional.isPresent()) {
                throw new ProcessNotFoundException("Process with given Id, " +
                        "associated with task doesn't exist");
            }
            assoc.setProcess(processOptional.get());
            assoc.setTask(task);
        }
    }
}
