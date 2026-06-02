package ap1.josemaria.valenzuela.service;

import ap1.josemaria.valenzuela.model.HuggingFaceQuery;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface HuggingFaceService {


    Mono<HuggingFaceQuery> classify(String inputText, String candidateLabels);

    Flux<HuggingFaceQuery> findAll();
    Mono<HuggingFaceQuery> update(Long id, String newInputText, String newCandidateLabels);
    Mono<HuggingFaceQuery> deleteLogical(Long id, boolean status);
}
