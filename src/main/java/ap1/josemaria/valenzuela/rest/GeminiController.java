package ap1.josemaria.valenzuela.rest;

import ap1.josemaria.valenzuela.model.GeminiQuery;
import ap1.josemaria.valenzuela.service.GeminiService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;

@RestController
@RequestMapping("/api/gemini")
@RequiredArgsConstructor
public class GeminiController {

    private final GeminiService geminiService;

    @PostMapping("/ask")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<GeminiQuery> ask(@RequestBody Map<String, String> body) {
        String prompt = body.getOrDefault("prompt", "");
        return geminiService.ask(prompt);
    }

    @GetMapping("/history")
    public Flux<GeminiQuery> history() {
        return geminiService.findAll();
    }
}
