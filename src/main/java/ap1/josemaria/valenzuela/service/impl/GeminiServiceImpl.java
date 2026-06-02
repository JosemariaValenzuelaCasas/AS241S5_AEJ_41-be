package ap1.josemaria.valenzuela.service.impl;

import ap1.josemaria.valenzuela.model.GeminiQuery;
import ap1.josemaria.valenzuela.repository.GeminiQueryRepository;
import ap1.josemaria.valenzuela.service.GeminiService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class GeminiServiceImpl implements GeminiService {

    private final WebClient geminiWebClient;
    private final GeminiQueryRepository repository;

    @Value("${ai.gemini.api-key}")
    private String apiKey;

    @Value("${ai.gemini.model}")
    private String model;

    public GeminiServiceImpl(@Qualifier("geminiWebClient") WebClient geminiWebClient,
                             GeminiQueryRepository repository) {
        this.geminiWebClient = geminiWebClient;
        this.repository      = repository;
    }

    @Override
    public Mono<GeminiQuery> ask(String prompt) {
        log.info("[Gemini] Enviando prompt: {}", prompt);

        Map<String, Object> requestBody = Map.of(
            "contents", List.of(
                Map.of("parts", List.of(Map.of("text", prompt)))
            )
        );

        return geminiWebClient.post()
                .uri(uriBuilder -> uriBuilder
                    .path("/models/{model}:generateContent")
                    .queryParam("key", apiKey)
                    .build(model)
                )
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(Map.class)
                .map(this::extractText)
                .flatMap(responseText -> {
                    GeminiQuery query = GeminiQuery.builder()
                            .prompt(prompt)
                            .response(responseText)
                            .modelUsed(model)
                            .tokensUsed(responseText.length() / 4)
                            .createdAt(LocalDateTime.now())
                            .build();
                    return repository.save(query);
                })
                .doOnSuccess(q -> log.info("[Gemini] Guardado con id={}", q.getId()))
                .doOnError(e -> log.error("[Gemini] Error: {}", e.getMessage()));
    }

    @Override
    public Flux<GeminiQuery> findAll() {
        return repository.findAllByOrderByCreatedAtDesc();
    }


    @SuppressWarnings("unchecked")
    private String extractText(Map<?, ?> body) {
        try {
            List<?> candidates = (List<?>) body.get("candidates");
            Map<?, ?> first    = (Map<?, ?>) candidates.get(0);
            Map<?, ?> content  = (Map<?, ?>) first.get("content");
            List<?> parts      = (List<?>) content.get("parts");
            Map<?, ?> part     = (Map<?, ?>) parts.get(0);
            return (String) part.get("text");
        } catch (Exception e) {
            log.warn("[Gemini] No se pudo parsear la respuesta: {}", body);
            return "Sin respuesta";
        }
    }

    @Override
    public Mono<GeminiQuery> update(Long id, String newPrompt) {
        return repository.findById(id)
            .flatMap(existingQuery -> {
                log.info("[Gemini] Actualizando consulta id: {} con nuevo prompt: {}", id, newPrompt);
                
                Map<String, Object> requestBody = Map.of(
                    "contents", List.of(Map.of("parts", List.of(Map.of("text", newPrompt))))
                );

                return geminiWebClient.post()
                    .uri(uriBuilder -> uriBuilder
                        .path("/models/{model}:generateContent")
                        .queryParam("key", apiKey)
                        .build(model)
                    )
                    .bodyValue(requestBody)
                    .retrieve()
                    // --- BLOQUE DE DEPURACIÓN ---
                    .onStatus(status -> status.isError(), response -> 
                        response.bodyToMono(String.class)
                            .flatMap(errorBody -> {
                                log.error("[Gemini API Error - UPDATE] Código: {}, Body: {}", response.statusCode(), errorBody);
                                return Mono.error(new RuntimeException("Error en Gemini: " + errorBody));
                            })
                    )
                    // ----------------------------
                    .bodyToMono(Map.class)
                    .map(this::extractText)
                    .flatMap(newResponse -> {
                        existingQuery.setPrompt(newPrompt);
                        existingQuery.setResponse(newResponse);
                        existingQuery.setCreatedAt(LocalDateTime.now());
                        return repository.save(existingQuery);
                    });
            });
    }

    @Override
    public Mono<GeminiQuery> deleteLogical(Long id, boolean status) {
        return repository.findById(id)
                .flatMap(query -> {
                    query.setStatus(status);
                    return repository.save(query);
                });
    }
}
