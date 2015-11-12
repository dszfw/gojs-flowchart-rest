package custom.resources;

import com.google.common.base.Optional;
import custom.dao.ProcessDAO;
import custom.domain.BaseEntity;
import custom.domain.Process;
import io.dropwizard.hibernate.UnitOfWork;
import io.dropwizard.jersey.params.LongParam;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/processes")
@Produces(MediaType.APPLICATION_JSON)
public class ProcessResource extends BaseResource {

    private final ProcessDAO processDAO;

    public ProcessResource(ProcessDAO processDAO) {
        this.processDAO = processDAO;
    }

    @POST
    @UnitOfWork
    public Response create(Process entity) {
        return createProcess(new Work() {
            @Override
            public BaseEntity entity() {
                return processDAO.create(entity);
            }
        });
    }

    @GET
    @Path("{id}")
    @UnitOfWork
    public Response get(@PathParam("id") LongParam id) {
        return getProcess(new Work() {
            @Override
            public Optional<? extends BaseEntity> entityOptional() {
                return processDAO.findById(id.get());
            }
        });
    }

    @GET
    @UnitOfWork
    public Response list() {
        return listProcesses(new Work() {
            @Override
            public List<? extends BaseEntity> entities() {
                return processDAO.findAll();
            }
        });
    }

    @PUT
    @Path("{id}")
    @UnitOfWork
    public Response update(@PathParam("id") LongParam id, Process process) {
        Work work = new Work() {
            @Override
            public BaseEntity entity() {
                return processDAO.update(process);
            }
        };
        return updateProcess(id, process, work);
    }

    @DELETE
    @Path("{id}")
    @UnitOfWork
    public Response delete(@PathParam("id") LongParam id) {
        return deleteProcess(new Work() {
            @Override
            public void doWork() {
                Process process = new Process();
                process.setId(id.get());
                processDAO.delete(process);
            }
        });
    }
}