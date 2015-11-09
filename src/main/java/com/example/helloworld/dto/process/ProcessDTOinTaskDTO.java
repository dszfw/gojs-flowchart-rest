package com.example.helloworld.dto.process;

import com.example.helloworld.core.Process;

public class ProcessDTOinTaskDTO {
    private long id;
    private long position;

    public ProcessDTOinTaskDTO() {
    }

    public ProcessDTOinTaskDTO(Process process, long position) {
        this.id = process.getId();
        this.position = position;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getPosition() {
        return position;
    }

    public void setPosition(long position) {
        this.position = position;
    }
}
