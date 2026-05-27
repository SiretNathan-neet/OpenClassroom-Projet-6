package com.openclassrooms.Models;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Data;

/**
 * Entité JPA représentant un article publié sur MDD.
 * Un article est associé à un auteur et un thème.
 * L'auteur et la date sont définis automatiquement à la création.
 */

@Entity
@Table(name = "posts")  
@Data
public class PostEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity author;

    @ManyToOne
    @JoinColumn(name = "topic_id", nullable = false)
    private TopicEntity topic;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /**
    * Entité JPA représentant un article publié sur MDD.
    * Un article est associé à un auteur et un thème.
    * L'auteur et la date sont définis automatiquement à la création.
    */
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }   
}
