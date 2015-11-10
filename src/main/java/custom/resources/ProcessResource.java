package custom.resources;

import custom.domain.ProcessTask;
import custom.domain.Task;
import custom.dao.ProcessDAO;
import custom.domain.Process;
import custom.dao.TaskDAO;
import custom.dto.ErrorDTO;
import custom.dto.process.ProcessDTO;
import com.google.common.base.Optional;
import custom.exception.dao.ProcessNotFoundException;
import custom.exception.dao.TaskNotFoundException;
import io.dropwizard.hibernate.UnitOfWork;
import io.dropwizard.jersey.params.LongParam;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

import static javax.ws.rs.client.Entity.json;
import static javax.ws.rs.core.Response.Status.*;

@Path("/processes")
@Produces(MediaType.APPLICATION_JSON)
public class ProcessResource {

    private final ProcessDAO processDAO;
    private final TaskDAO taskDAO;

    public ProcessResource(ProcessDAO processDAO, TaskDAO taskDAO) {
        this.processDAO = processDAO;
        this.taskDAO = taskDAO;
    }

    @GET
    @Path("{processId}")
    @UnitOfWork
    public Response getProcess(@PathParam("processId") LongParam processId) {
        Optional<Process> processOptional = processDAO.findById(processId.get());
        if (processOptional.isPresent()) {
            return ok(processOptional.get());
        }
        return errorResponse(NOT_FOUND, "Process not found");
    }

    @POST
    @UnitOfWork
    public Response createProcess(Process process) {
        try {
            Process p = processDAO.create(process);
            return ok(p);
        } catch (TaskNotFoundException e) {
            return errorResponse(BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            return errorResponse(INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @GET
    @UnitOfWork
    public Response listProcesses() {
        try {
            List<Process> processes = processDAO.findAll();
            List<ProcessDTO> dtos = new ArrayList<>();
            for (Process process : processes) {
                dtos.add(new ProcessDTO(process));
            }
            return Response.ok(dtos).build();
        } catch (Exception e) {
            return errorResponse(INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @PUT
    @Path("{processId}")
    @UnitOfWork
    public Response update(@PathParam("processId") LongParam processId, Process process) {
        try {
            Optional<Process> processOptional = processDAO.findById(processId.get());
            if (processOptional.isPresent()) {
                Process p = processDAO.update(processOptional.get());
                return ok(p);
            }
            return errorResponse(NOT_FOUND, "Process not found");
        } catch (Exception e) {
            return errorResponse(INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @DELETE
    @Path("{processId}")
    @UnitOfWork
    public Response deleteProcess(@PathParam("processId") LongParam processId) {
        try {
            Optional<Process> processOptional = processDAO.findById(processId.get());
            if (processOptional.isPresent()) {
                processDAO.delete(processOptional.get());
                return Response.noContent().build();
            }
            return errorResponse(NOT_FOUND, "Process not found");
        } catch (Exception e) {
            return errorResponse(INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    private Response ok(Process p) {
        return Response.ok(new ProcessDTO(p)).build();
    }

    private Response errorResponse(Response.StatusType statusType, String msg) {
        return Response.status(statusType).entity(json(new ErrorDTO(msg))).build();
    }

    // TODO should be removed
    @GET
    @Path("test")
    @UnitOfWork
    public Process test() {
        final Process process = new Process("Process with tasks");
        final Task task = taskDAO.create(new Task("task with process"));

        ProcessTask assoc = new ProcessTask();
        assoc.setProcess(process);
        assoc.setTask(task);
        assoc.setPosition(222);

        process.getTaskAssoc().add(assoc);
        processDAO.create(process);
        return process;
    }
}
