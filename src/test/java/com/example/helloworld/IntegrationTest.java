package com.example.helloworld;

import com.example.helloworld.core.*;
import com.example.helloworld.api.Saying;
import com.example.helloworld.core.Process;
import com.google.common.base.Optional;
import io.dropwizard.testing.ConfigOverride;
import io.dropwizard.testing.ResourceHelpers;
import io.dropwizard.testing.junit.DropwizardAppRule;
import org.junit.*;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import java.io.File;
import java.io.IOException;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON_TYPE;
import static org.assertj.core.api.Assertions.assertThat;

public class IntegrationTest {

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

    @Test
    public void testHelloWorld() throws Exception {
        final Optional<String> name = Optional.fromNullable("Dr. IntegrationTest");
        final Saying saying = client.target("http://localhost:" + RULE.getLocalPort() + "/hello-world")
                .queryParam("name", name.get())
                .request()
                .get(Saying.class);
        assertThat(saying.getContent()).isEqualTo(RULE.getConfiguration().buildTemplate().render(name));
    }

    @Test
    public void testPostPerson() throws Exception {
        final Person person = new Person("Dr. IntegrationTest", "Chief Wizard");
        final Person newPerson = client.target("http://localhost:" + RULE.getLocalPort() + "/people")
                .request()
                .post(Entity.entity(person, APPLICATION_JSON_TYPE))
                .readEntity(Person.class);
        assertThat(newPerson.getId()).isNotNull();
        assertThat(newPerson.getFullName()).isEqualTo(person.getFullName());
        assertThat(newPerson.getJobTitle()).isEqualTo(person.getJobTitle());
    }

    @Test
    public void postProcess() throws Exception {
        final Process process = new Process("First process");
        final Process newProcess = client.target("http://localhost:" + RULE.getLocalPort() + "/processes")
                .request()
                .post(Entity.entity(process, APPLICATION_JSON_TYPE))
                .readEntity(Process.class);
        assertThat(newProcess.getId()).isNotNull();
        assertThat(newProcess.getName()).isEqualTo(process.getName());
    }

    @Test
    public void postTask() throws Exception {
        final Task task = new Task("First task");
        final Task newTask = client.target("http://localhost:" + RULE.getLocalPort() + "/tasks")
                .request()
                .post(Entity.entity(task, APPLICATION_JSON_TYPE))
                .readEntity(Task.class);
        assertThat(newTask.getId()).isNotNull();
        assertThat(newTask.getName()).isEqualTo(task.getName());
    }

    @Test
    public void postProcessWithTasks() throws Exception {
        final Process process = new Process("Process with tasks");
        int tasksCount = 10;
        createTasksForProcess(process, tasksCount);
        final Process newProcess = client.target("http://localhost:" + RULE.getLocalPort() + "/processes")
                .request()
                .post(Entity.entity(process, APPLICATION_JSON_TYPE))
                .readEntity(Process.class);
        assertThat(newProcess.getId()).isNotNull();
        assertThat(newProcess.getName()).isEqualTo(process.getName());
        assertThat(newProcess.getTasks()).isNotNull();
        assertThat(newProcess.getTasks().size()).isEqualTo(tasksCount);
    }

    private void createTasksForProcess(Process process, int count) {
        for (int i = 0; i < count; i++) {
            Task task = new Task("Task " + i);

            final Task responseTask = client.target("http://localhost:" + RULE.getLocalPort() + "/tasks")
                    .request()
                    .post(Entity.entity(task, APPLICATION_JSON_TYPE))
                    .readEntity(Task.class);

            Task t = new Task();
            t.setId(responseTask.getId());
            process.getTasks().add(t);
        }
    }
}
