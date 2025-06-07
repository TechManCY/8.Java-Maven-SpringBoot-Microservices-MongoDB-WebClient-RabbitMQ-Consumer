package com.example.aiservice.service;

import com.example.aiservice.model.Activity;
import com.example.aiservice.model.Recommendation;
import com.example.aiservice.repository.RecommendationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class ActivityMessageListener {
    private final ActivityAiService activityAiService;
    private final RecommendationRepository recommendationRepository;

    @RabbitListener(queues = "${rabbitmq.queue.name}")
    public void processActivity(Activity activity){
        log.info("Received activity for processing: {}", activity.getId());
        Recommendation recommendation = activityAiService.generatedRecommendation(activity);
        if (recommendation != null){
            log.info("Generated recommendation: {}", recommendation);
            recommendationRepository.save(recommendation);
        } else {
            log.warn("Failed to generate a recommendation for activity: {}. The AI service returned null.", activity.getId());
        }

    }
}
