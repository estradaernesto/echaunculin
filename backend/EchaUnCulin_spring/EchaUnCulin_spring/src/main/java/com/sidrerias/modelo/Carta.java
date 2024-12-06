package com.sidrerias.modelo;



import jakarta.persistence.*;
import lombok.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "cartas")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Carta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "recomendacion")
    private String recomendacion;

    @Column(name = "precio_medio")
    private Float precioMedio;

    @OneToMany( cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "carta_id") // this is the column that will be added to 'direccion' table
    @EqualsAndHashCode.Exclude // Excluye la colección de direcciones en hashCode y equals evitando un buble infinito en la relaccion bidireccional
    @ToString.Exclude
    private Set<Plato>platos=new HashSet<Plato>();


}
