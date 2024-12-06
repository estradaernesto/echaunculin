package com.sidrerias.service.impl;

import com.sidrerias.dto.ReviewDTO;
import com.sidrerias.modelo.Review;
import com.sidrerias.modelo.Sidreria;
import com.sidrerias.modelo.Usuario;
import com.sidrerias.repository.ReviewRepository;
import com.sidrerias.repository.SidreriaRepository;
import com.sidrerias.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Servicio que proporciona la lógica de negocio para las reseñas.
 * Utiliza {@link ReviewRepository} para interactuar con la base de datos.
 */
@Service
public class ReviewService {
    @Autowired
    private ReviewRepository reviewRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private SidreriaRepository sidreriaRepository;

    /**
     * Guarda o actualiza una reseña en la base de datos.
     *
     * @param reviewDTO La reseña a guardar o actualizar.
     * @return La reseña guardada o actualizada.
     */
    public Review saveReview(ReviewDTO reviewDTO){
        Usuario usuario=usuarioRepository.findById(reviewDTO.getIdUsuario()).get();
        Sidreria sidreria=sidreriaRepository.findById(reviewDTO.getIdSidreria()).get();
       Review review= reviewDTO.toEntity();
       review.setUsuario(usuario);
       review.setSidreria(sidreria);
        return reviewRepository.save(review);
    }

    public Review updateReview(ReviewDTO reviewDTO, Long idReview){
        Review existingReview =reviewRepository.findById(idReview)
         .orElseThrow(() -> new NoSuchElementException("No se encontró una review  con ID: " + idReview));
        actualizarCamposReview(reviewDTO, existingReview);
        return reviewRepository.save(existingReview);
    }


    /**
     * Recupera una reseña por su identificador único.
     *
     * @param id El identificador de la reseña.
     * @return Un {@link Optional} que contiene la reseña si es encontrada, o vacío si no existe.
     */
    public Optional<Review> getReviewById(Long id){
        return reviewRepository.findById(id);
    }

    /**
     * Obtiene todas las reseñas disponibles.
     *
     * @return Una lista de todas las reseñas.
     */
    public List<Review> getAllReviews(){
        return reviewRepository.findAll();
    }

    /**
     * Actualiza la información de una reseña existente.
     *
     * @param review La reseña con la información actualizada.
     * @return La reseña actualizada.
     */
    public Review updateReview(Review review){
        return reviewRepository.save(review);
    }

    /**
     * Elimina una reseña de la base de datos por su identificador.
     *
     * @param id El identificador de la reseña a eliminar.
     */
    public void deleteReviewById(Long id){
        reviewRepository.deleteById(id);
    }

    /**
     * Agrupa las reseñas por sidrería y las ordena por valoración de forma descendente.
     *
     * @return Un mapa donde las claves son las sidrerías y los valores son listas de reseñas asociadas.
     */
    public Map<Sidreria, List<Review>> getResenasAgrupadasPorSidreria() {
        return reviewRepository.findAll().stream()
                .sorted(Comparator.comparing(Review::getValoracion).reversed())
                .collect(Collectors.groupingBy(Review::getSidreria));
    }

    /**
     * Obtiene las reseñas realizadas por un usuario específico.
     *
     * @param usuarioId El identificador del usuario.
     * @return Una lista de reseñas asociadas al usuario.
     */
    public List<ReviewDTO> getReviewsPorUsuario(Long usuarioId) {
        return reviewRepository.findByUsuarioId(usuarioId).stream().map(this::toReviewDTO).toList();
    }




    private ReviewDTO toReviewDTO(Review review){
        return ReviewDTO.builder()
                .id(review.getId())
                .valoracion(review.getValoracion())
                .contenido(review.getContenido())
                .fechaPublicacion(review.getFechaPublicacion())
                .isEditado(review.isEditado())
                .titulo(review.getTitulo())
                .respuesta(review.getRespuesta())
                .idUsuario(review.getUsuario().getId())
                .nombreUsuario(review.getUsuario().getNombre())
                .apellidosUsuario(review.getUsuario().getApellidos())
                .role(review.getUsuario().getRole())
                .idSidreria(review.getSidreria().getId())
                .nombreSidreria(review.getSidreria().getNombre())
                .build();
    }


    private Review actualizarCamposReview(ReviewDTO reviewDTO, Review existingReview) {

        if (reviewDTO.getValoracion() > 0) {
            existingReview.setValoracion(reviewDTO.getValoracion());
        }
        if (reviewDTO.getTitulo() != null) {
            existingReview.setTitulo(reviewDTO.getTitulo());
        }
        if (reviewDTO.getContenido() != null) {
            existingReview.setContenido(reviewDTO.getContenido());
        }
        existingReview.setEditado(reviewDTO.isEditado());
        if (reviewDTO.getRespuesta() != null) {
            existingReview.setRespuesta(reviewDTO.getRespuesta());
        }

        return existingReview;
    }

}
