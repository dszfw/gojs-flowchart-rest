package custom.dto;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ErrorDTO {

    private List<String> errors;

    public ErrorDTO() {
        errors = new ArrayList<>();
    }

    public ErrorDTO(String... msgs) {
        errors = new ArrayList<>(Arrays.asList(msgs));
    }

    public List<String> getErrors() {
        return errors;
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
    }
}
