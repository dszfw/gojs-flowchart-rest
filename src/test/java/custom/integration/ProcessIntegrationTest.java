package custom.integration;

import custom.domain.Process;
import custom.domain.ProcessTask;
import custom.domain.Task;
import custom.dto.process.ProcessDTO;
import custom.dto.task.TaskDTOinProcessDTO;
import custom.resources.ProcessResource;
import org.junit.*;

import javax.ws.rs.core.GenericType;
import java.util.*;

import static javax.ws.rs.core.Response.Status.CREATED;
import static javax.ws.rs.core.Response.Status.NOT_FOUND;
import static javax.ws.rs.core.Response.Status.OK;
import static org.assertj.core.api.Assertions.assertThat;

public class ProcessIntegrationTest extends BaseCrudIntegrationTest<ProcessResource, Process, ProcessDTO> {

    private Set<Task> tasks;
    private Map<Long, Long> taskIdPositionMap;
    private Task task;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        tasks = null;
        taskIdPositionMap = null;
        task = null;
        setResourceClass(ProcessResource.class);
        setEntityClass(Process.class);
        setDtoClass(ProcessDTO.class);
        setDtoGenericType(new GenericType<List<ProcessDTO>>() {
        });
    }

    @After
    public void tearDown() throws Exception {
        super.tearDown();
    }

    @Override
    protected Process createNewEntity(String name) {
        return new Process(name);
    }

    @Test
    public void createProcessWithTasks_tasksExist_created() {
        givenEntity("New process with tasks attached");
        givenTasksAttach();
        whenCreateRequestPerform();
        thenSuccess(CREATED);
        thenResponseEntityEqualGiven();
        thenIdWasAdded();
        thenTasksWereAttached();
    }

    @Test
    public void createProcessWithTask_taskNotExist_notFound() {
        givenEntity("New process with task attached");
        givenNonExistTaskAttached();
        whenCreateRequestPerform();
        thenError(NOT_FOUND);
    }

    @Test
    public void getProcessWithTasks() {
        givenEntityId(1000);
        givenEntity("Some Process");
        whenGetRequestPerform();
        thenSuccess(OK);
        thenResponseEntityEqualGiven();
        thenProcessContainsTasks();
    }

    @Test
    public void updateTasksPositionsInProcess() {
        givenEntityId(1000);
        whenGetRequestPerform();
        thenSuccess(OK);
        thenProcessContainsTasks();
        // process name will not be changed, leave the same
        givenEntity(dto.getName());
        givenNewPositionsForTasks();
        whenUpdateRequestPerform();
        thenSuccess(OK);
        thenPositionsChanged();
    }

    private void thenPositionsChanged() {
        assertThat(dto.getTasks()).isNotEmpty();
        assertThat(dto.getTasks().size()).isEqualTo(taskIdPositionMap.size());
        for (TaskDTOinProcessDTO taskDto : dto.getTasks()) {
            assertThat(taskDto.getPosition()).isNotNull();
            assertThat(taskDto.getPosition()).
                    isNotEqualTo(taskIdPositionMap.get(taskDto.getId()));
        }
    }

    private void givenNewPositionsForTasks() {
        // old positions
        taskIdPositionMap = new HashMap<>();
        for (TaskDTOinProcessDTO taskDto : dto.getTasks()) {
            taskIdPositionMap.put(taskDto.getId(), taskDto.getPosition());
            Task task = new Task();
            task.setId(taskDto.getId());
            // new position
            ProcessTask assoc = new ProcessTask(entity, task, taskDto.getPosition() + 1);
            entity.getTaskAssoc().add(assoc);
        }
    }

    private void thenProcessContainsTasks() {
        assertThat(dto.getTasks()).isNotEmpty();
    }

    private void givenNonExistTaskAttached() {
        task = new Task();
        task.setId(System.nanoTime());
        entity.getTaskAssoc().add(new ProcessTask(entity, task, 1L));
    }

    private void thenTasksWereAttached() {
        assertThat(dto.getTasks()).isNotEmpty();
        assertThat(dto.getTasks().size()).isEqualTo(tasks.size());
        for (TaskDTOinProcessDTO taskDto : dto.getTasks()) {
            assertThat(taskIdPositionMap.keySet()).contains(taskDto.getId());
            assertThat(taskIdPositionMap.get(taskDto.getId()))
                    .isEqualTo(taskDto.getPosition());
        }
    }

    private void givenTasksAttach() {
        tasks = new HashSet<>();
        taskIdPositionMap = new HashMap<>();
        long position = 0;
        for (int i = 1001; i <= 1003; i++) {
            Task task = new Task();
            task.setId(i);
            tasks.add(task);
            taskIdPositionMap.put(task.getId(), position);

            ProcessTask processTaskAssoc = new ProcessTask(entity, task, position);
            entity.getTaskAssoc().add(processTaskAssoc);
            position++;
        }
    }
}
