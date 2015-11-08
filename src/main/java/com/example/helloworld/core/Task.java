package com.example.helloworld.core;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.CascadeType.REMOVE;
import static javax.persistence.FetchType.LAZY;

@Entity
@Table(name = "tasks")
@NamedQueries({
        @NamedQuery(
                name = "com.example.helloworld.core.Task.findAll",
                query = "SELECT t FROM Task t"
        )
})
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id", scope = Task.class)
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "name", nullable = true)
    private String name;

    @Column(name = "category", nullable = true)
    private String category;

    @Column(name = "loc", nullable = true)
    private String loc;

    @OneToMany(mappedBy = "task", cascade = ALL)
    private List<ProcessTask> processAssoc = new ArrayList<>();

    @OneToMany(fetch = LAZY, mappedBy = "from", cascade = REMOVE)
    @Column(name = "fromConnections")
    private Set<TaskConnection> fromConnections = new HashSet<>();

    @OneToMany(fetch = LAZY, mappedBy = "to", cascade = REMOVE)
    @Column(name = "toConnections")
    private Set<TaskConnection> toConnections = new HashSet<>();

    public Task() {
    }

    public Task(String name) {
        this.name = name;
    }

    public Set<TaskConnection> getFromConnections() {
        return fromConnections;
    }

    public void setFromConnections(Set<TaskConnection> fromConnections) {
        this.fromConnections = fromConnections;
    }

    public Set<TaskConnection> getToConnections() {
        return toConnections;
    }

    public void setToConnections(Set<TaskConnection> toConnections) {
        this.toConnections = toConnections;
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

    public List<ProcessTask> getProcessAssoc() {
        return processAssoc;
    }

    public void setProcessAssoc(List<ProcessTask> processAssoc) {
        this.processAssoc = processAssoc;
    }

}
