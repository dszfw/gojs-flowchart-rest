package custom.integration;

import com.example.helloworld.HelloWorldApplication;
import com.example.helloworld.HelloWorldConfiguration;
import custom.domain.Process;
import custom.dto.process.ProcessDTO;
import custom.resources.ProcessResource;
import io.dropwizard.testing.ConfigOverride;
import io.dropwizard.testing.ResourceHelpers;
import io.dropwizard.testing.junit.DropwizardAppRule;
import org.junit.*;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.GenericType;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class ProcessCrudIntegrationTest extends BaseCrudTest <ProcessResource, Process, ProcessDTO> {

    private static final String TMP_FILE = createTempFile();
    private static final String CONFIG_PATH = ResourceHelpers.resourceFilePath("test-example.yml");

    @ClassRule
    public static final DropwizardAppRule<HelloWorldConfiguration> RULE = new DropwizardAppRule<>(
            HelloWorldApplication.class, CONFIG_PATH, ConfigOverride.config("database.url", "jdbc:h2:" + TMP_FILE));


    @BeforeClass
    public static void migrateDb() throws Exception {
        RULE.getApplication().run("db", "migrate", CONFIG_PATH);
    }

    @Before
    public void setUp() throws Exception {
        super.setUp();
        client = ClientBuilder.newClient();
        target = client.target("http://localhost:" + RULE.getLocalPort());
    }


    @After
    public void tearDown() throws Exception {
        client.close();
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
        return new GenericType<List<ProcessDTO>>(){};
    }

    private static String createTempFile() {
        try {
            return File.createTempFile("test-example", null).getAbsolutePath();
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }
}
