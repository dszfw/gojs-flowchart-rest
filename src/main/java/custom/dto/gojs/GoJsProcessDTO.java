package custom.dto.gojs;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Map;
import java.util.Set;


@JsonIgnoreProperties(ignoreUnknown = true)
public class GoJsProcessDTO {

//    private String linkToPortIdProperty;
//    private Set<Map<String, String>> nodeDataArray;
    private Set<Map<String, String>> linkDataArray;

    public GoJsProcessDTO() {
    }

//    public String getLinkToPortIdProperty() {
//        return linkToPortIdProperty;
//    }
//
//    public void setLinkToPortIdProperty(String linkToPortIdProperty) {
//        this.linkToPortIdProperty = linkToPortIdProperty;
//    }

    //    public Set<Map<String, String>> getNodeDataArray() {
//        return nodeDataArray;
//    }
//
//    public void setNodeDataArray(Set<Map<String, String>> nodeDataArray) {
//        this.nodeDataArray = nodeDataArray;
//    }
//
    public Set<Map<String, String>> getLinkDataArray() {
        return linkDataArray;
    }

    public void setLinkDataArray(Set<Map<String, String>> linkDataArray) {
        this.linkDataArray = linkDataArray;
    }
}
