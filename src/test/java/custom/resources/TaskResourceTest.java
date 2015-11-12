package custom.resources;

import custom.domain.*;
import custom.dto.task.TaskDTO;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import javax.ws.rs.core.GenericType;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class TaskResourceTest extends BaseResourceTest<TaskResource, Task, TaskDTO>{

    @Before
    public void setUp() {
        super.setUp();
        entity = new Task();
        dao = taskDAO;
    }

    @After
    public void tearDown() {
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
        return new GenericType<List<TaskDTO>>() {};
    }
}
