package com.openclassrooms.Controllers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.openclassrooms.DTO.Request.UpdateUserRequestDTO;
import com.openclassrooms.DTO.Response.UserResponseDTO;
import com.openclassrooms.Services.UserService;

/**
 * Contrôleur gérant les thèmes et les abonnements.
 * Routes protégées — nécessitent un token JWT valide.
 */

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/me")
    public ResponseEntity<UserResponseDTO> getMe() {
        return ResponseEntity.ok(userService.getMe());
    }

    @PutMapping("/me")
    public ResponseEntity<UserResponseDTO> updateMe(@RequestBody UpdateUserRequestDTO request) {
        return ResponseEntity.ok(userService.updateMe(request));
    }
}