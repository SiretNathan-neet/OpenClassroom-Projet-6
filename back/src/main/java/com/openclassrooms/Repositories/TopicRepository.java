package com.openclassrooms.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.openclassrooms.Models.TopicEntity;

/**
 * Repository pour la gestion des entités TopicEntity.
 */

public interface TopicRepository extends JpaRepository<TopicEntity, Integer> {

}
