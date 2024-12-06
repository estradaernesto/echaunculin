package com.sidrerias.modelo;



import com.sidrerias.dto.ReviewDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;



import java.util.Date;

@Entity
@Table(name = "reviews")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "valoracion",nullable = false)
    private float valoracion;
    @Column(name = "contenido_review",nullable = false)
    private String contenido;
    @Column(name = "fecha_publicacion")
    private Date fechaPublicacion;

    @Column(name = "is_editado")
    private boolean isEditado;
    @Column(name="titulo")
    private String titulo;

    @Column(name="respuesta")
    private String respuesta;


    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "sidreria_id", nullable = false)
    private Sidreria sidreria;




}
