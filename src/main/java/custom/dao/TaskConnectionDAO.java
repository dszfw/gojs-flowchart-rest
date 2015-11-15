package custom.dao;

import com.google.common.base.Optional;
import custom.domain.*;
import custom.domain.Process;
import custom.exception.dao.*;
import org.hibernate.SessionFactory;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TaskConnectionDAO extends BaseDAO<TaskConnection> {

    private ProcessDAO processDAO;
    private TaskDAO taskDAO;

    public TaskConnectionDAO(SessionFactory factory) {
        super(factory);
    }

    public void setProcessDAO(ProcessDAO processDAO) {
        this.processDAO = processDAO;
    }

    public void setTaskDAO(TaskDAO taskDAO) {
        this.taskDAO = taskDAO;
    }

    public TaskConnection create(TaskConnection connection) {
        if (connection.getId() != 0) {
            throw new IdentifierSpecifiedForCreatingException("ID was specified " +
                    "for new taskConnection, it will be created automatically");
        }
        checkThatDependenciesExist(connection);
        checkThatTasksAssociatedWithProcess(connection);
        return persist(connection);
    }

    public TaskConnection update(TaskConnection connection) {
        if (!isExist(connection.getId())) {
            throw new TaskConnectionNotFound();
        }
        checkThatDependenciesExist(connection);
        checkThatTasksAssociatedWithProcess(connection);
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

    private void checkThatDependenciesExist(TaskConnection connection) {
        // TODO check NPE
        checkThatProcessExist(connection.getProcess().getId());
        checkThatTaskExist(connection.getToTask().getId());
        checkThatTaskExist(connection.getFromTask().getId());
    }

    private void checkThatProcessExist(Long processId) {
        Optional<Process> process = processDAO.findById(processId);
        if (!process.isPresent()) {
            throw new ProcessNotFoundException("Process with given Id, " +
                    "associated with TaskConnection, doesn't exist");
        }
    }

    private void checkThatTasksAssociatedWithProcess(TaskConnection connection) {
        Process process = processDAO.findById(connection.getProcess().getId()).get();
        Task from = taskDAO.findById(connection.getFromTask().getId()).get();
        Task to = taskDAO.findById(connection.getToTask().getId()).get();

        Set<Long> processTasksIdSet = new HashSet<>();
        for (ProcessTask processTask : process.getTaskAssoc()) {
            processTasksIdSet.add(processTask.getTask().getId());
        }

        if (!processTasksIdSet.contains(from.getId())
                || !processTasksIdSet.contains(to.getId())) {
            throw new TaskNotAssociatedWithProcess();
        }
    }

    private void checkThatTaskExist(Long taskId) {
        Optional<Task> task = taskDAO.findById(taskId);
        if (!task.isPresent()) {
            throw new TaskNotFoundException("Task with given Id, " +
                    "associated with TaskConnection, doesn't exist");
        }
    }
}
