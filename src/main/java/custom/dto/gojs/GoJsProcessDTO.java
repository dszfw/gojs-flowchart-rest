package custom.dto.gojs;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Set;


@JsonIgnoreProperties(ignoreUnknown = true)
public class GoJsProcessDTO {


    private Set<GoJsTaskDTO> nodeDataArray;
    private Set<GoJsTaskConnectionDTO> linkDataArray;

    public GoJsProcessDTO() {
    }

    public Set<GoJsTaskDTO> getNodeDataArray() {
        return nodeDataArray;
    }

    public void setNodeDataArray(Set<GoJsTaskDTO> nodeDataArray) {
        this.nodeDataArray = nodeDataArray;
    }

    public Set<GoJsTaskConnectionDTO> getLinkDataArray() {
        return linkDataArray;
    }

    public void setLinkDataArray(Set<GoJsTaskConnectionDTO> linkDataArray) {
        this.linkDataArray = linkDataArray;
    }
}
