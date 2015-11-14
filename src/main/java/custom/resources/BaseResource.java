package custom.resources;

import com.google.common.base.Optional;
import custom.domain.*;
import custom.dto.BaseDTO;
import custom.dto.ErrorDTO;
import custom.exception.dao.*;
import io.dropwizard.jersey.params.LongParam;

import custom.exception.dao.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.ConstraintViolationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import static custom.Utils.*;
import static javax.ws.rs.core.Response.Status.BAD_REQUEST;
import static javax.ws.rs.core.Response.Status.INTERNAL_SERVER_ERROR;
import static javax.ws.rs.core.Response.Status.NOT_FOUND;

abstract public class BaseResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(BaseResource.class);

    protected Response create(Work work) {
        try {
            BaseEntity createdEntity = work.entity();
            URI uri = uriForIdentifiable(createdEntity, getClass());
            return created(createdEntity, uri);
        } catch (IdentifierSpecifiedForCreatingException | ConstraintViolationException e) {
            return error(BAD_REQUEST, e);
        } catch (NotFoundException e) {
            return error(NOT_FOUND, e);
        } catch (Exception e) {
            return error(INTERNAL_SERVER_ERROR, e);
        }
    }

    protected Response get(Work work) {
        Optional<? extends BaseEntity> entityOptional = work.entityOptional();
        if (entityOptional.isPresent()) {
            return ok(entityOptional.get());
        }
        return error(NOT_FOUND, "Not found");
    }

    protected Response list(Work work) {
        try {
            List<BaseDTO> dtos = collectionDto(work.entities());
            return Response.ok(dtos).build();
        } catch (Exception e) {
            return error(INTERNAL_SERVER_ERROR, e);
        }
    }

    protected Response update(LongParam id, BaseEntity entity, Work work) {
        if (entity.getId() != 0) {
            return error(BAD_REQUEST, "ID has been specified in request body");
        }
        try {
            entity.setId(id.get());
            BaseEntity updated = work.entity();
            return ok(updated);
        } catch (NotFoundException e) {
            return error(NOT_FOUND, e);
        } catch (Exception e) {
            return error(INTERNAL_SERVER_ERROR, e);
        }
    }

    protected Response delete(Work work) {
        try {
            work.doWork();
            return Response.noContent().build();
        } catch (NotFoundException e) {
            return error(NOT_FOUND, e);
        } catch (Exception e) {
            return error(INTERNAL_SERVER_ERROR, e);
        }
    }

    private Response error(Status status, Exception e) {
        if (status == INTERNAL_SERVER_ERROR) {
            LOGGER.error("An error has occurred", e);
        }
        return error(status, e.getMessage());
    }

    private Response error(Status status, String msg) {
        return Response.status(status)
                .entity(new ErrorDTO(msg))
                .build();
    }

    private Response ok(BaseEntity entity) {
        return Response.ok(entity.createDto())
                .build();
    }

    public static Response created(BaseEntity en, URI uri) {
        return Response.created(uri)
                .entity(en.createDto())
                .build();
    }

    private List<BaseDTO> collectionDto(List<? extends BaseEntity> entities) {
        List<BaseDTO> dtos = new ArrayList<>();
        for (BaseEntity entity : entities) {
            dtos.add(entity.createDto());
        }
        return dtos;
    }
}