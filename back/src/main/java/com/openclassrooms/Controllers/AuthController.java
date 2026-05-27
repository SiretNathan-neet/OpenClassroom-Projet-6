package com.openclassrooms.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.openclassrooms.DTO.Request.LoginRequestDTO;
import com.openclassrooms.DTO.Request.RegisterRequestDTO;
import com.openclassrooms.DTO.Response.AuthResponseDTO;
import com.openclassrooms.Models.UserEntity;
import com.openclassrooms.Services.TokenService;
import com.openclassrooms.Services.UserService;

/**
 * Contrôleur gérant l'authentification des utilisateurs.
 * Routes publiques — accessibles sans token JWT.
 */

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenService tokenService;

    /**
     * Inscrit un nouvel utilisateur et retourne un token JWT.
     * L'authentification est déclenchée immédiatement après l'inscription
     * pour éviter de redemander à l'utilisateur de se connecter manuellement.
     */
    @PostMapping("/register")
    public ResponseEntity<AuthResponseDTO> register(@RequestBody RegisterRequestDTO request) {

        userService.register(request);

        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                request.getEmail(),
                request.getPassword()
            )
        );

        String token = tokenService.generateToken(authentication);
        return ResponseEntity.ok(new AuthResponseDTO(token));
    }

    /**
     * Authentifie un utilisateur via email ou username et retourne un token JWT.
     * L'identifier est résolu par CustomUserDetailsService qui cherche
     * par email OU par username via findByEmailOrUsername.
     */
    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@RequestBody LoginRequestDTO request) {

        UserEntity user = userService.login(request);
        if (user == null) {
            return ResponseEntity.badRequest().build();
        }

        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                request.getIdentifier(),
                request.getPassword()
            )
        );

        String token = tokenService.generateToken(authentication);
        return ResponseEntity.ok(new AuthResponseDTO(token));
    }
}
