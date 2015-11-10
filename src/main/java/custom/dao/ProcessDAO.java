package custom.dao;

import custom.domain.Process;
import custom.domain.ProcessTask;
import com.google.common.base.Optional;
import custom.domain.Task;
import custom.exception.dao.ProcessNotFoundException;
import custom.exception.dao.TaskNotFoundException;
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
        return list(namedQuery("custom.domain.Process.findAll"));
    }

    public Process create(Process process) {
        assocWithTask(process);
        return persist(process);
    }

    public Process update(Process process) {
        if (!findById(process.getId()).isPresent()) {
            throw new ProcessNotFoundException("Process not found");
        }
        assocWithTask(process);
        return persist(process);
    }

    public void delete(Process process) {
        currentSession().delete(process);
    }

    private void assocWithTask(Process process) {
        for (ProcessTask assoc : process.getTaskAssoc()) {
            long taskId = assoc.getTask().getId();
            Optional<Task> taskOptional = taskDAO.findById(taskId);
            if (!taskOptional.isPresent()) {
                throw new TaskNotFoundException("Task with given Id doesn't exist");
            }
            assoc.setTask(taskOptional.get());
            assoc.setProcess(process);
        }
    }
}
