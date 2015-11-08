package com.example.helloworld.resources;

import com.example.helloworld.core.Task;
import com.example.helloworld.db.ProcessDAO;
import com.example.helloworld.core.Process;
import com.google.common.base.Optional;
import io.dropwizard.hibernate.UnitOfWork;
import io.dropwizard.jersey.params.LongParam;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/processes")
@Produces(MediaType.APPLICATION_JSON)
public class ProcessResource {

    private final ProcessDAO processDAO;

    public ProcessResource(ProcessDAO processDAO) {
        this.processDAO = processDAO;
    }

    @GET
    @Path("{processId}")
    @UnitOfWork
    public Process getProcess(@PathParam("processId") LongParam processId) {
        return findSafely(processId.get());
    }

    @POST
    @UnitOfWork
    public Process createProcess(Process process) {
        return processDAO.create(process);
    }

    @GET
    @UnitOfWork
    public List<Process> listProcesses() {
        return processDAO.findAll();
    }

    // TODO should be removed
    @GET
    @Path("test")
    @UnitOfWork
    public Process test() {
        final Process process = new Process("Process with tasks");

        for (int i = 0; i < 10; i++) {
            Task task = new Task("Task " + i);
            task.getProcesses().add(process);
            process.getTasks().add(task);
        }

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
