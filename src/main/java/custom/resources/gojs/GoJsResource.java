package custom.resources.gojs;

import com.google.common.base.Optional;
import custom.dao.ProcessDAO;
import custom.dao.TaskConnectionDAO;
import custom.dao.TaskDAO;
import custom.domain.Process;
import custom.domain.ProcessTask;
import custom.domain.Task;
import custom.domain.TaskConnection;
import custom.dto.gojs.GoJsProcessDTO;
import custom.dto.gojs.GoJsTaskConnectionDTO;
import custom.dto.gojs.GoJsTaskDTO;
import io.dropwizard.hibernate.UnitOfWork;
import io.dropwizard.jersey.params.LongParam;

import javax.ws.rs.*;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

@Path("/gojs")
@Produces(APPLICATION_JSON)
@Consumes(APPLICATION_JSON)
public class GoJsResource {

    private ProcessDAO processDAO;
    private TaskDAO taskDAO;
    private TaskConnectionDAO taskConnectionDAO;

    public void setProcessDAO(ProcessDAO processDAO) {
        this.processDAO = processDAO;
    }

    public void setTaskDAO(TaskDAO taskDAO) {
        this.taskDAO = taskDAO;
    }

    public void setTaskConnectionDAO(TaskConnectionDAO taskConnectionDAO) {
        this.taskConnectionDAO = taskConnectionDAO;
    }

    @GET
    @Path("{id}")
    @UnitOfWork
    public GoJsProcessDTO getGoJsJson(@PathParam("id") final LongParam id) {
        GoJsProcessDTO processDTO = new GoJsProcessDTO();
        processDTO.setLinkDataArray(new HashSet<>());
        processDTO.setNodeDataArray(new HashSet<>());
        Optional<Process> processOptional = processDAO.findById(id.get());
        Process process = processOptional.get();
        for (ProcessTask pt : process.getTaskAssoc()) {
            GoJsTaskDTO taskDTO = new GoJsTaskDTO();
            taskDTO.setKey(pt.getPosition());
            taskDTO.setCategory(pt.getTask().getCategory());
            taskDTO.setLoc(pt.getTask().getLoc());
            taskDTO.setText(pt.getTask().getName());
            processDTO.getNodeDataArray().add(taskDTO);
        }
        for (TaskConnection tc : process.getConnections()) {
            GoJsTaskConnectionDTO connectionDTO = new GoJsTaskConnectionDTO();
            connectionDTO.setFrom(tc.getFromTask().getId());
            connectionDTO.setTo(tc.getToTask().getId());
            connectionDTO.setFromPort(tc.getFromConnector());
            connectionDTO.setToPort(tc.getToConnector());
            processDTO.getLinkDataArray().add(connectionDTO);
        }
        return processDTO;
    }

    @POST
    @UnitOfWork
    public void  postGoJsJson(final GoJsProcessDTO dto) {
        Process process = processDAO.create(new Process());
        Map<Long, Long> taskKeyId = new HashMap<>();

        for (GoJsTaskDTO jsTaskDTO: dto.getNodeDataArray()) {
            Task t = new Task();
            ProcessTask processTask = new ProcessTask(process, t, jsTaskDTO.getKey());
            process.getTaskAssoc().add(processTask);
            t.getProcessAssoc().add(processTask);
            t.setCategory(jsTaskDTO.getCategory());
            t.setLoc(jsTaskDTO.getLoc());
            t.setName(jsTaskDTO.getText());
            Task task = taskDAO.create(t);
            taskKeyId.put(jsTaskDTO.getKey(), task.getId());
        }

        for (GoJsTaskConnectionDTO jsTaskConnectionDTO : dto.getLinkDataArray()) {
            Optional<Task> from = taskDAO.findById(taskKeyId.get(jsTaskConnectionDTO.getFrom()));
            Optional<Task> to = taskDAO.findById(taskKeyId.get(jsTaskConnectionDTO.getTo()));
            TaskConnection tc = new TaskConnection();
            tc.setProcess(process);
            tc.setFromTask(from.get());
            tc.setToTask(to.get());
            tc.setFromConnector(jsTaskConnectionDTO.getFromPort());
            tc.setToConnector(jsTaskConnectionDTO.getToPort());
            taskConnectionDAO.create(tc);
        }

    }
}
