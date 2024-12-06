package com.sidrerias.dto;

import com.sidrerias.modelo.ImagenSidreria;
import com.sidrerias.modelo.Sidreria;
import com.sidrerias.modelo.Usuario;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SidreriaRequest extends BaseDTO<Sidreria> {
    private String nombre;
    private String ubicacion;
    private double valoracion;
    private double precioComensal;
    private boolean esEscanciado;
    private Set<String>imagenes;
    private String rutaCarta;

    @Override
    public Sidreria toEntity() {
        Sidreria sidreria = new Sidreria();
        sidreria.setNombre(getNombre());
        sidreria.setUbicacion(getUbicacion());
        sidreria.setValoracion(getValoracion());
        sidreria.setPrecioComensal(getPrecioComensal());
        sidreria.setEscanciado(isEsEscanciado());
        sidreria.setRutaCarta(getRutaCarta());
        return sidreria;
    }
}
