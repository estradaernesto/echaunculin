package com.sidrerias.controller.impl;

import com.sidrerias.dto.MessageResponse;
import com.sidrerias.dto.ReviewDTO;
import com.sidrerias.modelo.Review;
import com.sidrerias.modelo.Sidreria;
import com.sidrerias.service.impl.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Controlador REST para gestionar las operaciones CRUD relacionadas con las reseñas.
 * Proporciona endpoints para crear, recuperar, actualizar y eliminar reseñas,
 * así como para obtener reseñas agrupadas por sidrería y reseñas por usuario.
 */
@RestController
@RequestMapping("/api/reviews")
public class ReviewController {
    @Autowired
    private ReviewService reviewService;

    /**
     * Crea una nueva reseña.
     *
     * @param reviewDTO La reseña a ser creada.
     * @return La reseña creada con estado HTTP 200.
     */
    @PostMapping
    public ResponseEntity<Review> createReview(@RequestBody ReviewDTO reviewDTO){
        return  ResponseEntity.ok( reviewService.saveReview( reviewDTO));
    }

    /**
     * Obtiene todas las reseñas almacenadas.
     *
     * @return Lista de todas las reseñas con estado HTTP 200.
     */
    @GetMapping
    public ResponseEntity<List<Review>> getAllReviews() {
        List<Review> reviews = reviewService.getAllReviews();
        return ResponseEntity.ok(reviews);
    }

    /**
     * Obtiene una reseña específica por su identificador.
     *
     * @param id El identificador de la reseña.
     * @return La reseña solicitada envuelta en un objeto Optional con estado HTTP 200.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Optional<Review>> getReviewById(@PathVariable Long id) {
        Optional<Review> review = reviewService.getReviewById(id);
        return ResponseEntity.ok(review);
    }

    /**
     * Actualiza una reseña existente identificada por su ID.
     *
     * @param updatedReview La reseña con los datos actualizados.
     * @return La reseña actualizada con estado HTTP 200.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Review> updateReview(@RequestBody ReviewDTO updatedReview,@PathVariable Long id) {
        Review review = reviewService.updateReview(updatedReview,id);
        return ResponseEntity.ok(review);
    }

    /**
     * Elimina una reseña basándose en su identificador.
     *
     * @param id El identificador de la reseña a eliminar.
     * @return Un cuerpo vacío con estado HTTP 200.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReview(@PathVariable Long id) {
        reviewService.deleteReviewById(id);
        return ResponseEntity.ok().build();
    }

    /**
     * Obtiene las reseñas agrupadas por sidrería.
     *
     * @return Un mapa de sidrerías a listas de reseñas con estado HTTP 200.
     */
    @GetMapping("/agrupadas-por-sidreria")
    public ResponseEntity<Map<Sidreria, List<Review>>> getResenasAgrupadasPorSidreria() {
        Map<Sidreria, List<Review>> reviewsAgrupadas = reviewService.getResenasAgrupadasPorSidreria();
        return reviewsAgrupadas.isEmpty() ? new ResponseEntity<>(HttpStatus.NO_CONTENT)
                : new ResponseEntity<>(reviewsAgrupadas, HttpStatus.OK);
    }

    /**
     * Obtiene las reseñas realizadas por un usuario específico.
     *
     * @param usuarioId El identificador del usuario.
     * @return Lista de reseñas del usuario especificado con estado HTTP 200.
     */
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<ReviewDTO>> getReviewsPorUsuario(@PathVariable Long usuarioId) {
        List<ReviewDTO> reviews = reviewService.getReviewsPorUsuario(usuarioId);
        return reviews.isEmpty() ? new ResponseEntity<>(HttpStatus.NO_CONTENT)
                : new ResponseEntity<>(reviews, HttpStatus.OK);
    }
}
