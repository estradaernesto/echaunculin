package com.sidrerias.modelo;



import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;


import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "sidras")
public class Sidra {
    @NonNull
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre_sidra", nullable = false)
    private String nombre;

    @Column(name = "nombre_llagar",nullable = false)
    private String llagar;

    @Column(name = "es_denominacion_origen")
    private boolean esDenominacionOrigen;

    @Column(name = "valoracion")
    private float valoracion;
    @JsonIgnore
    @EqualsAndHashCode.Exclude
    @ManyToMany(mappedBy = "sidras")
    private Set<Sidreria>sidrerias=new HashSet<Sidreria>();
}
