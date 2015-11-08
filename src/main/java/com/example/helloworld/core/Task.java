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
@Table(name = "tasks")
@NamedQueries({
        @NamedQuery(
                name = "com.example.helloworld.core.Task.findByIdJoinProcesses",
                query = "SELECT DISTINCT t FROM Task t LEFT JOIN FETCH t.processes where t.id = :id"
        ),
        @NamedQuery(
                name = "com.example.helloworld.core.Task.findAll",
                query = "SELECT t FROM Task t"
        ),
        @NamedQuery(
                name = "com.example.helloworld.core.Task.findAllJoinProcesses",
                query = "SELECT DISTINCT t FROM Task t LEFT JOIN FETCH t.processes"
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

    @ManyToMany(fetch = LAZY)
    @JoinTable(name = "process_task",
            joinColumns = @JoinColumn(name = "taskId"),
            inverseJoinColumns = @JoinColumn(name = "processId"))
    @JsonIdentityReference
    private Set<Process> processes = new HashSet<>();

    @OneToMany(fetch = LAZY, mappedBy = "task", cascade = CascadeType.ALL)
    @Column(name = "orders")
    private Set<TaskOrder> orders = new HashSet<>();

    @OneToMany(fetch = LAZY, mappedBy = "from", cascade = CascadeType.ALL)
    @Column(name = "fromConnections")
    private Set<TaskConnection> fromConnections = new HashSet<>();

    @OneToMany(fetch = LAZY, mappedBy = "to", cascade = CascadeType.ALL)
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

    public Set<TaskOrder> getOrders() {
        return orders;
    }

    public void setOrders(Set<TaskOrder> orders) {
        this.orders = orders;
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

    public Set<Process> getProcesses() {
        return processes;
    }

    public void setProcesses(Set<Process> processes) {
        this.processes = processes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Task)) return false;

        Task task = (Task) o;

        if (getId() != task.getId()) return false;
        if (getName() != null ? !getName().equals(task.getName()) : task.getName() != null) return false;
        if (getCategory() != null ? !getCategory().equals(task.getCategory()) : task.getCategory() != null)
            return false;
        return !(getLoc() != null ? !getLoc().equals(task.getLoc()) : task.getLoc() != null);

    }

    @Override
    public int hashCode() {
        int result = (int) (getId() ^ (getId() >>> 32));
        result = 31 * result + (getName() != null ? getName().hashCode() : 0);
        result = 31 * result + (getCategory() != null ? getCategory().hashCode() : 0);
        result = 31 * result + (getLoc() != null ? getLoc().hashCode() : 0);
        return result;
    }
}
