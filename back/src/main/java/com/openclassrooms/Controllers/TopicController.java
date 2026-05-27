package com.openclassrooms.Controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.openclassrooms.DTO.Response.TopicResponseDTO;
import com.openclassrooms.Services.TopicService;

/**
 * Contrôleur gérant les thèmes et les abonnements.
 * Routes protégées — nécessitent un token JWT valide.
 */

@RestController
@RequestMapping("/api/topics")
public class TopicController {

    @Autowired
    private TopicService topicService;

    @GetMapping
    public ResponseEntity<List<TopicResponseDTO>> getAllTopics() {
        return ResponseEntity.ok(topicService.getAllTopics());
    }

    @PostMapping("/{topicId}/subscribe")
    public ResponseEntity<Void> subscribeToTopic(@PathVariable Integer topicId) {
        topicService.subscribe(topicId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{topicId}/unsubscribe")
    public ResponseEntity<Void> unsubscribeFromTopic(@PathVariable Integer topicId) {
        topicService.unsubscribeFromTopic(topicId);
        return ResponseEntity.ok().build();
    }

    /**
    * Contrôleur gérant les thèmes et les abonnements.
    * Routes protégées — nécessitent un token JWT valide.
    */
    @GetMapping("/me")
    public ResponseEntity<List<TopicResponseDTO>> getTopicsForCurrentUser() {
        return ResponseEntity.ok(topicService.getUserSubscriptions());
    }
}

