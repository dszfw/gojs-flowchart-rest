package custom.integration;

import custom.domain.*;
import custom.domain.Process;
import custom.dto.TaskConnectionDTO;
import custom.resources.TaskConnectionResource;
import org.junit.After;
import org.junit.Before;

import javax.ws.rs.core.GenericType;
import java.util.List;

public class TaskConnectionIntegrationTest extends BaseCrudIntegrationTest<TaskConnectionResource, TaskConnection, TaskConnectionDTO> {

    @Before
    public void setUp() throws Exception {
        super.setUp();
        setResourceClass(TaskConnectionResource.class);
        setEntityClass(TaskConnection.class);
        setDtoClass(TaskConnectionDTO.class);
        setDtoGenericType(new GenericType<List<TaskConnectionDTO>>() {
        });
    }

    @After
    public void tearDown() throws Exception {
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
        entity.setFromTask(from);
        entity.setToTask(to);
        entity.setFromConnector("A");
        entity.setToConnector("B");
    }

    @Override
    protected TaskConnection createNewEntity(String name) {
        return new TaskConnection(name);
    }

    /*    @Test
    public void createTaskWithConnections_connectionsExist_created() {
        givenEntity("New task with connection attached");
        givenConnections();
        whenCreateRequestPerform();
        thenSuccess(CREATED);
        thenResponseEntityEqualGiven();
        thenIdWasAdded();
        givenEntityId(dto.getId());
        whenGetRequestPerform();
        thenConnectionsWereAttached();
        thenConnectionsEqualGiven();
    }

    private void thenConnectionsEqualGiven() {
//        assertThat(dto.get)
    }

    private void thenConnectionsWereAttached() {
        assertThat(dto.getFromConnections()).isNotEmpty();
        assertThat(dto.getToConnections()).isNotEmpty();

    }

    private void givenConnections() {
        from = new TaskConnection();
        fromConnectionId = 1010L;
        from.setId(fromConnectionId);
        entity.getFromConnections().add(from);
        to = new TaskConnection();
        toConnectionId = 1011L;
        to.setId(toConnectionId);
        entity.getToConnections().add(to);
    }
*/
}
