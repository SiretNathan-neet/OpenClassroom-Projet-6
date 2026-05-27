package com.openclassrooms.Repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.openclassrooms.Models.UserEntity;

/**
 * Repository pour la gestion des utilisateurs.
 */

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Integer> {
    Optional<UserEntity> findByEmail(String email);
    Optional<UserEntity> findByUsername(String username);

    boolean existsByEmail(String email);
    boolean existsByUsername(String username);

    /**
     * Permet l'authentification via email OU username.
     * Les deux paramètres reçoivent le même identifiant saisi par l'utilisateur —
     * Spring Data retourne le résultat dès que l'un des deux correspond.
     */
    Optional<UserEntity> findByEmailOrUsername(String email, String username);
}
