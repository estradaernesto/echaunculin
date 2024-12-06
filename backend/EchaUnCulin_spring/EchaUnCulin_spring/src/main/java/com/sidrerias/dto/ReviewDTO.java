package com.sidrerias.dto;

import com.sidrerias.modelo.Review;
import com.sidrerias.modelo.Sidreria;
import com.sidrerias.modelo.Usuario;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class ReviewDTO extends BaseDTO<Review>{
    private Long id;
    private float valoracion;
    private String titulo;
    private String contenido;
    private Date fechaPublicacion;
    private boolean isEditado;
    private String respuesta;
    private Long idUsuario;
    private String nombreUsuario;
    private String apellidosUsuario;
    private String role;
    private Long idSidreria;
    private String nombreSidreria;
    private String rutaAvatar;

    @Override
    public Review toEntity() {
        Review review=new Review();
        review.setId(getId());
        review.setValoracion(getValoracion());
        review.setTitulo(getTitulo());
        review.setContenido(getContenido());
        review.setFechaPublicacion((getFechaPublicacion()));
        review.setEditado(isEditado);
        review.setRespuesta(getRespuesta());
        return review;
    }
}
