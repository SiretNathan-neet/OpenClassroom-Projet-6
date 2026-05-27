package com.openclassrooms.Repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.openclassrooms.Models.CommentEntity;
import com.openclassrooms.Models.PostEntity;

/**
 * Repository pour la gestion des entités CommentEntity.
 */

@Repository
public interface CommentRepository extends JpaRepository<CommentEntity, Integer> {
    /** Trouve les commentaires d'un post triés par date de création ascendante */
    List<CommentEntity> findByPostOrderByCreatedAtAsc(PostEntity post);
}
