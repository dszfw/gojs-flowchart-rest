package com.example.helloworld.core;

import java.io.Serializable;

public class ProcessTaskId implements Serializable {
    private long process;
    private long task;

    public long getProcess() {
        return process;
    }

    public void setProcess(long process) {
        this.process = process;
    }

    public long getTask() {
        return task;
    }

    public void setTask(long task) {
        this.task = task;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProcessTaskId)) return false;

        ProcessTaskId that = (ProcessTaskId) o;

        if (getProcess() != that.getProcess()) return false;
        return getTask() == that.getTask();

    }

    @Override
    public int hashCode() {
        int result = (int) (getProcess() ^ (getProcess() >>> 32));
        result = 31 * result + (int) (getTask() ^ (getTask() >>> 32));
        return result;
    }
}
