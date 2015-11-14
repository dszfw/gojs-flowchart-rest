package custom.resources;

import com.google.common.base.Optional;
import custom.dao.BaseDAO;
import custom.dao.ProcessDAO;
import custom.dao.TaskConnectionDAO;
import custom.dao.TaskDAO;
import custom.domain.*;
import custom.dto.BaseDTO;
import custom.exception.dao.IdentifierSpecifiedForCreatingException;
import custom.exception.dao.NotFoundException;
import io.dropwizard.testing.junit.ResourceTestRule;
import org.apache.commons.lang3.SerializationUtils;
import org.junit.ClassRule;
import org.junit.Test;

import javax.ws.rs.ProcessingException;
import javax.ws.rs.client.Client;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

import static custom.Utils.uriForCollection;
import static custom.Utils.uriForIdentifiable;
import static javax.ws.rs.client.Entity.json;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON_TYPE;
import static javax.ws.rs.core.Response.Status.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

/**
 * Base unit test, that covers standard REST CRUD operations
 *
 * @param <R> resource type
 * @param <E> entity type
 * @param <D> response dto type
 */
abstract public class BaseResourceUnitTest<R extends BaseResource, E extends BaseEntity, D extends BaseDTO> {

    protected static final ProcessDAO processDAO = mock(ProcessDAO.class);
    protected static final TaskDAO taskDAO = mock(TaskDAO.class);
    protected static final TaskConnectionDAO taskConnectionDAO = mock(TaskConnectionDAO.class);

    @ClassRule
    public static final ResourceTestRule resources = ResourceTestRule.builder()
            .addResource(new ProcessResource(processDAO))
            .addResource(new TaskResource(taskDAO))
            .addResource(new TaskConnectionResource(taskConnectionDAO))
            .build();

    protected E entity;
    protected Response response;
    protected List<E> entities;
    protected long id;
    protected D dto;
    protected Client client = resources.client();
    protected BaseDAO<E> dao;

    protected void setUp() {
        entity = null;
        response = null;
        entities = null;
        dto = null;
    }

    public void tearDown() {
        reset(processDAO, taskDAO, taskConnectionDAO);
    }

    protected abstract Class<R> getResourceClass();
    protected abstract Class<E> getEntityClass();
    protected abstract E createNewEntity(String name);
    protected abstract Class<D> getDtoClass();
    protected abstract GenericType<List<D>> getDtosGenericType();

    @Test
    public void testCreate_entityCreatedSuccessfully_created() {
        givenEntity("New " + getEntityClassName());
        givenDaoActionsSuccessfully();
        whenCreateRequestPerform();
        thenSuccess(CREATED);
        thenResponseEntityEqualGiven();
        thenIdWasAdded();
    }

    @Test
    public void testCreate_idSpecified_badRequest() {
        givenEntityWithId("New " + getEntityClassName() + " with ID");
        givenDaoActionsFailed(IdentifierSpecifiedForCreatingException.class);
        whenCreateRequestPerform();
        thenError(BAD_REQUEST);
    }

    @Test
    public void testCreate_entityNotFound_notFound() {
        givenEntity("Some " + getEntityClassName() + " that does not exist");
        givenDaoActionsFailed(NotFoundException.class);
        whenCreateRequestPerform();
        thenError(NOT_FOUND);
    }

    @Test
    public void testCreate_createFailed_internalServerError() {
        givenEntity("Some " + getEntityClassName() + " that would not be created");
        givenDaoActionsFailed(RuntimeException.class);
        whenCreateRequestPerform();
        thenError(INTERNAL_SERVER_ERROR);
    }

    @Test
    public void testGet_entityWithGivenIdExist_ok() {
        givenEntityId();
        givenEntity("Existing " + getEntityClassName());
        givenDaoActionsSuccessfully();
        whenGetRequestPerform();
        thenSuccess(OK);
        thenResponseEntityEqualGiven();
        thenIdWasAdded();
    }

    @Test
    public void testGet_entityWithGivenIdDoesNotExist_notFound() {
        givenEntityId();
        givenDaoActionsFailed(NotFoundException.class);
        whenGetRequestPerform();
        thenError(NOT_FOUND);
    }


    @Test
    public void list_entities_ok() {
        givenEntities(getEntityClassName() + "#");
        givenDaoActionsSuccessfully();
        whenGetAllRequestPerform();
        thenSuccess(OK);
        thenResponseEntitiesEqualGiven();
    }

    @Test
    public void update_entityWithGivenIdExist_ok() {
        givenEntityId();
        givenEntity(getEntityClassName() + " that should be updated");
        givenDaoActionsSuccessfully();
        whenUpdateRequestPerform();
        thenSuccess(OK);
        thenResponseEntityEqualGiven();
        thenIdWasAdded();
    }

    @Test
    public void update_idSpecifiedInRequestBody_badRequest() {
        givenEntityId();
        givenEntityWithId(getEntityClassName() + " that should be updated has ID in body");
        whenUpdateRequestPerform();
        thenError(BAD_REQUEST);
    }

    @Test
    public void update_entityWithGivenIdDoesNotExist_notFound() {
        givenEntityId();
        givenEntity(getEntityClassName() + " that would not be updated");
        givenDaoActionsFailed(NotFoundException.class);
        whenUpdateRequestPerform();
        thenError(NOT_FOUND);
    }

    @Test
    public void delete_entityWithGivenIdExist_noContent() {
        givenEntityId();
        givenDaoActionsSuccessfully();
        whenDeleteRequestPerform();
        thenSuccess(NO_CONTENT);
    }

    @Test
    public void delete_entityWithGivenIdDoesNotExist_notFound() {
        givenEntityId();
        givenDaoActionsFailed(NotFoundException.class);
        whenDeleteRequestPerform();
        thenError(NOT_FOUND);
    }

    private void whenDeleteRequestPerform() {
        response = client.target(uriForIdentifiable(id, getResourceClass()))
                .request(APPLICATION_JSON_TYPE)
                .delete();
    }

    private void whenUpdateRequestPerform() {
        response = client.target(uriForIdentifiable(id, getResourceClass()))
                .request(APPLICATION_JSON_TYPE)
                .put(json(entity));
        buildDto();
    }

    private void thenResponseEntitiesEqualGiven() {
        List<? extends BaseDTO> dtos = response.readEntity(getDtosGenericType());
        for (int i = 0; i < entities.size(); i++) {
            assertThat(dtos.get(i).getId())
                    .isEqualTo(entities.get(i).getId());
            assertThat(dtos.get(i).getName())
                    .isEqualTo(entities.get(i).getName());
        }
    }

    private void whenGetAllRequestPerform() {
        response = client.target(uriForCollection(getResourceClass()))
                .request(APPLICATION_JSON_TYPE)
                .get();
    }

    private void givenEntities(String prefix) {
        entities = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            givenEntityWithId(prefix + i);
            entities.add(entity);
        }
    }

    private void thenSuccess(Response.Status status) {
        assertThat(response.getStatusInfo()).isEqualTo(status);
    }

    private void thenResponseEntityEqualGiven() {
        assertThat(dto.getName()).isEqualTo(entity.getName());
    }


    private void thenIdWasAdded() {
        assertThat(dto.getId()).isNotZero();
        assertThat(dto.getId()).isNotEqualTo(entity.getId());
    }

    private void whenGetRequestPerform() {
        response = client.target(uriForIdentifiable(id, getResourceClass()))
                .request(APPLICATION_JSON_TYPE)
                .get();
        buildDto();
    }

    private void thenError(Response.Status status) {
        assertThat(response.getStatusInfo()).isEqualTo(status);
    }

    private <Ex extends RuntimeException> void givenDaoActionsFailed(Class<Ex> exClass) {
        when(dao.create(any(getEntityClass())))
                .thenThrow(exClass);
        when(dao.update(any(getEntityClass())))
                .thenThrow(exClass);
        when(dao.findById(any(Long.class)))
                .thenReturn(Optional.absent());
        doThrow(exClass).when(dao).delete(any(getEntityClass()));
    }


    private void givenDaoActionsSuccessfully() {
        when(dao.create(any(getEntityClass())))
                .thenReturn(withNewId(entity));
        when(dao.update(any(getEntityClass())))
                .thenReturn(withGivenId(entity));
        when(dao.findById(any(Long.class)))
                .thenReturn(Optional.of(withGivenId(entity)));
        when(dao.findAll())
                .thenReturn(entities);
    }

    private E withGivenId(E entity) {
        return withId(entity, id);
    }

    private E withNewId(E entity) {
        return withId(entity, System.nanoTime());
    }

    private E withId(E entity, long id) {
        E clone = SerializationUtils.clone(entity);
        clone.setId(id);
        return clone;
    }

    private void whenCreateRequestPerform() {
        response = client.target(uriForCollection(getResourceClass()))
                .request(APPLICATION_JSON_TYPE)
                .post(json(entity));
        buildDto();
    }


    private void buildDto() {
        try {
            dto = response.readEntity(getDtoClass());
        } catch (ProcessingException e) {}
    }

    private void givenEntity(String name) {
        entity = createNewEntity(name);
    }

    private void givenEntityWithId(String name) {
        givenEntity(name);
        entity.setId(System.nanoTime());
    }

    private void givenEntityId() {
        id = System.nanoTime();
    }

    private String getEntityClassName() {
        return getEntityClass().getSimpleName();
    }
}
