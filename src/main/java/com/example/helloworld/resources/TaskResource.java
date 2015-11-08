package com.example.helloworld.resources;

import com.example.helloworld.core.*;
import com.example.helloworld.db.TaskDAO;
import com.google.common.base.Optional;
import io.dropwizard.hibernate.UnitOfWork;
import io.dropwizard.jersey.params.LongParam;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/tasks")
@Produces(MediaType.APPLICATION_JSON)
public class TaskResource {

    private final TaskDAO taskDAO;

    public TaskResource(TaskDAO taskDAO) {
        this.taskDAO = taskDAO;
    }

    @GET
    @Path("{taskId}")
    @UnitOfWork
    public Task getTask(@PathParam("taskId") LongParam taskId) {
        return findSafely(taskId.get());
    }

    @GET
    @UnitOfWork
    public List<Task> listTasks() {
        return taskDAO.findAll();
    }

    @POST
    @UnitOfWork
    public Task createTask(Task task) {
        return taskDAO.create(task);
    }

    private Task findSafely(long taskId) {
        final Optional<Task> task = taskDAO.findById(taskId);
        if (!task.isPresent()) {
            throw new NotFoundException("No such task.");
        }
        return task.get();
    }
}
