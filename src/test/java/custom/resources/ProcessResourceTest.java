package custom.resources;

import com.google.common.base.Optional;
import custom.dao.ProcessDAO;
import custom.dao.TaskDAO;
import custom.domain.Process;
import custom.dto.process.ProcessDTO;
import custom.exception.dao.IdentifierSpecifiedForCreatingException;
import custom.exception.dao.ProcessNotFoundException;
import custom.exception.dao.TaskNotFoundException;
import io.dropwizard.testing.junit.ResourceTestRule;
import org.apache.commons.lang3.SerializationUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.runners.MockitoJUnitRunner;

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

@RunWith(MockitoJUnitRunner.class)
public class ProcessResourceTest {

    private static final ProcessDAO processDAO = mock(ProcessDAO.class);
    private static final TaskDAO taskDAO = mock(TaskDAO.class);
    public static final Class<ProcessResource> RESOURCE_CLASS = ProcessResource.class;

    private Process process;
    private Response response;
    private List<Process> processes;
    private long processId;
    private ProcessDTO processDTO;

    @ClassRule
    public static final ResourceTestRule resources = ResourceTestRule.builder()
            .addResource(new ProcessResource(processDAO))
            .build();

    private Client client = resources.client();

    @Captor
    private ArgumentCaptor<Process> processArgumentCaptor;

    @Before
    public void setUp() {
        process = new Process();
        response = null;
        processes = null;
        processDTO = null;
    }

    @After
    public void tearDown() {
        reset(processDAO, taskDAO);
    }

    @Test
    public void create_processCreatedSuccessfully_created() {
        givenProcess("New process");
        givenDaoActionsSuccessfully();
        whenCreateRequestPerform();
        thenSuccess(CREATED);
        thenResponseProcessEqualGiven();
        thenIdWasAdded();
    }

    @Test
    public void create_idSpecified_badRequest() {
        givenProcessWithId("New process with ID");
        givenDaoActionsFailed(IdentifierSpecifiedForCreatingException.class);
        whenCreateRequestPerform();
        thenError(BAD_REQUEST);
    }

    @Test
    public void create_taskNotFound_notFound() {
        givenProcess("New process with task assoc");
        givenDaoActionsFailed(TaskNotFoundException.class);
        whenCreateRequestPerform();
        thenError(NOT_FOUND);
    }

    @Test
    public void create_processCreateFailed_internalServerError() {
        givenProcess("Some process");
        givenDaoActionsFailed(RuntimeException.class);
        whenCreateRequestPerform();
        thenError(INTERNAL_SERVER_ERROR);
    }

    @Test
    public void get_processWithGivenIdExist_ok() {
        givenProcessId();
        givenProcess("Existing process");
        givenDaoActionsSuccessfully();
        whenGetRequestPerform();
        thenSuccess(OK);
        thenResponseProcessEqualGiven();
        thenIdWasAdded();
    }

    @Test
    public void get_processWithGivenIdDoesNotExist_notFound() {
        givenProcessId();
        givenProcessNonExist();
        whenGetRequestPerform();
        thenError(NOT_FOUND);
    }

    private void givenProcessId() {
        processId = System.nanoTime();
    }

    @Test
    public void list_processes_ok() {
        givenProcesses("Process #");
        givenDaoActionsSuccessfully();
        whenGetAllRequestPerform();
        thenSuccess(OK);
        thenResponseProcessesEqualGiven();
    }

    @Test
    public void update_processWithGivenIdExist_ok() {
        givenProcessId();
        givenProcess("New process name");
        givenDaoActionsSuccessfully();
        whenUpdateRequestPerform();
        thenSuccess(OK);
        thenResponseProcessEqualGiven();
        thenIdWasAdded();
    }

    @Test
    public void update_idSpecifiedInRequestBody_badRequest() {
        givenProcessId();
        givenProcessWithId("Process with id");
        whenUpdateRequestPerform();
        thenError(BAD_REQUEST);
    }

    @Test
    public void update_processWithGivenIdDoesNotExist_notFound() {
        givenProcessId();
        givenProcess("Process that would not be updated");
        givenDaoActionsFailed(ProcessNotFoundException.class);
        whenUpdateRequestPerform();
        thenError(NOT_FOUND);
    }

    @Test
    public void delete_processWithGivenIdExist_noContent() {
        givenProcessId();
        givenDaoActionsSuccessfully();
        whenDeleteRequestPerform();
        thenSuccess(NO_CONTENT);
    }

    @Test
    public void delete_processWithGivenIdDoesNotExist_notFound() {
        givenProcessId();
        givenDaoActionsFailed(ProcessNotFoundException.class);
        whenDeleteRequestPerform();
        thenError(NOT_FOUND);
    }

    private void whenDeleteRequestPerform() {
        response = client.target(uriForIdentifiable(processId, RESOURCE_CLASS))
                .request(APPLICATION_JSON_TYPE)
                .delete();
    }

    private void whenUpdateRequestPerform() {
        response = client.target(uriForIdentifiable(processId, RESOURCE_CLASS))
                .request(APPLICATION_JSON_TYPE)
                .put(json(process));
        buildProcessDto();
    }

    private void thenResponseProcessesEqualGiven() {
        List<ProcessDTO> dtos = response.readEntity(new GenericType<List<ProcessDTO>>() {
        });
        for (int i = 0; i < processes.size(); i++) {
            assertThat(dtos.get(i).getId())
                    .isEqualTo(processes.get(i).getId());
            assertThat(dtos.get(i).getName())
                    .isEqualTo(processes.get(i).getName());
        }
    }

    private void whenGetAllRequestPerform() {
        response = client.target(uriForCollection(RESOURCE_CLASS))
                .request(APPLICATION_JSON_TYPE)
                .get();
    }

    private void givenProcesses(String prefix) {
        processes = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            givenProcessWithId(prefix + i);
            processes.add(process);
        }
    }

    private void thenSuccess(Response.Status status) {
        assertThat(response.getStatusInfo()).isEqualTo(status);
    }

    private void thenResponseProcessEqualGiven() {
        assertThat(processDTO.getName()).isEqualTo(process.getName());
    }

    private void thenIdWasAdded() {
        assertThat(processDTO.getId()).isNotZero();
        assertThat(processDTO.getId()).isNotEqualTo(process.getId());
    }

    private void whenGetRequestPerform() {
        response = client.target(uriForIdentifiable(processId, RESOURCE_CLASS))
                .request(APPLICATION_JSON_TYPE)
                .get();
        buildProcessDto();
    }

    private void thenError(Response.Status status) {
        assertThat(response.getStatusInfo()).isEqualTo(status);
    }

    private <T extends RuntimeException> void givenDaoActionsFailed(Class<T> eClass) {
        when(processDAO.create(any(Process.class)))
                .thenThrow(eClass);
        when(processDAO.update(any(Process.class)))
                .thenThrow(eClass);
        doThrow(eClass).when(processDAO).delete(any(Process.class));
    }

    private void givenDaoActionsSuccessfully() {
        when(processDAO.create(any(Process.class)))
                .thenReturn(withNewId(process));
        when(processDAO.update(any(Process.class)))
                .thenReturn(withGivenId(process));
        when(processDAO.findById(any(Long.class)))
                .thenReturn(Optional.of(withGivenId(process)));
        when(processDAO.findAll())
                .thenReturn(processes);
    }

    private Process withGivenId(Process process) {
        return withId(process, processId);
    }

    private Process withNewId(Process process) {
        return withId(process, System.nanoTime());
    }

    private Process withId(Process process, long processId) {
        Process clone = SerializationUtils.clone(process);
        clone.setId(processId);
        return clone;
    }

    private void whenCreateRequestPerform() {
        response = client.target(uriForCollection(RESOURCE_CLASS))
                .request(APPLICATION_JSON_TYPE)
                .post(json(process));
        buildProcessDto();
    }

    private void buildProcessDto() {
        try {
            processDTO = response.readEntity(ProcessDTO.class);
        } catch (ProcessingException e) {

        }
    }

    private void givenProcessNonExist() {
        when(processDAO.findById(any(Long.class)))
                .thenReturn(Optional.absent());
    }

    private void givenProcess(String name) {
        process = new Process(name);
    }

    private void givenProcessWithId(String name) {
        givenProcess(name);
        process.setId(System.nanoTime());
    }
}
