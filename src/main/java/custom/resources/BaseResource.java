package custom.resources;

import com.google.common.base.Optional;
import custom.domain.*;
import custom.dto.BaseDTO;
import custom.dto.ErrorDTO;
import custom.exception.dao.*;
import io.dropwizard.jersey.params.LongParam;

import custom.exception.dao.NotFoundException;

import javax.ws.rs.core.Response;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import static custom.Utils.*;
import static javax.ws.rs.core.Response.Status.BAD_REQUEST;
import static javax.ws.rs.core.Response.Status.INTERNAL_SERVER_ERROR;
import static javax.ws.rs.core.Response.Status.NOT_FOUND;

abstract public class BaseResource {

    public Response create(BaseEntity entity) {
        try {
            BaseEntity createdEntity = createInternal(entity);
            URI uri = uriForIdentifiable(createdEntity, getClass());
            return created(createdEntity, uri);
        } catch (IdentifierSpecifiedForCreatingException e) {
            return error(BAD_REQUEST, e.getMessage());
        } catch (NotFoundException e) {
            return error(NOT_FOUND, e.getMessage());
        } catch (Exception e) {
            return error(INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    public Response get(LongParam id) {
        Optional<? extends BaseEntity> entityOptional = getInternal(id.get());
        if (entityOptional.isPresent()) {
            return ok(entityOptional.get());
        }
        return error(NOT_FOUND, "Process not found");
    }

    public Response list() {
        try {
            List<BaseDTO> dtos = collectionDto(listInternal());
            return Response.ok(dtos).build();
        } catch (Exception e) {
            return error(INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    public Response update(LongParam id, BaseEntity entity) {
        if (entity.getId() != 0) {
            return error(BAD_REQUEST, "Process ID has been specified in request body");
        }
        try {
            entity.setId(id.get());
            BaseEntity updated = updateInternal(entity);
            return ok(updated);
        } catch (NotFoundException e) {
            return error(NOT_FOUND, e.getMessage());
        } catch (Exception e) {
            return error(INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    public Response delete(LongParam id) {
        try {
            deleteInternal(id.get());
            return Response.noContent().build();
        } catch (NotFoundException e) {
            return error(NOT_FOUND, e.getMessage());
        } catch (Exception e) {
            return error(INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    protected abstract BaseEntity createInternal(BaseEntity entity);

    protected abstract Optional<? extends BaseEntity> getInternal(long aLong);

    protected abstract List<? extends BaseEntity> listInternal();

    protected abstract BaseEntity updateInternal(BaseEntity entity);

    protected abstract void deleteInternal(long id);

    private Response error(Response.StatusType statusType, String msg) {
        return Response.status(statusType)
                .entity(new ErrorDTO(msg))
                .build();
    }

    private Response ok(BaseEntity entity) {
        return Response.ok(entity.createDto())
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

