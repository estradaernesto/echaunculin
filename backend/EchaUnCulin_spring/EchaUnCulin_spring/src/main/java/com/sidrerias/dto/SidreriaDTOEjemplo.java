package com.sidrerias.dto;

import com.sidrerias.modelo.Sidreria;
import lombok.Data;

/**
 * Data Transfer Object para representar las sidrerías con un conteo de usuarios que las han visitado.
 */
@Data
public class SidreriaDTOEjemplo extends BaseDTO<Sidreria> {

    private String nombre;
    private int numeroDeUsuarios; // Representa el número de usuarios que han visitado la sidrería.

    /**
     * Constructor para crear un SidreriaDTO.
     *
     * @param nombre El nombre de la sidrería.
     * @param numeroDeUsuarios El número de usuarios que han visitado la sidrería.
     */
    public SidreriaDTOEjemplo(String nombre, int numeroDeUsuarios) {
        this.nombre = nombre;
        this.numeroDeUsuarios = numeroDeUsuarios;
    }

    /**
     * Convierte el DTO a la entidad Sidreria.
     *
     * @return La entidad Sidreria correspondiente al DTO.
     */
    @Override
    public Sidreria toEntity() {
        // Implementación de la conversión a la entidad, actualmente devuelve null.
        return null;
    }
}
