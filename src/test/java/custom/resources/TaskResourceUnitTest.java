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
public class TaskResourceUnitTest extends BaseResourceUnitTest<TaskResource, Task, TaskDTO> {

    @Before
    public void setUp() {
        super.setUp();
        setResourceClass(TaskResource.class);
        setEntityClass(Task.class);
        setDtoClass(TaskDTO.class);
        setDtoGenericType(new GenericType<List<TaskDTO>>(){});
        entity = new Task();
        dao = taskDAO;
    }

    @After
    public void tearDown() {
        super.tearDown();
    }

    @Override
    protected Task createNewEntity(String name) {
        return new Task(name);
    }

}
