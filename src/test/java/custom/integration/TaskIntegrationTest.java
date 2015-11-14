package custom.integration;

import custom.domain.Task;
import custom.dto.task.TaskDTO;
import custom.resources.TaskResource;
import org.junit.After;
import org.junit.Before;

import javax.ws.rs.core.GenericType;
import java.util.List;

public class TaskIntegrationTest extends BaseCrudIntegrationTest<TaskResource, Task, TaskDTO> {

    @Before
    public void setUp() throws Exception {
        super.setUp();
    }

    @After
    public void tearDown() throws Exception {
        super.tearDown();
    }

    @Override
    protected Class<TaskResource> getResourceClass() {
        return TaskResource.class;
    }

    @Override
    protected Class<Task> getEntityClass() {
        return Task.class;
    }

    @Override
    protected Task createNewEntity(String name) {
        return new Task(name);
    }

    @Override
    protected Class<TaskDTO> getDtoClass() {
        return TaskDTO.class;
    }

    @Override
    protected GenericType<List<TaskDTO>> getDtosGenericType() {
        return new GenericType<List<TaskDTO>>() {
        };
    }
}
