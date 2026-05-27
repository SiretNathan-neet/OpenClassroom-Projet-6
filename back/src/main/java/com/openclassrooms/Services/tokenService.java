package com.openclassrooms.Services;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

/** 
 * Service responsable de la génération des tokens JWT.
*/
@Service
public class TokenService {

    private final JwtEncoder jwtEncoder;

    public TokenService(JwtEncoder jwtEncoder){
        this.jwtEncoder = jwtEncoder;
    }

    /**
     * Génère un token JWT signé avec l'algorithme HS256.
     * Le subject du token contient l'email de l'utilisateur authentifié.
     * Le token expire après 1 jour.
     *
     * @param authentication l'objet d'authentification Spring Security
     * @return le token JWT sous forme de String
     */
    public String generateToken(org.springframework.security.core.Authentication authentication) {

        Instant now = Instant.now();
        JwtClaimsSet claims = JwtClaimsSet.builder()
                                          .issuer("self")
                                          .issuedAt(now)
                                          .expiresAt(now.plus(1, ChronoUnit.DAYS))
                                          .subject(authentication.getName())
                                          .build();
        JwtEncoderParameters jwtEncoderParameters = JwtEncoderParameters.from(JwsHeader.with(MacAlgorithm.HS256).build(), claims);
        
        return this.jwtEncoder.encode(jwtEncoderParameters).getTokenValue();
    }
}
