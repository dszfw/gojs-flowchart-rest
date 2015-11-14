package custom.resources;

import custom.domain.Process;
import custom.dto.process.ProcessDTO;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import javax.ws.rs.core.GenericType;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class ProcessResourceUnitTest extends BaseResourceUnitTest<ProcessResource, Process, ProcessDTO> {

    @Before
    public void setUp() {
        super.setUp();
        entity = new Process();
        dao = processDAO;
    }

    @After
    public void tearDown() {
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
    protected Class<ProcessDTO> getDtoClass() {
        return ProcessDTO.class;
    }

    @Override
    protected GenericType<List<ProcessDTO>> getDtosGenericType() {
        return new GenericType<List<ProcessDTO>>(){};
    }

    @Override
    protected Process createNewEntity(String name) {
        return new Process(name);
    }

}
