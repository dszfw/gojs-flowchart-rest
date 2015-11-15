package custom.resources;

import custom.dao.TaskDAO;
import custom.domain.*;
import com.google.common.base.Optional;
import io.dropwizard.hibernate.UnitOfWork;
import io.dropwizard.jersey.params.LongParam;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/tasks")
@Produces(MediaType.APPLICATION_JSON)
public class TaskResource extends BaseResource {

    private final TaskDAO taskDAO;

    public TaskResource(TaskDAO taskDAO) {
        this.taskDAO = taskDAO;
    }

    @POST
    @UnitOfWork
    public Response create(final Task task) {
        return create(new Work() {
            @Override
            public BaseEntity entity() {
                return taskDAO.create(task);
            }
        });
    }

    @GET
    @Path("{id}")
    @UnitOfWork
    public Response get(@PathParam("id") final LongParam id) {
        return get(new Work() {
            @Override
            public Optional<? extends BaseEntity> entityOptional() {
                return taskDAO.findById(id.get());
            }
        });
    }

    @GET
    @UnitOfWork
    public Response list() {
        return list(new Work() {
            @Override
            public List<? extends BaseEntity> entities() {
                return taskDAO.findAll();
            }
        });
    }

    @PUT
    @Path("{id}")
    @UnitOfWork
    public Response update(@PathParam("id") final LongParam id, final Task task) {
        Work work = new Work() {
            @Override
            public BaseEntity entity() {
                return taskDAO.update(task);
            }
        };
        return update(id, task, work);
    }

    @DELETE
    @Path("{id}")
    @UnitOfWork
    public Response delete(@PathParam("id") final LongParam id) {
        return delete(new Work() {
            @Override
            public void doWork() {
                Task task = new Task();
                task.setId(id.get());
                taskDAO.delete(task);
            }
        });
    }

}
