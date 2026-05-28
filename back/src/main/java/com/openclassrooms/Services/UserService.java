package com.openclassrooms.Services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.openclassrooms.DTO.Request.LoginRequestDTO;
import com.openclassrooms.DTO.Request.RegisterRequestDTO;
import com.openclassrooms.DTO.Request.UpdateUserRequestDTO;
import com.openclassrooms.DTO.Response.UserResponseDTO;
import com.openclassrooms.Exceptions.AlreadyUsedException;
import com.openclassrooms.Exceptions.NotFoundException;
import com.openclassrooms.Models.UserEntity;
import com.openclassrooms.Repositories.UserRepository;

/**
 * Service gérant la logique métier des utilisateurs.
 */

@Service
public class UserService {
    
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Valide le mot de passe selon les critères suivants :
     * - Minimum 8 caractères
     * - Au moins une lettre majuscule
     * - Au moins une lettre minuscule
     * - Au moins un chiffre
     * - Au moins un caractère spécial
     */
    private boolean isValidPassword(String password) {
        String pattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[^a-zA-Z0-9]).{8,}$";
        return password.matches(pattern);
    }

    public UserEntity register(RegisterRequestDTO request){

        if (!isValidPassword(request.getPassword())) {
            throw new IllegalArgumentException("Le mot de passe ne respecte pas les critères de sécurité");
        }

        if (userRepository.existsByEmail(request.getEmail()) || 
        userRepository.existsByUsername(request.getUsername())) {
            throw new AlreadyUsedException("Email ou nom d'utilisateur déjà utilisé");
        }
        
        UserEntity user = new UserEntity();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        
        UserEntity savedUser = userRepository.save(user);

        return savedUser;
    }

    public UserEntity login(LoginRequestDTO request) {
        Optional<UserEntity> user = userRepository
        .findByEmailOrUsername(request.getIdentifier(), request.getIdentifier());

        if (user.isEmpty()) {
            return null;
        }

        return user.get();
    }

    /**
     * Récupère le profil de l'utilisateur connecté.
     * L'email est extrait du token JWT via le SecurityContext.
     */
    public UserResponseDTO getMe() {
        String email = SecurityContextHolder.getContext()
                                            .getAuthentication()
                                            .getName();  
        
        UserEntity user = userRepository.findByEmail(email)
        .orElseThrow(() -> new NotFoundException("Utilisateur introuvable"));

        UserResponseDTO response = new UserResponseDTO();
        response.setId(user.getId());
        response.setUsername(user.getUsername());
        response.setEmail(user.getEmail());

        return response;
    }

    public UserResponseDTO updateMe(UpdateUserRequestDTO request) {
        String email = SecurityContextHolder.getContext()
                                            .getAuthentication()
                                            .getName();  
        
        UserEntity user = userRepository.findByEmail(email)
        .orElseThrow(() -> new NotFoundException("Utilisateur introuvable"));

        if (request.getUsername() != null) {
            user.setUsername(request.getUsername());
        }
        if (request.getEmail() != null) {
            user.setEmail(request.getEmail());
        }
        if (request.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }

        UserEntity updatedUser = userRepository.save(user);

        UserResponseDTO response = new UserResponseDTO();
        response.setId(updatedUser.getId());
        response.setUsername(updatedUser.getUsername());
        response.setEmail(updatedUser.getEmail());

        return response;
    }
}


