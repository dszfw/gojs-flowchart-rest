package com.example.helloworld.resources;

import com.example.helloworld.core.Task;
import com.example.helloworld.core.TaskConnection;
import com.example.helloworld.db.TaskConnectionDAO;
import com.example.helloworld.dto.TaskConnectionDTO;
import com.example.helloworld.dto.task.TaskDTO;
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

@Path("/connections")
@Produces(MediaType.APPLICATION_JSON)
public class TaskConnectionResource {

    private final TaskConnectionDAO dao;

    public TaskConnectionResource(TaskConnectionDAO taskConnectionDAO) {
        this.dao = taskConnectionDAO;
    }

    @GET
    @Path("{connectionId}")
    @UnitOfWork
    public TaskConnectionDTO get(@PathParam("connectionId") LongParam id) {
        return new TaskConnectionDTO(findSafely(id.get()));
    }

    @GET
    @UnitOfWork
    public List<TaskConnectionDTO> list() {
        List<TaskConnection> connections = dao.findAll();
        List<TaskConnectionDTO> dtos = new ArrayList<>();
        for (TaskConnection connection : connections) {
            dtos.add(new TaskConnectionDTO(connection));
        }
        return dtos;
    }

    @POST
    @UnitOfWork
    public TaskConnection create(TaskConnection connection) {
        return dao.create(connection);
    }

    @PUT
    @UnitOfWork
    public TaskConnection update(TaskConnection connection) {
        return dao.update(connection);
    }

    @DELETE
    @Path("{taskConnectionId}")
    @UnitOfWork
    public Response delete(@PathParam("taskConnectionId") LongParam connectionId) {
        try {
            TaskConnection connection = findSafely(connectionId.get());
            dao.delete(connection);
            return Response.status(NO_CONTENT).build();
        } catch (NotFoundException e) {
            return Response.status(NOT_FOUND).build();
        } catch (Exception e) {
            return Response.status(INTERNAL_SERVER_ERROR).build();
        }
    }

    private TaskConnection findSafely(long id) {
        final Optional<TaskConnection> connection = dao.findById(id);
        if (!connection.isPresent()) {
            throw new NotFoundException("No such connection.");
        }
        return connection.get();
    }
}
