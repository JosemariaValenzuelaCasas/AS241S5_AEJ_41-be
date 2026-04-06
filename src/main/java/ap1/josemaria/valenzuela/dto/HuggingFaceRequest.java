package ap1.josemaria.valenzuela.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HuggingFaceRequest {

    private String inputs;
    private Parameters parameters;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Parameters {
        private List<String> candidate_labels;
    }
}