package ap1.josemaria.valenzuela.rest;

import ap1.josemaria.valenzuela.dto.HuggingFaceUpdateRequest;
import ap1.josemaria.valenzuela.model.HuggingFaceQuery;
import ap1.josemaria.valenzuela.service.HuggingFaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/huggingface")
@RequiredArgsConstructor
public class HuggingFaceController {

    private final HuggingFaceService huggingFaceService;

    @PostMapping("/classify")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<HuggingFaceQuery> classify(@RequestBody Map<String, String> body) {
        String text   = body.getOrDefault("text", "");
        String labels = body.getOrDefault("labels", "positive,negative,neutral");
        return huggingFaceService.classify(text, labels);
    }

    @GetMapping("/history")
    public Flux<HuggingFaceQuery> history() {
        return huggingFaceService.findAll();
    }

    @PutMapping("/{id}")
    public Mono<HuggingFaceQuery> update(
            @PathVariable Long id, 
            @RequestBody HuggingFaceUpdateRequest request) { 
        
        return huggingFaceService.update(
                id, 
                request.inputText(), 
                request.candidateLabels()
        ); 
    }

    @PatchMapping("/delete/{id}")
    public Mono<HuggingFaceQuery> delete(@PathVariable Long id) { 
        return huggingFaceService.deleteLogical(id, false); 
    }
}
