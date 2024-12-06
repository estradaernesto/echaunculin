package com.sidrerias.modelo;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;



@Entity
@Table(name = "platos")
@Data
public class Plato {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre_plato",nullable = false)
    private String nombre;

    @Column(name = "ruta_imagen",nullable = false)
    private String rutaImagen;

    @Column(name = "es_vegano",nullable = false)
    private Boolean esVegano;

    @Column(name = "es_sin_gluten",nullable = false)
    private  Boolean esSinGluten;

    @Column(name = "es_sin_lactosa",nullable = false)
    private Boolean esSinLactosa;

    @Column(name = "es_plato_carta")
    private Boolean esPlatoCarta;

    @Column(name = "precio")
    private Float precio;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "menu_id", nullable = true)
    private Menu menu;


    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "carta_id", nullable =true)
    private Carta carta;



}
