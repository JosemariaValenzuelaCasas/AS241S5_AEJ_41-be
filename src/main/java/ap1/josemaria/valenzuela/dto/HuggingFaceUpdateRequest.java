package ap1.josemaria.valenzuela.dto;

public record HuggingFaceUpdateRequest(
    String inputText,
    String candidateLabels
) {}