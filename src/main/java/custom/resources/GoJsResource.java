package custom.resources;

import com.fasterxml.jackson.databind.JsonNode;
import custom.dto.gojs.GoJsProcessDTO;
import io.dropwizard.hibernate.UnitOfWork;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

@Path("/gojs")
@Produces(APPLICATION_JSON)
@Consumes(APPLICATION_JSON)
public class GoJsResource {

    @POST
    @UnitOfWork
    public void  postGoJsJson(final GoJsProcessDTO dto) {
        System.out.println("abcdef");
    }
}
