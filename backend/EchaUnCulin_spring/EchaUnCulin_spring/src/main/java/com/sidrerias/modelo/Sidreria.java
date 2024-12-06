package com.sidrerias.modelo;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sidrerias.dto.SidreriaDTO;
import jakarta.persistence.*;
import lombok.*;


import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;


@Entity
@Data

@Table(name= "sidrerias")
public class Sidreria {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre_sidreria")
    private String nombre;
    @Column(name="valoracion")
    private double valoracion;
    @Column(name="precio_comensal")
    private double precioComensal;
    @Column(name = "escanciado")
    private boolean escanciado;
    @Column
    private String ubicacion;

    @Column(name="ruta_carta")
    private String rutaCarta;

    @JsonIgnore
    @OneToMany( cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "sidreria_id") // this is the column that will be added to 'direccion' table
    @EqualsAndHashCode.Exclude // Excluye la colección de direcciones en hashCode y equals evitando un buble infinito en la relaccion bidireccional
    @ToString.Exclude
    private Set<Review> reviews=new HashSet<Review>();




    @JsonBackReference
    @ManyToMany(mappedBy = "sidreriasFavoritas")
    private Set<Usuario>usuariosGustan=new HashSet<Usuario>();

    @JsonIgnore
    @ManyToMany(mappedBy = "sidreriasVisitadas")
    private Set<Usuario>usuariosVisitan=new HashSet<Usuario>();

    @JsonIgnore
    @EqualsAndHashCode.Exclude
    @OneToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario gerente;

    @JsonIgnore
    @ManyToMany(cascade = {CascadeType.PERSIST,CascadeType.MERGE})
    @JoinTable(
            name = "sidreria_sidras",
            joinColumns = @JoinColumn(name="sidreria_id"),
            inverseJoinColumns = @JoinColumn(name = "sidra_id")
    )
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Set<Sidra>sidras=new HashSet<Sidra>();


    @JsonIgnore
    @OneToMany( cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "sidreria_id") // this is the column that will be added to 'direccion' table
    @EqualsAndHashCode.Exclude // Excluye la colección de direcciones en hashCode y equals evitando un buble infinito en la relaccion bidireccional
    @ToString.Exclude
    private Set<Menu>menus=new HashSet<Menu>();


    @JsonIgnore
    @OneToMany(mappedBy = "sidreria", cascade = CascadeType.ALL, orphanRemoval = true)
    @EqualsAndHashCode.Exclude // Excluye la colección de direcciones en hashCode y equals evitando un buble infinito en la relaccion bidireccional
    @ToString.Exclude
    private Set<ImagenSidreria>imagenes=new HashSet<ImagenSidreria>();



    public SidreriaDTO toDto(boolean isSidreriaFavorita){
        return SidreriaDTO.builder()
                .idSidreria(id)
                .nombre(getNombre())
                .ubicacion(getUbicacion())
                .valoracion(getValoracion())
                .esEscanciado(isEscanciado())
                .precioComensal(getPrecioComensal())
                .imagenes(getImagenes().stream().map(ImagenSidreria::getRuta).collect(Collectors.toSet()))
                .carta(getRutaCarta())
                .idHostelero(getGerente().getId())
                .isFavorita(isSidreriaFavorita)
                .build();
    }



}
