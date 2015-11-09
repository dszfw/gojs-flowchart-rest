package com.example.helloworld.dto.task;

import com.example.helloworld.core.ProcessTask;
import com.example.helloworld.core.Task;
import com.example.helloworld.dto.process.ProcessDTOinTaskDTO;

import java.util.HashSet;
import java.util.Set;

public class TaskDTO {
    private long id;
    private String name;
    private String category;
    private String loc;
    private Set<ProcessDTOinTaskDTO> processes = new HashSet<>();

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
}
