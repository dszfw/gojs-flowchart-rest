package com.example.helloworld.dto.process;

import com.example.helloworld.core.Process;
import com.example.helloworld.core.ProcessTask;
import com.example.helloworld.core.TaskConnection;
import com.example.helloworld.dto.TaskConnectionDTO;
import com.example.helloworld.dto.task.TaskDTOinProcessDTO;

import java.util.HashSet;
import java.util.Set;

public class ProcessDTO {
    private long id;
    private String name;
    private Set<TaskDTOinProcessDTO> tasks = new HashSet<>();
    private Set<TaskConnectionDTO> connections = new HashSet<>();

    public ProcessDTO() {
    }

    public ProcessDTO(Process process) {
        this.id = process.getId();
        this.name = process.getName();
        for (ProcessTask assoc : process.getTaskAssoc()) {
            this.tasks.add(new TaskDTOinProcessDTO(assoc.getTask(), assoc.getPosition()));
        }
        for (TaskConnection connection : process.getConnections()) {
            this.connections.add(new TaskConnectionDTO(connection));
        }
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

    public Set<TaskDTOinProcessDTO> getTasks() {
        return tasks;
    }

    public void setTasks(Set<TaskDTOinProcessDTO> tasks) {
        this.tasks = tasks;
    }

    public Set<TaskConnectionDTO> getConnections() {
        return connections;
    }

    public void setConnections(Set<TaskConnectionDTO> connections) {
        this.connections = connections;
    }
}
