package custom.dto.task;

import custom.domain.ProcessTask;
import custom.domain.Task;
import custom.domain.TaskConnection;
import custom.dto.BaseDTO;
import custom.dto.process.ProcessDTOinTaskDTO;

import java.util.HashSet;
import java.util.Set;

public class TaskDTO implements BaseDTO {
    private long id;
    private String name;
    private String category;
    private String loc;
    private Set<ProcessDTOinTaskDTO> processes = new HashSet<>();
    private Set<Long> fromConnections;
    private Set<Long> toConnections;

    public TaskDTO() {
    }

    public TaskDTO(Task task) {
        this.id = task.getId();
        this.name = task.getName();
        this.category = task.getCategory();
        this.loc = task.getLoc();
        for (ProcessTask assoc : task.getProcessAssoc()) {
            this.processes.add(new ProcessDTOinTaskDTO(assoc.getProcess(), assoc.getPosition()));
        }
        fromConnections = fillConnections(task.getFromConnections());
        toConnections = fillConnections(task.getToConnections());
    }

    private Set<Long> fillConnections(Set<TaskConnection> connections) {
        if (connections == null || connections.isEmpty()) {
            return null;
        }
        Set<Long> connectionsDto = new HashSet<>();
        for (TaskConnection connection : connections) {
            connectionsDto.add(connection.getId());
        }
        return connectionsDto;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getLoc() {
        return loc;
    }

    public void setLoc(String loc) {
        this.loc = loc;
    }

    public Set<ProcessDTOinTaskDTO> getProcesses() {
        return processes;
    }

    public void setProcesses(Set<ProcessDTOinTaskDTO> processes) {
        this.processes = processes;
    }

    public Set<Long> getFromConnections() {
        return fromConnections;
    }

    public void setFromConnections(Set<Long> fromConnections) {
        this.fromConnections = fromConnections;
    }

    public Set<Long> getToConnections() {
        return toConnections;
    }

    public void setToConnections(Set<Long> toConnections) {
        this.toConnections = toConnections;
    }
}
