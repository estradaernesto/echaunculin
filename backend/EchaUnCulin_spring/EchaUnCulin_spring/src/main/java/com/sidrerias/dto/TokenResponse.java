package com.sidrerias.dto;

import com.sidrerias.modelo.Usuario;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TokenResponse {
    private String accessToken;

}
