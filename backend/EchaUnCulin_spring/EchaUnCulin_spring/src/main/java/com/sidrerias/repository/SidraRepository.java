package com.sidrerias.repository;

import com.sidrerias.modelo.Sidra;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio JPA para la entidad Sidra.
 * Proporciona una forma automatizada de interactuar con la base de datos para la entidad Sidra.
 */
@Repository
public interface SidraRepository extends JpaRepository<Sidra, Long> {

    /**
     * Guarda una sidra en la base de datos.
     *
     * @param sidra La sidra a guardar.
     * @return La sidra guardada.
     */
    Sidra save(Sidra sidra);

    /**
     * Busca una sidra por su identificador único.
     *
     * @param id El identificador de la sidra.
     * @return Un Optional que contiene la sidra si se encuentra, o vacío si no se encuentra.
     */
    Optional<Sidra> findById(Long id);

}

