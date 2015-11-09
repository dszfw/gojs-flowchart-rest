package custom.dao;

import custom.domain.Process;
import custom.domain.ProcessTask;
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
        return list(namedQuery("custom.domain.Process.findAll"));
    }

    public Process create(Process process) {
        assocWithTask(process);
        return persist(process);
    }

    public Process update(Process process) {
        assocWithTask(process);
        return persist(process);
    }

    public void delete(Process process) {
        currentSession().delete(process);
    }

    private void assocWithTask(Process process) {
        for (ProcessTask assoc : process.getTaskAssoc()) {
            long taskId = assoc.getTask().getId();
            // TODO NPE
            assoc.setTask(taskDAO.findById(taskId).get());
            assoc.setProcess(process);
        }
    }
}
