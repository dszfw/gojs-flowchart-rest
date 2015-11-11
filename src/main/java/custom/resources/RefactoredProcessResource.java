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

@Path("/processes2")
@Produces(MediaType.APPLICATION_JSON)
public class RefactoredProcessResource extends BaseResource {

    private final ProcessDAO processDAO;

    public RefactoredProcessResource(ProcessDAO processDAO) {
        this.processDAO = processDAO;
    }

    @POST
    @UnitOfWork
    public Response create(Process entity) {
        return super.create(entity);
    }

    @GET
    @Path("{id}")
    @UnitOfWork
    public Response get(@PathParam("id") LongParam id) {
        return super.get(id);
    }

    @GET
    @UnitOfWork
    public Response list() {
        return super.list();
    }

    @PUT
    @Path("{id}")
    @UnitOfWork
    public Response update(@PathParam("id") LongParam id, Process process) {
        return super.update(id, process);
    }

    @DELETE
    @Path("{id}")
    @UnitOfWork
    public Response delete(@PathParam("id") LongParam id) {
        return super.delete(id);
    }

    protected BaseEntity createInternal(BaseEntity entity) {
        return processDAO.create((Process) entity);
    }

    protected Optional<? extends BaseEntity> getInternal(long id) {
        return processDAO.findById(id);
    }

    protected List<? extends BaseEntity> listInternal() {
        return processDAO.findAll();
    }

    protected BaseEntity updateInternal(BaseEntity entity) {
        return processDAO.update((Process) entity);
    }

    protected void deleteInternal(long id) {
        Process process = new Process();
        process.setId(id);
        processDAO.delete(process);
    }
}
