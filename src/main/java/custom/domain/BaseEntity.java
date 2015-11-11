package custom.domain;

import custom.dto.BaseDTO;

import javax.persistence.*;
import java.io.Serializable;

@MappedSuperclass
abstract public class BaseEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "name", nullable = true)
    private String name;

    public BaseEntity() {
    }

    public BaseEntity(String name) {
        this.name = name;
    }

    abstract public BaseDTO createDto();

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
}
