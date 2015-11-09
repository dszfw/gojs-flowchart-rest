package custom.dao;

import com.google.common.base.Optional;
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

    private void assocWithProcess(Task task) {
        for (ProcessTask assoc : task.getProcessAssoc()) {
            long processId = assoc.getProcess().getId();
            // TODO npe
            assoc.setProcess(processDAO.findById(processId).get());
            assoc.setTask(task);
        }
    }
}
