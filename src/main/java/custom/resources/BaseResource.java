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

    protected Response createProcess(Work work) {
        try {
            BaseEntity createdEntity = work.entity();
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

    protected Response getProcess(Work work) {
        Optional<? extends BaseEntity> entityOptional = work.entityOptional();
        if (entityOptional.isPresent()) {
            return ok(entityOptional.get());
        }
        return error(NOT_FOUND, "Not found");
    }

    protected Response listProcesses(Work work) {
        try {
            List<BaseDTO> dtos = collectionDto(work.entities());
            return Response.ok(dtos).build();
        } catch (Exception e) {
            return error(INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    protected Response updateProcess(LongParam id, BaseEntity entity, Work work) {
        if (entity.getId() != 0) {
            return error(BAD_REQUEST, "ID has been specified in request body");
        }
        try {
            entity.setId(id.get());
            BaseEntity updated = work.entity();
            return ok(updated);
        } catch (NotFoundException e) {
            return error(NOT_FOUND, e.getMessage());
        } catch (Exception e) {
            return error(INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    protected Response deleteProcess(Work work) {
        try {
            work.doWork();
            return Response.noContent().build();
        } catch (NotFoundException e) {
            return error(NOT_FOUND, e.getMessage());
        } catch (Exception e) {
            return error(INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

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