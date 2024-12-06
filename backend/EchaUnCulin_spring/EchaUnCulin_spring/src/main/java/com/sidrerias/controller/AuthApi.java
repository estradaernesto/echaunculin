package com.sidrerias.controller;

import com.sidrerias.dto.TokenResponse;
import com.sidrerias.dto.UserRequest;
import com.sidrerias.dto.UserResponse;
import com.sidrerias.modelo.Usuario;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("api/auth")
public interface AuthApi {
    @PostMapping(value = "/register")
    ResponseEntity<UserResponse> createUser(@RequestBody @Valid Usuario userRequest);

    @GetMapping
    ResponseEntity<String>getUser(@RequestAttribute(name = "X-User-Id")String userId);

    @PostMapping(value = "/login")
    ResponseEntity<UserResponse> login(@RequestBody @Valid UserRequest userRequest);
}