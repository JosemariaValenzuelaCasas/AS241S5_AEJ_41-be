package ap1.josemaria.valenzuela.repository;

import ap1.josemaria.valenzuela.model.GeminiQuery;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface GeminiQueryRepository extends ReactiveCrudRepository<GeminiQuery, Long> {

    Flux<GeminiQuery> findAllByOrderByCreatedAtDesc();

    Flux<GeminiQuery> findByModelUsed(String modelUsed);
}
