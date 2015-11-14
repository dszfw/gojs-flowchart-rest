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
    }

    @After
    public void tearDown() throws Exception {
        super.tearDown();
    }

    @Override
    protected Class<ProcessResource> getResourceClass() {
        return ProcessResource.class;
    }

    @Override
    protected Class<Process> getEntityClass() {
        return Process.class;
    }

    @Override
    protected Process createNewEntity(String name) {
        return new Process(name);
    }

    @Override
    protected Class<ProcessDTO> getDtoClass() {
        return ProcessDTO.class;
    }

    @Override
    protected GenericType<List<ProcessDTO>> getDtosGenericType() {
        return new GenericType<List<ProcessDTO>>() {
        };
    }

}
