package custom.dto.process;

import custom.domain.Process;

public class ProcessDTOinTaskDTO {

    private Long id;
    private Long position;

    public ProcessDTOinTaskDTO() {
    }

    public ProcessDTOinTaskDTO(Process process, Long position) {
        this.id = process.getId();
        this.position = position;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPosition() {
        return position;
    }

    public void setPosition(Long position) {
        this.position = position;
    }
}
