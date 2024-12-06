package com.sidrerias.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Builder
@Data
public class SidreriaDTO {
    private Long idSidreria;
    private String nombre;
    private String ubicacion;
    private double valoracion;
    private boolean esEscanciado;
    private double precioComensal;
    private Set<String>imagenes;
    private String carta;
    private Long idHostelero;
    private boolean isFavorita;



}
