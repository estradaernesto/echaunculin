package com.sidrerias.service.impl;

import com.sidrerias.dto.TokenResponse;
import com.sidrerias.dto.UserRequest;
import com.sidrerias.dto.UserResponse;
import com.sidrerias.modelo.Usuario;
import com.sidrerias.repository.UsuarioRepository;
import com.sidrerias.service.AuthService;
import com.sidrerias.service.JwtService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthServiceImpl implements AuthService {
    private final UsuarioRepository userRepository;

    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final UserDetailsServiceImpl userDetailsService;

    public AuthServiceImpl(UsuarioRepository userRepository, JwtService jwtService, PasswordEncoder passwordEncoder, UserDetailsServiceImpl userDetailsService) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
        this.userDetailsService = userDetailsService;
    }

    @Override
    public UserResponse createUser(Usuario userRequest) {
        userRequest.setRutaAvatar("imagenes/avatares/por-defecto.png");
        TokenResponse token=Optional.of(userRequest)
                .map(this::userWithEncodePassword)
                .map(userRepository::save)
                .map(userCreated -> jwtService.generateToken(userCreated.getId()))
                .orElseThrow(() -> new RuntimeException("Failed to create user"));
        return mapToUserResponse(userRequest,token.getAccessToken());
    }

    @Override
    public UserResponse loadUser(UserRequest userRequest) {
        return userRepository
                .findByEmail(userRequest.getEmail())
                .filter(user -> passwordEncoder.matches(userRequest.getPassword(), user.getPassword()))
                .map(user -> {
                    // Generar el token JWT
                   TokenResponse tokenResponse= jwtService.generateToken(user.getId());

                    // Crear y devolver la respuesta con el token y los datos del usuario
                    return new UserResponse(
                            tokenResponse.getAccessToken(),
                            user.getId(),
                            user.getEmail(),
                            user.getNombre(),
                            user.getApellidos(),
                            user.getRole(),
                            user.getRutaAvatar()
                    );
                })
                .orElseThrow(() -> new RuntimeException("Failed to load user"));
    }




    private Usuario userWithEncodePassword(Usuario user) {
        String encodePassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodePassword);
        return user;
    }

    public UserResponse mapToUserResponse(Usuario usuario,String token){
        return UserResponse.builder()
                .accessToken(token)
                .id(usuario.getId())
                .nombre(usuario.getNombre())
                .email(usuario.getEmail())
                .apellidos(usuario.getApellidos())
                .role(usuario.getRole())
                .rutaAvatar(usuario.getRutaAvatar())
                .build();
    }
}


