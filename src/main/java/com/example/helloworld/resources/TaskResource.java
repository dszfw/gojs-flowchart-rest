package com.example.helloworld.resources;

import com.example.helloworld.core.*;
import com.example.helloworld.db.TaskDAO;
import com.google.common.base.Optional;
import io.dropwizard.hibernate.UnitOfWork;
import io.dropwizard.jersey.params.LongParam;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

import static javax.ws.rs.core.Response.Status.INTERNAL_SERVER_ERROR;
import static javax.ws.rs.core.Response.Status.NOT_FOUND;
import static javax.ws.rs.core.Response.Status.NO_CONTENT;

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

    @PUT
    @UnitOfWork
    public Task update(Task task) {
        return taskDAO.update(task);
    }

    @DELETE
    @Path("{taskId}")
    @UnitOfWork
    public Response deleteTask(@PathParam("taskId") LongParam taskId) {
        try {
            Task task = findSafely(taskId.get());
            taskDAO.delete(task);
            return Response.status(NO_CONTENT).build();
        } catch (NotFoundException e) {
            return Response.status(NOT_FOUND).build();
        } catch (Exception e) {
            return Response.status(INTERNAL_SERVER_ERROR).build();
        }
    }

    private Task findSafely(long taskId) {
        final Optional<Task> task = taskDAO.findById(taskId);
        if (!task.isPresent()) {
            throw new NotFoundException("No such task.");
        }
        return task.get();
    }
}
