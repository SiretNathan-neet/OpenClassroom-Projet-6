package com.openclassrooms.Models;

import java.io.Serializable;

import jakarta.persistence.Embeddable;
import lombok.Data;

/**
 * Clé primaire composite pour la table "subscriptions".
 * Doit implémenter Serializable — exigence JPA pour les clés composites.
 * La combinaison userId + topicId garantit l'unicité d'un abonnement.
 */

@Embeddable
@Data
public class SubscriptionId implements Serializable {

    private Integer userId;
    private Integer topicId;
}
