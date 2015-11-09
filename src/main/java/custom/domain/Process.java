package custom.domain;

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
@Table(name = "processes")
@NamedQueries({
        @NamedQuery(
                name = "custom.domain.Process.findAll",
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

    @OneToMany(mappedBy = "process", cascade = ALL)
    private List<ProcessTask> taskAssoc = new ArrayList<>();

    @OneToMany(fetch = LAZY, mappedBy = "process", cascade = REMOVE)
    @Column(name = "connections")
    private Set<TaskConnection> connections = new HashSet<>();

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

    public Set<TaskConnection> getConnections() {
        return connections;
    }

    public void setConnections(Set<TaskConnection> connections) {
        this.connections = connections;
    }
}
