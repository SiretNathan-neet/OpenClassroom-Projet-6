package com.openclassrooms.Models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

/**
 * Clé primaire composite pour la table "subscriptions".
 * Doit implémenter Serializable — exigence JPA pour les clés composites.
 * La combinaison userId + topicId garantit l'unicité d'un abonnement.
 */

@Entity
@Table(name = "topics")
@Data
public class TopicEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true)
    private String name;
}
