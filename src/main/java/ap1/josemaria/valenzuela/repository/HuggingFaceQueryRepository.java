package ap1.josemaria.valenzuela.repository;

import ap1.josemaria.valenzuela.model.HuggingFaceQuery;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface HuggingFaceQueryRepository extends ReactiveCrudRepository<HuggingFaceQuery, Long> {

    Flux<HuggingFaceQuery> findAllByOrderByCreatedAtDesc();

    Flux<HuggingFaceQuery> findByTopLabel(String topLabel);
}
