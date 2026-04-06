package ap1.josemaria.valenzuela.service.impl;

import ap1.josemaria.valenzuela.model.HuggingFaceQuery;
import ap1.josemaria.valenzuela.repository.HuggingFaceQueryRepository;
import ap1.josemaria.valenzuela.service.HuggingFaceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ap1.josemaria.valenzuela.dto.HuggingFaceRequest;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class HuggingFaceServiceImpl implements HuggingFaceService {

    private final WebClient huggingFaceWebClient;
    private final HuggingFaceQueryRepository repository;

    @Value("${ai.huggingface.model}")
    private String model;

    public HuggingFaceServiceImpl(@Qualifier("huggingFaceWebClient") WebClient huggingFaceWebClient,
                                  HuggingFaceQueryRepository repository) {
        this.huggingFaceWebClient = huggingFaceWebClient;
        this.repository           = repository;
    }

    @Override
    public Mono<HuggingFaceQuery> classify(String inputText, String candidateLabels) {
        log.info("[HuggingFace] Clasificando: {}", inputText);

        List<String> labels = Arrays.stream(candidateLabels.split(","))
        .map(String::trim)
        .toList();

        HuggingFaceRequest request = new HuggingFaceRequest(
            inputText,
            new HuggingFaceRequest.Parameters(labels)
        );

        return huggingFaceWebClient.post()
                .uri(uriBuilder -> uriBuilder
                    .pathSegment(model.split("/"))
                    .build()
                )
                .bodyValue( request)
                .retrieve()
                .onStatus(status -> status.isError(), response ->
                    response.bodyToMono(String.class)
                        .flatMap(errorBody -> {
                            log.error("[HuggingFace] Error body: {}", errorBody);
                            return Mono.error(new RuntimeException(errorBody));
                        })
                )
                .bodyToMono(List.class)
                .flatMap(list -> {
                    Map<String, Object> body = (Map<String, Object>) list.get(0); 
                    String topLabel = extractTopLabel(body);                   
                    Double topScore = extractTopScore(body);

                    HuggingFaceQuery query = HuggingFaceQuery.builder()
                            .inputText(inputText)
                            .candidateLabels(candidateLabels)
                            .topLabel(topLabel)
                            .topScore(topScore)
                            .fullResponse(body.toString())
                            .modelUsed(model)
                            .createdAt(LocalDateTime.now())
                            .build();

                    return repository.save(query);
                })
                .doOnSuccess(q -> log.info("[HuggingFace] Guardado con id={}", q.getId()))
                .doOnError(e -> log.error("[HuggingFace] Error: {}", e.getMessage()));
    }

    @Override
    public Flux<HuggingFaceQuery> findAll() {
        return repository.findAllByOrderByCreatedAtDesc();
    }


    @SuppressWarnings("unchecked")
    private String extractTopLabel(Map<?, ?> body) {
        try {
            List<?> labels = (List<?>) body.get("labels");
            return (String) labels.get(0);
        } catch (Exception e) {
            return "unknown";
        }
    }

    @SuppressWarnings("unchecked")
    private Double extractTopScore(Map<?, ?> body) {
        try {
            List<?> scores = (List<?>) body.get("scores");
            return ((Number) scores.get(0)).doubleValue();
        } catch (Exception e) {
            return 0.0;
        }
    }
}
