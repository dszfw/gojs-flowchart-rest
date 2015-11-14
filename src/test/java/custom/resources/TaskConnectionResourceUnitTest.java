package custom.resources;

import custom.domain.Process;
import custom.domain.Task;
import custom.domain.TaskConnection;
import custom.dto.TaskConnectionDTO;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import javax.ws.rs.core.GenericType;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class TaskConnectionResourceUnitTest extends
        BaseResourceUnitTest<TaskConnectionResource, TaskConnection, TaskConnectionDTO> {

    @Before
    public void setUp() {
        super.setUp();
        setResourceClass(TaskConnectionResource.class);
        setEntityClass(TaskConnection.class);
        setDtoClass(TaskConnectionDTO.class);
        setDtoGenericType(new GenericType<List<TaskConnectionDTO>>() {});
        entity = new TaskConnection();
        dao = taskConnectionDAO;
    }

    @After
    public void tearDown() {
        super.tearDown();
    }

    @Override
    protected void givenEntity(String name) {
        super.givenEntity(name);
        Process process = new Process();
        process.setId(1000);
        Task to = new Task();
        to.setId(1002);
        Task from = new Task();
        from.setId(1003);

        entity.setProcess(process);
        entity.setFrom(from);
        entity.setTo(to);
        entity.setFromConnector("A");
        entity.setToConnector("B");
    }

    @Override
    protected TaskConnection createNewEntity(String name) {
        return new TaskConnection(name);
    }

}
