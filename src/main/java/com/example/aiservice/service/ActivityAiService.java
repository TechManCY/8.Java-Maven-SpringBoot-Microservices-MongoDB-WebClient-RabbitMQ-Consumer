package com.example.aiservice.service;

import com.example.aiservice.model.Activity;
import com.example.aiservice.model.Recommendation;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
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

    public Recommendation generatedRecommendation(Activity activity){
        String prompt = createPromptForActivity(activity);
        String aiResponse = aiService.getAnswer(prompt);
        return processAiResponse(activity, aiResponse);
    }

    //Extract relevant content from the ai Response
    private Recommendation processAiResponse (Activity activity , String aiResponse){
        Recommendation recommendation = null; // Declare the local variable here
        try{
            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(aiResponse);
            //following is specific to AI return format.
            JsonNode textNode = rootNode.path("candidates")
                    .get(0)
                    .path("content")
                    .path("text");
            String jsonContent = textNode.asText()
                    .replaceAll("```json\\n","")
                    .replaceAll("\\n```","")
                    .trim();
            log.info("PASSED RESPONSE FROM AI: {} ", jsonContent);
            // Deserialize jsonContent into Recommendation object
            recommendation = mapper.readValue(jsonContent, Recommendation.class);
            return recommendation;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return recommendation;
    }

    private String createPromptForActivity(Activity activity) {
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
