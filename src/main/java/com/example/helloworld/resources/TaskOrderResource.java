package com.example.helloworld.resources;

import com.example.helloworld.core.TaskOrder;
import com.example.helloworld.db.TaskOrderDAO;
import com.google.common.base.Optional;
import io.dropwizard.hibernate.UnitOfWork;
import io.dropwizard.jersey.params.LongParam;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static javax.ws.rs.core.Response.Status.*;

@Path("/taskorders")
@Produces(MediaType.APPLICATION_JSON)
public class TaskOrderResource {

    private final TaskOrderDAO taskOrderDAO;

    public TaskOrderResource(TaskOrderDAO taskOrderDAO) {
        this.taskOrderDAO = taskOrderDAO;
    }

    @POST
    @UnitOfWork
    public TaskOrder create(TaskOrder taskOrder) {
        return taskOrderDAO.create(taskOrder);
    }

    @PUT
    @UnitOfWork
    public TaskOrder update(TaskOrder taskOrder) {
        return taskOrderDAO.update(taskOrder);
    }

    @DELETE
    @Path("{taskOrderId}")
    @UnitOfWork
    public Response delete(@PathParam("taskOrderId") LongParam taskOrderId) {
        try {
            TaskOrder taskOrder = findSafely(taskOrderId.get());
            taskOrderDAO.delete(taskOrder);
            return Response.status(NO_CONTENT).build();
        } catch (NotFoundException e) {
            return Response.status(NOT_FOUND).build();
        } catch (Exception e) {
            return Response.status(INTERNAL_SERVER_ERROR).build();
        }
    }

    private TaskOrder findSafely(long id) {
        final Optional<TaskOrder> taskOrder = taskOrderDAO.findById(id);
        if (!taskOrder.isPresent()) {
            throw new NotFoundException("No such taskOrder.");
        }
        return taskOrder.get();
    }
}
