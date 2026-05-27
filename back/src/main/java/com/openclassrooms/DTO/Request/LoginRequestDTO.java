package com.openclassrooms.DTO.Request;

import lombok.Data;

@Data
public class LoginRequestDTO {
    /** Email ou nom d'utilisateur - Résolu par CustomUserDetailsService */
    private String identifier; 
    private String password;
}
