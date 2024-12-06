package com.sidrerias.modelo;



import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;


import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "menus")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Menu {
    @NonNull
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre")
    private String descripcion;

    @Column(name = "incluye_sidra")
    private boolean incluyeSidra;

    @Column(name = "fecha_publicacion")
    private Date fechaPublicacion;

    @Column(name = "precio",nullable = false)
    private Float precio;

    @JsonIgnore
    @OneToMany( cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "menu_id") // this is the column that will be added to 'direccion' table
    @EqualsAndHashCode.Exclude // Excluye la colección de direcciones en hashCode y equals evitando un buble infinito en la relaccion bidireccional
    @ToString.Exclude
    private Set<Plato>platos=new HashSet<Plato>();


    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "sidreria_id", nullable = false)
    private Sidreria sidreria;



}
