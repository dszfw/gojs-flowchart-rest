package com.example.helloworld.dto;

import com.example.helloworld.core.Task;

public class TaskDTO {
    private long id;
    private String name;
    private String category;
    private String loc;

    public TaskDTO() {
    }

    public TaskDTO(Task task) {
        this.id = task.getId();
        this.name = task.getName();
        this.category = task.getCategory();
        this.loc = task.getLoc();
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
}
