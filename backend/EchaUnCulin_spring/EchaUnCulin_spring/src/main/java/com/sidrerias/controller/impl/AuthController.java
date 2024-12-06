package com.sidrerias.controller.impl;

import com.sidrerias.controller.AuthApi;
import com.sidrerias.dto.TokenResponse;
import com.sidrerias.dto.UserRequest;
import com.sidrerias.dto.UserResponse;
import com.sidrerias.modelo.Usuario;
import com.sidrerias.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController implements AuthApi {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @Override
    public ResponseEntity<UserResponse> createUser(Usuario usuario) {

        return ResponseEntity.ok(authService.createUser(usuario));
    }

    @Override
    public ResponseEntity<String> getUser(String userId) {

        return ResponseEntity.ok(userId);
    }

    @Override
    public ResponseEntity<UserResponse> login(UserRequest userRequest) {
        return ResponseEntity.ok(authService.loadUser(userRequest));
    }


}
