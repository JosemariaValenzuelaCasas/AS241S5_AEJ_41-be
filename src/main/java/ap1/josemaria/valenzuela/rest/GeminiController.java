package ap1.josemaria.valenzuela.rest;

import ap1.josemaria.valenzuela.dto.GeminiUpdateRequest;
import ap1.josemaria.valenzuela.model.GeminiQuery;
import ap1.josemaria.valenzuela.service.GeminiService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;
@CrossOrigin(origins = "*") // Permite solicitudes desde cualquier origen (útil para desarrollo)
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

    @PutMapping("/{id}")
    public Mono<GeminiQuery> update(
            @PathVariable Long id, 
            @RequestBody GeminiUpdateRequest request) { 
        
        // Al usar request.prompt(), le pasamos al Service solo el texto: "¿Qué día es mañana?"
        // Así tu Impl recibirá el String limpio y no el JSON crudo.
        return geminiService.update(id, request.prompt()); 
    }

    @PatchMapping("/delete/{id}")
    public Mono<GeminiQuery> delete(@PathVariable Long id) { 
        return geminiService.deleteLogical(id, false); 
    }
}
