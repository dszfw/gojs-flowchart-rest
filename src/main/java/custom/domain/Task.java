package custom.domain;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import custom.dto.BaseDTO;
import custom.dto.task.TaskDTO;

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
                name = "custom.domain.Task.findAll",
                query = "SELECT t FROM Task t"
        )
})
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id", scope = Task.class)
public class Task extends BaseEntity {

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
        super(name);
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

    @Override
    public BaseDTO createDto() {
        return new TaskDTO(this);
    }
}
