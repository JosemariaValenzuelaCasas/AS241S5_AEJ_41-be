package ap1.josemaria.valenzuela.service;

import ap1.josemaria.valenzuela.model.GeminiQuery;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface GeminiService {

    Mono<GeminiQuery> ask(String prompt);

    Flux<GeminiQuery> findAll();
}
