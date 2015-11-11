package custom.dao;

import custom.domain.Process;
import com.google.common.base.Optional;
import custom.exception.DropwizardExampleException;
import io.dropwizard.hibernate.AbstractDAO;
import custom.domain.ProcessTask;
import custom.domain.Task;
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
        return list(namedQuery("custom.domain.Task.findAll"));
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

    public boolean isExist(long id) {
        Optional<Task> task = findById(id);
        if (task.isPresent()) {
            currentSession().evict(task.get());
            return true;
        }
        return false;
    }

    private void assocWithProcess(Task task) {
        for (ProcessTask assoc : task.getProcessAssoc()) {
            long processId = assoc.getProcess().getId();
            Optional<Process> processOptional = processDAO.findById(processId);
            if (!processOptional.isPresent()) {
                throw new DropwizardExampleException("Process with given Id doesn't exist");
            }
            assoc.setProcess(processOptional.get());
            assoc.setTask(task);
        }
    }
}
