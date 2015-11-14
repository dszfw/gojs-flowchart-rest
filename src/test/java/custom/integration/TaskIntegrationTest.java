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

}
