package custom.resources;

import custom.domain.TaskConnection;
import custom.dto.TaskConnectionDTO;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import javax.ws.rs.core.GenericType;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class TaskConnectionResourceTest extends
        BaseResourceTest<TaskConnectionResource, TaskConnection, TaskConnectionDTO> {

    @Before
    public void setUp() {
        super.setUp();
        entity = new TaskConnection();
        dao = taskConnectionDAO;
    }

    @After
    public void tearDown() {
        super.tearDown();
    }

    @Override
    protected Class<TaskConnectionResource> getResourceClass() {
        return TaskConnectionResource.class;
    }

    @Override
    protected Class<TaskConnection> getEntityClass() {
        return TaskConnection.class;
    }

    @Override
    protected TaskConnection createNewEntity(String name) {
        return new TaskConnection(name);
    }

    @Override
    protected Class<TaskConnectionDTO> getDtoClass() {
        return TaskConnectionDTO.class;
    }

    @Override
    protected GenericType<List<TaskConnectionDTO>> getDtosGenericType() {
        return new GenericType<List<TaskConnectionDTO>>() {
        };
    }
}
