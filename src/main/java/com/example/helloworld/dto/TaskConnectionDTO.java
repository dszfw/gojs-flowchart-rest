package com.example.helloworld.dto;

import com.example.helloworld.core.Process;
import com.example.helloworld.core.TaskConnection;

public class TaskConnectionDTO {
    private long id;
    private String name;
    private long from;
    private long to;
    private long process;
    private String fromConnector;
    private String toConnector;

    public TaskConnectionDTO() {
    }

    public TaskConnectionDTO(TaskConnection connection) {
        this.id = connection.getId();
        this.name = connection.getName();
        this.from = connection.getFrom().getId();
        this.to = connection.getTo().getId();
        this.process = connection.getProcess().getId();
        this.fromConnector = connection.getFromConnector();
        this.toConnector = connection.getToConnector();
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

    public long getFrom() {
        return from;
    }

    public void setFrom(long from) {
        this.from = from;
    }

    public long getTo() {
        return to;
    }

    public void setTo(long to) {
        this.to = to;
    }

    public long getProcess() {
        return process;
    }

    public void setProcess(long process) {
        this.process = process;
    }

    public String getFromConnector() {
        return fromConnector;
    }

    public void setFromConnector(String fromConnector) {
        this.fromConnector = fromConnector;
    }

    public String getToConnector() {
        return toConnector;
    }

    public void setToConnector(String toConnector) {
        this.toConnector = toConnector;
    }
}
