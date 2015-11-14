package custom;

import custom.domain.Process;

import com.example.helloworld.HelloWorldApplication;
import com.example.helloworld.HelloWorldConfiguration;
import custom.dto.process.ProcessDTO;
import custom.dto.task.TaskDTO;
import io.dropwizard.testing.ConfigOverride;
import io.dropwizard.testing.ResourceHelpers;
import io.dropwizard.testing.junit.DropwizardAppRule;
import custom.domain.ProcessTask;
import custom.domain.Task;
import org.junit.*;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;
import java.io.File;
import java.io.IOException;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON_TYPE;
import static org.assertj.core.api.Assertions.assertThat;

public class CustomIntegrationTest {

    private static final String TMP_FILE = createTempFile();
    private static final String CONFIG_PATH = ResourceHelpers.resourceFilePath("test-example.yml");

    @ClassRule
    public static final DropwizardAppRule<HelloWorldConfiguration> RULE = new DropwizardAppRule<>(
            HelloWorldApplication.class, CONFIG_PATH,
            ConfigOverride.config("database.url", "jdbc:h2:" + TMP_FILE));

    private Client client;

    @BeforeClass
    public static void migrateDb() throws Exception {
        RULE.getApplication().run("db", "migrate", CONFIG_PATH);
    }

    @Before
    public void setUp() throws Exception {
        client = ClientBuilder.newClient();
    }

    @After
    public void tearDown() throws Exception {
        client.close();
    }

    private static String createTempFile() {
        try {
            return File.createTempFile("test-example", null).getAbsolutePath();
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    /*@Test
    public void postProcess() throws Exception {
        final Process process = new Process("First process");
        final ProcessDTO processDTO = client.target("http://localhost:" + RULE.getLocalPort() + "/processes")
                .request()
                .post(Entity.entity(process, APPLICATION_JSON_TYPE))
                .readEntity(ProcessDTO.class);
        assertThat(processDTO.getId()).isNotNull();
        assertThat(processDTO.getName()).isEqualTo(process.getName());
    }

    @Test
    public void postTask() throws Exception {
        final Task task = new Task("First task");
        final TaskDTO taskDTO = client.target("http://localhost:" + RULE.getLocalPort() + "/tasks")
                .request()
                .post(Entity.entity(task, APPLICATION_JSON_TYPE))
                .readEntity(TaskDTO.class);
        assertThat(taskDTO.getId()).isNotNull();
        assertThat(taskDTO.getName()).isEqualTo(task.getName());
    }*/

    @Test
    public void postProcessWithTasks() throws Exception {
        final Process process = new Process("Process with tasks");
        int tasksCount = 10;
        createTasksForProcess(process, tasksCount);
        final ProcessDTO processDTO = client.target("http://localhost:" + RULE.getLocalPort() + "/processes")
                .request()
                .post(Entity.entity(process, APPLICATION_JSON_TYPE))
                .readEntity(ProcessDTO.class);
        // TODO check Response
        assertThat(processDTO.getId()).isNotNull();
        assertThat(processDTO.getName()).isEqualTo(process.getName());
        assertThat(processDTO.getTasks()).isNotEmpty();
        assertThat(processDTO.getTasks().size()).isEqualTo(tasksCount);
    }

    private void createTasksForProcess(Process process, int count) {
        for (int i = 1; i <= count; i++) {
            Task task = new Task("Task " + i);
            final TaskDTO taskDTO = client.target("http://localhost:" + RULE.getLocalPort() + "/tasks")
                    .request()
                    .post(Entity.entity(task, APPLICATION_JSON_TYPE))
                    .readEntity(TaskDTO.class);

            ProcessTask assoc = new ProcessTask();
            task.setId(taskDTO.getId());
            assoc.setTask(task);
            assoc.setPosition(i);
            process.getTaskAssoc().add(assoc);
        }
    }

}
