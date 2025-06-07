package com.example.aiservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Service
public class AiService {
    private final WebClient webClient;

    public AiService(WebClient.Builder webClientBuilder){
        this.webClient = webClientBuilder.build();
    }

    public String getAnswer (String question) {
        //Construct a Map format as required of the body of the post request to Ai API
        Map<String, Object> requestBody = Map.of(
                "contents", new Object[]{
                        Map.of("parts" , new Object[]{
                                Map.of("text", question)
                        })
                });
        //String response = webClient.post().uri().header().bodyValue(requestBody).retrieve().bodyToMono(String.class).block();
        String response = "This is simulated answer.";

        return response;
    }
}
