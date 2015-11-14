package custom.integration;

import custom.domain.Process;
import custom.dto.process.ProcessDTO;
import custom.resources.ProcessResource;
import org.junit.*;

import javax.ws.rs.core.GenericType;
import java.util.List;

public class ProcessIntegrationTest extends BaseCrudIntegrationTest<ProcessResource, Process, ProcessDTO> {

    @Before
    public void setUp() throws Exception {
        super.setUp();
        setResourceClass(ProcessResource.class);
        setEntityClass(Process.class);
        setDtoClass(ProcessDTO.class);
        setDtoGenericType(new GenericType<List<ProcessDTO>>() {});
    }

    @After
    public void tearDown() throws Exception {
        super.tearDown();
    }

    @Override
    protected Process createNewEntity(String name) {
        return new Process(name);
    }

}
