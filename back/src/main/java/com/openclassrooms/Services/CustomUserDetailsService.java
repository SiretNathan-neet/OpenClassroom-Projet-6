package com.openclassrooms.Services;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.openclassrooms.Models.UserEntity;
import com.openclassrooms.Repositories.UserRepository;

/**
 * Implémentation de UserDetailsService pour Spring Security.
 * Permet l'authentification via email OU username.
 */

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Charge un utilisateur par email ou username.
     * Le paramètre "username" est ici un identifiant générique — il peut
     * contenir un email ou un username selon ce que l'utilisateur a saisi.
     * Spring Security utilise toujours l'email comme identifiant interne
     * une fois l'utilisateur trouvé.
     */
    @Override
    public UserDetails loadUserByUsername(String identifier) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository
        .findByEmailOrUsername(identifier, identifier)
        .orElseThrow(() -> new UsernameNotFoundException(
            "Utilisateur introuvable : " + identifier));

        return User.builder()
                .username(userEntity.getEmail())
                .password(userEntity.getPassword())
                .roles("USER")
                .build();
    }
}
