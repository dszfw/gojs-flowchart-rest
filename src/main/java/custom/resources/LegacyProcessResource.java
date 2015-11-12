package custom.resources;

import custom.domain.ProcessTask;
import custom.domain.Task;
import custom.dao.ProcessDAO;
import custom.domain.Process;
import custom.dao.TaskDAO;
import custom.dto.BaseDTO;
import com.google.common.base.Optional;
import custom.exception.dao.IdentifierSpecifiedForCreatingException;
import custom.exception.dao.ProcessNotFoundException;
import custom.exception.dao.TaskNotFoundException;
import io.dropwizard.hibernate.UnitOfWork;
import io.dropwizard.jersey.params.LongParam;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.util.List;

import static custom.Utils.*;
import static javax.ws.rs.core.Response.Status.*;

@Path("/processes1")
@Produces(MediaType.APPLICATION_JSON)
@Deprecated
public class LegacyProcessResource {

    private final ProcessDAO processDAO;
    private final TaskDAO taskDAO;

    public LegacyProcessResource(ProcessDAO processDAO, TaskDAO taskDAO) {
        this.processDAO = processDAO;
        this.taskDAO = taskDAO;
    }

    @POST
    @UnitOfWork
    public Response create(Process process) {
        try {
            Process createdProcess = processDAO.create(process);
            URI uri = uriForIdentifiable(createdProcess, LegacyProcessResource.class);
            return created(createdProcess, uri);
        } catch (IdentifierSpecifiedForCreatingException e) {
            return error(BAD_REQUEST, e.getMessage());
        } catch (TaskNotFoundException e) {
            return error(NOT_FOUND, e.getMessage());
        } catch (Exception e) {
            return error(INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @GET
    @Path("{processId}")
    @UnitOfWork
    public Response get(@PathParam("processId") LongParam processId) {
        Optional<Process> processOptional = processDAO.findById(processId.get());
        if (processOptional.isPresent()) {
            return ok(processOptional.get());
        }
        return error(NOT_FOUND, "Process not found");
    }

    @GET
    @UnitOfWork
    public Response list() {
        try {
            List<BaseDTO> dtos = collectionDto(processDAO.findAll());
            return Response.ok(dtos).build();
        } catch (Exception e) {
            return error(INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @PUT
    @Path("{id}")
    @UnitOfWork
    public Response update(@PathParam("id") LongParam id, Process process) {
        if (process.getId() != 0) {
            return error(BAD_REQUEST, "Process ID has been specified in request body");
        }
        try {
            process.setId(id.get());
            Process updated = processDAO.update(process);
            return ok(updated);
        } catch (ProcessNotFoundException | TaskNotFoundException e) {
            return error(NOT_FOUND, e.getMessage());
        } catch (Exception e) {
            return error(INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @DELETE
    @Path("{id}")
    @UnitOfWork
    public Response delete(@PathParam("id") LongParam id) {
        try {
            Process process = new Process();
            process.setId(id.get());
            processDAO.delete(process);
            return Response.noContent().build();
        } catch (ProcessNotFoundException e) {
            return error(NOT_FOUND, e.getMessage());
        } catch (Exception e) {
            return error(INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    // TODO should be removed
    // Can be used for testing
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
