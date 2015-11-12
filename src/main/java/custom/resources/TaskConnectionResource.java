package custom.resources;

import custom.domain.*;
import custom.dao.TaskConnectionDAO;
import com.google.common.base.Optional;
import io.dropwizard.hibernate.UnitOfWork;
import io.dropwizard.jersey.params.LongParam;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.util.List;

@Path("/connections")
@Produces(MediaType.APPLICATION_JSON)
public class TaskConnectionResource extends BaseResource{

    private final TaskConnectionDAO dao;

    public TaskConnectionResource(TaskConnectionDAO taskConnectionDAO) {
        this.dao = taskConnectionDAO;
    }

    @POST
    @UnitOfWork
    public Response create(TaskConnection connection) {
        return create(new Work() {
            @Override
            public BaseEntity entity() {
                return dao.create(connection);
            }
        });
    }

    @GET
    @Path("{id}")
    @UnitOfWork
    public Response get(@PathParam("id") LongParam id) {
        return get(new Work() {
            @Override
            public Optional<? extends BaseEntity> entityOptional() {
                return dao.findById(id.get());
            }
        });
    }

    @GET
    @UnitOfWork
    public Response list() {
        return list(new Work() {
            @Override
            public List<? extends BaseEntity> entities() {
                return dao.findAll();
            }
        });
    }

    @PUT
    @Path("{id}")
    @UnitOfWork
    public Response update(@PathParam("id") LongParam id, TaskConnection connection) {
        Work work = new Work() {
            @Override
            public BaseEntity entity() {
                return dao.update(connection);
            }
        };
        return update(id, connection, work);
    }

    @DELETE
    @Path("{id}")
    @UnitOfWork
    public Response delete(@PathParam("id") LongParam id) {
        return delete(new Work() {
            @Override
            public void doWork() {
                TaskConnection connection = new TaskConnection();
                connection.setId(id.get());
                dao.delete(connection);
            }
        });
    }
    
}
