package com.example.aiservice.service;

import com.example.aiservice.model.Activity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class ActivityAiService {
    private final AiService aiService;

    public String generatedRecommendation(Activity activity){
        String prompt = createPromptForActivty(activity);
        String aiResponse = aiService.getAnswer(prompt);
        log.info("RESPONSE FROM AI: {} ", aiResponse);
        return aiResponse;
    }

    private String createPromptForActivty(Activity activity) {
        return String.format("""
                Provide recommendation based on: \n
                Type: %s, Duration: %d, Calories Burned: %d, Additional Metrics: %s
                """,
                activity.getType(),
                activity.getDuration(),
                activity.getCaloriesBurned(),
                activity.getAdditionalMetrics()); // This will print the Map's toString() representation
    }

}
