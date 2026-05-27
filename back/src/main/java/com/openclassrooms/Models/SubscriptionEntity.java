package com.openclassrooms.Models;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.Data;

/**
 * Entité JPA représentant l'abonnement d'un utilisateur à un thème.
 * Utilise une clé primaire composite (userId + topicId) via @EmbeddedId
 * pour garantir qu'un utilisateur ne peut pas s'abonner deux fois au même thème.
 */

@Entity
@Table(name = "subscriptions")
@Data
public class SubscriptionEntity {

    /**
     *Clé primaire composite embarquée — combine userId et topicId.
     */
    @EmbeddedId
    private SubscriptionId id;

    /**
     * @MapsId lie le champ "topicId" de SubscriptionId à cette relation JPA.
     */
    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @ManyToOne
    @MapsId("topicId")
    @JoinColumn(name = "topic_id")
    private TopicEntity topic;
}
