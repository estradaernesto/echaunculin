package com.sidrerias.modelo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="imagen_sidreria")
@Data
@NoArgsConstructor
public class ImagenSidreria {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ruta",nullable = false)
    private String ruta;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "sidreria_id",nullable = false)
    private Sidreria sidreria;

    public ImagenSidreria(Sidreria sidreria,String ruta){
        this.sidreria=sidreria;
        this.ruta=ruta;
    }
}
