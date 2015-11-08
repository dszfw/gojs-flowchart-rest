package com.example.helloworld.core;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

import static javax.persistence.FetchType.LAZY;

@Entity
@Table(name = "processes")
@NamedQueries({
        @NamedQuery(
                name = "com.example.helloworld.core.Process.findByIdJoinTasks",
                query = "SELECT DISTINCT p FROM Process p LEFT JOIN FETCH p.tasks WHERE p.id = :id"
        ),
        @NamedQuery(
                name = "com.example.helloworld.core.Process.findAll",
                query = "SELECT p FROM Process p"
        ),
        @NamedQuery(
                name = "com.example.helloworld.core.Process.findAllJoinTasks",
                query = "SELECT DISTINCT p FROM Process p LEFT JOIN FETCH p.tasks"
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

    @ManyToMany(fetch = LAZY)
    @JoinTable(name = "process_task",
            joinColumns = @JoinColumn(name = "processId"),
            inverseJoinColumns = @JoinColumn(name = "taskId"))
    @JsonIdentityReference
    private Set<Task> tasks = new HashSet<>();

    @OneToMany(fetch = LAZY, mappedBy = "process", cascade = CascadeType.ALL)
    @Column(name = "orders")
    private Set<TaskOrder> orders = new HashSet<>();

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

    public Set<Task> getTasks() {
        return tasks;
    }

    public void setTasks(Set<Task> tasks) {
        this.tasks = tasks;
    }

    public Set<TaskOrder> getOrders() {
        return orders;
    }

    public void setOrders(Set<TaskOrder> orders) {
        this.orders = orders;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Process)) return false;

        Process process = (Process) o;

        if (getId() != process.getId()) return false;
        return !(getName() != null ? !getName().equals(process.getName()) : process.getName() != null);

    }

    @Override
    public int hashCode() {
        int result = (int) (getId() ^ (getId() >>> 32));
        result = 31 * result + (getName() != null ? getName().hashCode() : 0);
        return result;
    }
}
