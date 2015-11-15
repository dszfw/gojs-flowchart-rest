package custom.integration;

import custom.domain.Process;
import custom.domain.ProcessTask;
import custom.domain.Task;
import custom.domain.TaskConnection;
import custom.dto.process.ProcessDTOinTaskDTO;
import custom.dto.task.TaskDTO;
import custom.resources.TaskResource;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.core.GenericType;
import java.util.List;

import static javax.ws.rs.core.Response.Status.NOT_FOUND;
import static org.assertj.core.api.Assertions.assertThat;
import static javax.ws.rs.core.Response.Status.CREATED;

public class TaskIntegrationTest extends BaseCrudIntegrationTest<TaskResource, Task, TaskDTO> {

    private Process process;
    private Long position;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        process = null;
        position = null;
        setResourceClass(TaskResource.class);
        setEntityClass(Task.class);
        setDtoClass(TaskDTO.class);
        setDtoGenericType(new GenericType<List<TaskDTO>>(){});
    }

    @After
    public void tearDown() throws Exception {
        super.tearDown();
    }

    @Override
    protected Task createNewEntity(String name) {
        return new Task(name);
    }

    @Test
    public void createTaskWithProcess_processExist_created() {
        givenEntity("New task with process attached");
        givenTaskPositionInProcess();
        givenProcessAttach();
        whenCreateRequestPerform();
        thenSuccess(CREATED);
        thenResponseEntityEqualGiven();
        thenIdWasAdded();
        givenEntityId(dto.getId());
        whenGetRequestPerform();
        thenProcessWasAttached();
    }

    @Test
    public void createTaskWithProcess_processNotExist_notFound() {
        givenEntity("New task with process attached");
        givenNonExistProcessAttached();
        whenCreateRequestPerform();
        thenError(NOT_FOUND);
    }

    private void givenNonExistProcessAttached() {
        process = new Process();
        process.setId(System.nanoTime());
        entity.getProcessAssoc().add(new ProcessTask(process, entity, 1L));
    }


    private void givenTaskPositionInProcess() {
        position = System.nanoTime();
    }

    private void thenProcessWasAttached() {
        assertThat(dto.getProcesses()).isNotEmpty();
        assertThat(dto.getProcesses().size()).isEqualTo(1);
        ProcessDTOinTaskDTO processDto = dto.getProcesses().iterator().next();
        assertThat(processDto.getId()).isEqualTo(process.getId());
        assertThat(processDto.getPosition()).isEqualTo(position);

    }

    private void givenProcessAttach() {
        process = new Process("Some process");
        process.setId(1000);
        ProcessTask assoc = new ProcessTask(process, entity, position);
        entity.getProcessAssoc().add(assoc);
    }
}
