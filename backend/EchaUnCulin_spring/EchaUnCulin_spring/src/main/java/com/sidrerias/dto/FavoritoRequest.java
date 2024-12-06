package com.sidrerias.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FavoritoRequest {
    private Long idSidreria;
    private Long idUsuario;

    @JsonProperty("isAltaFavorito")
    private boolean isAltaFavorito;

}
