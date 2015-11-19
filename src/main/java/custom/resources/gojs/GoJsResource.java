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
import javax.ws.rs.core.Response;

import java.util.*;

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
    public Response getGoJsJson(@PathParam("id") final LongParam id) {
        Optional<Process> p = processDAO.findById(id.get());
        Map<String, Object> dto = createGoJsProcessDTO(p.get());
        return Response.ok(dto).build();
    }

    @GET
    @UnitOfWork
    public Response getGoJsJsons() {
        Set<Map<String, Object>> dtos = new HashSet<>();
        List<Process> processList = processDAO.findAll();
        for (Process process : processList) {
            dtos.add(createGoJsProcessDTO(process));
        }
        return Response.ok(dtos).build();
    }

    @POST
    @UnitOfWork
    public Response postGoJsJson(final GoJsProcessDTO dto) {
        Process process = processDAO.create(new Process());
        Map<Long, Long> keyIdMap = new HashMap<>();

        for (GoJsTaskDTO jsTaskDTO : dto.getNodeDataArray()) {
            Task t = new Task();
            ProcessTask processTask = new ProcessTask(process, t, jsTaskDTO.getKey());
            process.getTaskAssoc().add(processTask);
            t.getProcessAssoc().add(processTask);
            t.setCategory(jsTaskDTO.getCategory());
            t.setLoc(jsTaskDTO.getLoc());
            t.setName(jsTaskDTO.getText());
            Task task = taskDAO.create(t);
            keyIdMap.put(jsTaskDTO.getKey(), task.getId());
        }

        for (GoJsTaskConnectionDTO jsTaskConnectionDTO : dto.getLinkDataArray()) {
            Optional<Task> from = taskDAO.findById(keyIdMap.get(jsTaskConnectionDTO.getFrom()));
            Optional<Task> to = taskDAO.findById(keyIdMap.get(jsTaskConnectionDTO.getTo()));
            TaskConnection tc = new TaskConnection();
            tc.setProcess(process);
            tc.setFromTask(from.get());
            tc.setToTask(to.get());
            tc.setFromConnector(jsTaskConnectionDTO.getFromPort());
            tc.setToConnector(jsTaskConnectionDTO.getToPort());
            taskConnectionDAO.create(tc);
        }
        return Response.ok(createGoJsProcessDTO(process)).build();
    }

    private Map<String, Object> createGoJsProcessDTO(Process process) {
        HashMap<String, Object> processDTO = new HashMap<>();
        HashSet<GoJsTaskDTO> nodeDataArray = new HashSet<>();
        HashSet<GoJsTaskConnectionDTO> linkDataArray = new HashSet<>();
        processDTO.put("nodeDataArray", nodeDataArray);
        processDTO.put("linkDataArray", linkDataArray);
        processDTO.put("class", "go.GraphLinksModel");
        processDTO.put("linkFromPortIdProperty", "fromPort");
        processDTO.put("linkToPortIdProperty", "toPort");

        Map<Long, Long> idKeyMap = new HashMap<>();
        for (ProcessTask pt : process.getTaskAssoc()) {
            idKeyMap.put(pt.getTask().getId(), pt.getPosition());
            GoJsTaskDTO taskDTO = new GoJsTaskDTO();
            taskDTO.setKey(pt.getPosition());
            taskDTO.setCategory(pt.getTask().getCategory());
            taskDTO.setLoc(pt.getTask().getLoc());
            taskDTO.setText(pt.getTask().getName());
            nodeDataArray.add(taskDTO);
        }
        for (TaskConnection tc : process.getConnections()) {
            GoJsTaskConnectionDTO connectionDTO = new GoJsTaskConnectionDTO();
            connectionDTO.setFrom(idKeyMap.get(tc.getFromTask().getId()));
            connectionDTO.setTo(idKeyMap.get(tc.getToTask().getId()));
            connectionDTO.setFromPort(tc.getFromConnector());
            connectionDTO.setToPort(tc.getToConnector());
            linkDataArray.add(connectionDTO);
        }
        return processDTO;
    }
}
