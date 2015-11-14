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
        setResourceClass(ProcessResource.class);
        setEntityClass(Process.class);
        setDtoClass(ProcessDTO.class);
        setDtoGenericType(new GenericType<List<ProcessDTO>>() {});
        entity = new Process();
        dao = processDAO;
    }

    @After
    public void tearDown() {
        super.tearDown();
    }

    @Override
    protected Process createNewEntity(String name) {
        return new Process(name);
    }

}
