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

    private Class<R> resourceClass;
    private Class<E> entityClass;
    private Class<D> dtoClass;
    private GenericType<List<D>> dtoGenericType;

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

    protected abstract E createNewEntity(String name);

    public void setResourceClass(Class<R> resourceClass) {
        this.resourceClass = resourceClass;
    }

    public void setEntityClass(Class<E> entityClass) {
        this.entityClass = entityClass;
    }

    public void setDtoClass(Class<D> dtoClass) {
        this.dtoClass = dtoClass;
    }

    public void setDtoGenericType(GenericType<List<D>> dtoGenericType) {
        this.dtoGenericType = dtoGenericType;
    }

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

    protected void whenDeleteRequestPerform() {
        response = client.target(uriForIdentifiable(id, resourceClass))
                .request(APPLICATION_JSON_TYPE)
                .delete();
    }

    protected void whenUpdateRequestPerform() {
        response = client.target(uriForIdentifiable(id, resourceClass))
                .request(APPLICATION_JSON_TYPE)
                .put(json(entity));
        buildDto();
    }

    protected void thenResponseEntitiesEqualGiven() {
        List<D> dtos = response.readEntity(dtoGenericType);
        for (int i = 0; i < entities.size(); i++) {
            assertThat(dtos.get(i).getId())
                    .isEqualTo(entities.get(i).getId());
            assertThat(dtos.get(i).getName())
                    .isEqualTo(entities.get(i).getName());
        }
    }

    protected void whenGetAllRequestPerform() {
        response = client.target(uriForCollection(resourceClass))
                .request(APPLICATION_JSON_TYPE)
                .get();
    }

    protected void givenEntities(String prefix) {
        entities = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            givenEntityWithId(prefix + i);
            entities.add(entity);
        }
    }

    protected void thenSuccess(Response.Status status) {
        assertThat(response.getStatusInfo()).isEqualTo(status);
    }

    protected void thenResponseEntityEqualGiven() {
        assertThat(dto.getName()).isEqualTo(entity.getName());
    }


    protected void thenIdWasAdded() {
        assertThat(dto.getId()).isNotZero();
        assertThat(dto.getId()).isNotEqualTo(entity.getId());
    }

    protected void whenGetRequestPerform() {
        response = client.target(uriForIdentifiable(id, resourceClass))
                .request(APPLICATION_JSON_TYPE)
                .get();
        buildDto();
    }

    protected void thenError(Response.Status status) {
        assertThat(response.getStatusInfo()).isEqualTo(status);
    }

    @SuppressWarnings("unchecked")
    protected <Ex extends RuntimeException> void givenDaoActionsFailed(Class<Ex> exClass) {
        when(dao.create(any(entityClass)))
                .thenThrow(exClass);
        when(dao.update(any(entityClass)))
                .thenThrow(exClass);
        when(dao.findById(any(Long.class)))
                .thenReturn(Optional.absent());
        doThrow(exClass).when(dao).delete(any(entityClass));
    }


    protected void givenDaoActionsSuccessfully() {
        when(dao.create(any(entityClass)))
                .thenReturn(withNewId(entity));
        when(dao.update(any(entityClass)))
                .thenReturn(withGivenId(entity));
        when(dao.findById(any(Long.class)))
                .thenReturn(Optional.of(withGivenId(entity)));
        when(dao.findAll())
                .thenReturn(entities);
    }

    protected E withGivenId(E entity) {
        return withId(entity, id);
    }

    protected E withNewId(E entity) {
        return withId(entity, System.nanoTime());
    }

    protected E withId(E entity, long id) {
        E clone = SerializationUtils.clone(entity);
        clone.setId(id);
        return clone;
    }

    protected void whenCreateRequestPerform() {
        response = client.target(uriForCollection(resourceClass))
                .request(APPLICATION_JSON_TYPE)
                .post(json(entity));
        buildDto();
    }


    protected void buildDto() {
        try {
            dto = response.readEntity(dtoClass);
        } catch (ProcessingException e) {
        }
    }

    protected void givenEntity(String name) {
        entity = createNewEntity(name);
    }

    protected void givenEntityWithId(String name) {
        givenEntity(name);
        entity.setId(System.nanoTime());
    }

    protected void givenEntityId() {
        id = System.nanoTime();
    }

    protected String getEntityClassName() {
        return entityClass.getSimpleName();
    }
}
