package custom.integration;

import com.example.helloworld.HelloWorldApplication;
import com.example.helloworld.HelloWorldConfiguration;
import custom.domain.*;
import custom.dto.BaseDTO;
import custom.resources.BaseResource;
import io.dropwizard.testing.ConfigOverride;
import io.dropwizard.testing.ResourceHelpers;
import io.dropwizard.testing.junit.DropwizardAppRule;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;

import javax.ws.rs.ProcessingException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static custom.Utils.uriForCollection;
import static custom.Utils.uriForIdentifiable;
import static javax.ws.rs.client.Entity.json;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON_TYPE;
import static javax.ws.rs.core.Response.Status.*;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Base test, that covers standard CRUD operations
 *
 * @param <R> resource type
 * @param <E> entity type
 * @param <D> response dto type
 */
abstract public class BaseCrudIntegrationTest<R extends BaseResource, E extends BaseEntity, D extends BaseDTO> {

    protected static final String TMP_FILE = createTempFile();
    protected static final String CONFIG_PATH = ResourceHelpers.resourceFilePath("test-example.yml");

    @ClassRule
    public static final DropwizardAppRule<HelloWorldConfiguration> RULE = new DropwizardAppRule<>(
            HelloWorldApplication.class, CONFIG_PATH, ConfigOverride.config("database.url", "jdbc:h2:" + TMP_FILE));


    @BeforeClass
    public static void migrateDb() throws Exception {
        RULE.getApplication().run("db", "migrate", CONFIG_PATH);
    }

    protected E entity;
    protected long id;
    protected List<E> entities;
    protected Set<Long> expectedIds;
    protected Response response;
    protected D dto;
    protected Client client;
    protected WebTarget target;

    public void setUp() throws Exception {
        client = ClientBuilder.newClient();
        target = client.target("http://localhost:" + RULE.getLocalPort());
        entity = null;
        response = null;
        entities = null;
        dto = null;
    }

    public void tearDown() throws Exception {
        client.close();
    }

    protected abstract Class<R> getResourceClass();

    protected abstract Class<E> getEntityClass();

    protected abstract E createNewEntity(String name);

    protected abstract Class<D> getDtoClass();

    protected abstract GenericType<List<D>> getDtosGenericType();

    @Test
    public void testCreate_entityCreatedSuccessfully_created() {
        givenEntity("New " + getEntityClassName());
        whenCreateRequestPerform();
        thenSuccess(CREATED);
        thenResponseEntityEqualGiven();
        thenIdWasAdded();
    }

    @Test
    public void testCreate_idSpecified_badRequest() {
        givenEntityWithId("New " + getEntityClassName() + "#");
        whenCreateRequestPerform();
        thenError(BAD_REQUEST);
    }

    @Test
    public void testGet_entityWithGivenIdExist_ok() {
        givenEntityId(1000);
        givenEntity("Some " + getEntityClassName());
        whenGetRequestPerform();
        thenSuccess(OK);
        thenResponseEntityEqualGiven();
        thenIdWasAdded();
        thenIdEqualGiven();
    }

    private void thenIdEqualGiven() {
        assertThat(dto.getId()).isEqualTo(id);
    }

    @Test
    public void testGet_entityWithGivenIdDoesNotExist_notFound() {
        givenEntityId();
        whenGetRequestPerform();
        thenError(NOT_FOUND);
    }

    @Test
    public void list_entities_ok() {
        givenEntities(getEntityClassName() + "#");
        whenGetAllRequestPerform();
        thenSuccess(OK);
        thenResponseEntitiesNotEmptyAndEqualGiven();
    }

    @Test
    public void update_entityWithGivenIdExist_ok() {
        givenEntityId(1012);
        givenEntity(getEntityClassName() + " that should be updated");
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
        whenUpdateRequestPerform();
        thenError(NOT_FOUND);
    }

    @Test
    public void delete_entityWithGivenIdExist_noContent() {
        givenEntityId(1011);
        whenDeleteRequestPerform();
        thenSuccess(NO_CONTENT);
    }

    @Test
    public void delete_entityWithGivenIdDoesNotExist_notFound() {
        givenEntityId();
        whenDeleteRequestPerform();
        thenError(NOT_FOUND);
    }

    protected void whenDeleteRequestPerform() {
        response = target.path(uriForIdentifiable(id, getResourceClass()).toString())
                .request(APPLICATION_JSON_TYPE)
                .delete();
    }

    protected void whenUpdateRequestPerform() {
        response = target.path(uriForIdentifiable(id, getResourceClass()).toString())
                .request(APPLICATION_JSON_TYPE)
                .put(json(entity));
        buildDto();
    }

    protected void thenResponseEntitiesNotEmptyAndEqualGiven() {
        List<? extends BaseDTO> dtos = response.readEntity(getDtosGenericType());
        assertThat(dtos).isNotEmpty();

        Set<Long> actualIds = new HashSet<>();
        for (BaseDTO dto : dtos) {
            actualIds.add(dto.getId());
        }
        assertThat(actualIds).containsAll(expectedIds);
    }

    protected void whenGetAllRequestPerform() {
        response = target.path(uriForCollection(getResourceClass()).toString())
                .request(APPLICATION_JSON_TYPE)
                .get();
    }

    protected void givenEntities(String prefix) {
        entities = new ArrayList<>();
        expectedIds = new HashSet<>();
        for (long i = 1001; i < 1011; i++) {
            givenEntity(prefix + i);
            expectedIds.add(i);
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
        response = target.path(uriForIdentifiable(id, getResourceClass()).toString())
                .request(APPLICATION_JSON_TYPE)
                .get();
        buildDto();
    }

    protected void thenError(Response.Status status) {
        assertThat(response.getStatusInfo()).isEqualTo(status);
    }

    protected void whenCreateRequestPerform() {
        response = target.path(uriForCollection(getResourceClass()).toString())
                .request(APPLICATION_JSON_TYPE)
                .post(json(entity));
        buildDto();
    }

    protected void buildDto() {
        try {
            dto = response.readEntity(getDtoClass());
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

    protected void givenEntityId(long id) {
        this.id = id;
    }

    protected void givenEntityId() {
        id = System.nanoTime();
    }

    protected String getEntityClassName() {
        return getEntityClass().getSimpleName();
    }

    private static String createTempFile() {
        try {
            return File.createTempFile("test-example", null).getAbsolutePath();
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }
}
