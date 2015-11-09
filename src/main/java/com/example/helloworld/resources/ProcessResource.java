package com.example.helloworld.resources;

import com.example.helloworld.core.ProcessTask;
import com.example.helloworld.core.Task;
import com.example.helloworld.db.ProcessDAO;
import com.example.helloworld.core.Process;
import com.example.helloworld.db.TaskDAO;
import com.example.helloworld.dto.ProcessDTO;
import com.google.common.base.Optional;
import io.dropwizard.hibernate.UnitOfWork;
import io.dropwizard.jersey.params.LongParam;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

import static javax.ws.rs.core.Response.Status.INTERNAL_SERVER_ERROR;
import static javax.ws.rs.core.Response.Status.NOT_FOUND;
import static javax.ws.rs.core.Response.Status.NO_CONTENT;

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
    public ProcessDTO getProcess(@PathParam("processId") LongParam processId) {
        return new ProcessDTO(findSafely(processId.get()));
    }

    @POST
    @UnitOfWork
    public Process createProcess(Process process) {
        return processDAO.create(process);
    }

    @GET
    @UnitOfWork
    public List<ProcessDTO> listProcesses() {
        List<Process> processes = processDAO.findAll();
        List<ProcessDTO> dtos = new ArrayList<>();
        for (Process process : processes) {
            dtos.add(new ProcessDTO(process));
        }
        return dtos;
    }

    @PUT
    @UnitOfWork
    public Process update(Process process) {
        return processDAO.update(process);
    }

    @DELETE
    @Path("{processId}")
    @UnitOfWork
    public Response deleteProcess(@PathParam("processId") LongParam processId) {
        try {
            Process process = findSafely(processId.get());
            processDAO.delete(process);
            return Response.status(NO_CONTENT).build();
        } catch (NotFoundException e) {
            return Response.status(NOT_FOUND).build();
        } catch (Exception e) {
            return Response.status(INTERNAL_SERVER_ERROR).build();
        }
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

    private Process findSafely(long processId) {
        final Optional<Process> process = processDAO.findById(processId);
        if (!process.isPresent()) {
            throw new NotFoundException("No such process.");
        }
        return process.get();
    }
}
