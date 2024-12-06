package com.sidrerias.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponse {
    private String accessToken;
    private Long id;
    private String email;
    private String nombre;
    private String apellidos;
    private String role;
    private String rutaAvatar;
}
