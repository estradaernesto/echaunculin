package com.sidrerias.repository;

import com.sidrerias.modelo.Plato;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio JPA para la entidad Plato.
 * Proporciona una forma automatizada de interactuar con la base de datos para la entidad Plato.
 */
@Repository
public interface PlatoRepository extends JpaRepository<Plato, Long> {

    /**
     * Guarda un plato en la base de datos.
     *
     * @param plato El plato a guardar.
     * @return El plato guardado.
     */
    Plato save(Plato plato);

    /**
     * Busca un plato por su identificador único.
     *
     * @param id El identificador del plato.
     * @return Un Optional que contiene el plato si se encuentra, o vacío si no se encuentra.
     */
    Optional<Plato> findById(Long id);

    /**
     * Recupera todos los platos de la base de datos.
     *
     * @return Una lista de todos los platos.
     */
    List<Plato> findAll();

    /**
     * Elimina un plato de la base de datos.
     *
     * @param plato El plato a eliminar.
     */
    void delete(Plato plato);

    /**
     * Encuentra platos por su nombre.
     *
     * @param nombre El nombre del plato a buscar.
     * @return Una lista de platos que coinciden con el nombre proporcionado.
     */
    List<Plato> findByNombre(String nombre);
}

