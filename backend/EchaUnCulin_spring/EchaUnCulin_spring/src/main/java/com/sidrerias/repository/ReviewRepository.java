package com.sidrerias.repository;

import com.sidrerias.modelo.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio JPA para la entidad Review.
 * Proporciona una forma automatizada de interactuar con la base de datos para la entidad Review.
 */
@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    /**
     * Guarda una reseña en la base de datos.
     *
     * @param review La reseña a guardar.
     * @return La reseña guardada.
     */
    Review save(Review review);

    /**
     * Busca una reseña por su identificador único.
     *
     * @param id El identificador de la reseña.
     * @return Un Optional que contiene la reseña si se encuentra, o vacío si no se encuentra.
     */
    Optional<Review> findById(Long id);

    /**
     * Recupera todas las reseñas de la base de datos.
     *
     * @return Una lista de todas las reseñas.
     */
    List<Review> findAll();

    /**
     * Elimina una reseña de la base de datos.
     *
     * @param review La reseña a eliminar.
     */
    void delete(Review review);

    /**
     * Encuentra reseñas por el identificador del usuario.
     *
     * @param usuarioId El identificador del usuario cuyas reseñas se quieren encontrar.
     * @return Una lista de reseñas asociadas al usuario.
     */
    List<Review> findByUsuarioId(Long usuarioId);
}

