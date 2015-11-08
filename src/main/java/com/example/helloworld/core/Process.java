package com.example.helloworld.core;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "processes")
@NamedQueries({
        @NamedQuery(
                name = "com.example.helloworld.core.Process.findAll",
                query = "SELECT p FROM Process p"
        )
})
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id", scope = Process.class)
public class Process {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "name", nullable = true)
    private String name;

    @OneToMany(mappedBy = "process", cascade = CascadeType.ALL)
    private List<ProcessTask> taskAssoc = new ArrayList<>();

    public Process() {
    }

    public Process(String name) {
        this.name = name;
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

    public List<ProcessTask> getTaskAssoc() {
        return taskAssoc;
    }

    public void setTaskAssoc(List<ProcessTask> taskAssoc) {
        this.taskAssoc = taskAssoc;
    }

}
