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
        entity.setFrom(from);
        entity.setTo(to);
        entity.setFromConnector("A");
        entity.setToConnector("B");
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
