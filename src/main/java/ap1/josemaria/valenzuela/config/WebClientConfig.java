package ap1.josemaria.valenzuela.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Value("${ai.gemini.base-url}")
    private String geminiBaseUrl;

    @Value("${ai.gemini.api-key}")
    private String geminiApiKey;

    @Bean("geminiWebClient")
    public WebClient geminiWebClient() {
        return WebClient.builder()
                .baseUrl(geminiBaseUrl)
                .defaultHeader("Content-Type", "application/json")
                .build();
    }

    @Value("${ai.huggingface.base-url}")
    private String hfBaseUrl;

    @Value("${ai.huggingface.api-key}")
    private String hfApiKey;

    @Bean("huggingFaceWebClient")
    public WebClient huggingFaceWebClient() {
        return WebClient.builder()
                .baseUrl(hfBaseUrl)
                .defaultHeader("Content-Type", "application/json")
                .defaultHeader("Authorization", "Bearer " + hfApiKey)
                .defaultHeader("Accept", "application/json")
                .build();
    }
}
