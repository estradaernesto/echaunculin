package com.sidrerias.modelo;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;


import java.util.*;

@Entity
@Table(name = "usuarios")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Usuario implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "apellidos")
    private String apellidos;

    @Column(name = "username")
    private String username;


    @Column(name = "email", nullable = false,unique=true)
    private String email;

    @Column(name = "password",nullable = false)
    private String password;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @Column(name = "fecha_nacimiento")
    private Date fechaNacimiento;

    @Column
    private String role;
    @Column(name = "ruta_avatar")
    private String rutaAvatar;

    @JsonIgnore
    @ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinTable(
            name = "usuarios_sidrerias_visitan",
            joinColumns = @JoinColumn(name = "usuario_id"),
            inverseJoinColumns = @JoinColumn(name = "sidreria_id")
    )
    private Set<Sidreria>sidreriasVisitadas=new HashSet<Sidreria>();

    @JsonIgnore
    @EqualsAndHashCode.Exclude
    @ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinTable(
            name = "usuarios_sidrerias_favoritas",
            joinColumns = @JoinColumn(name = "usuario_id"),
            inverseJoinColumns = @JoinColumn(name = "sidreria_id")
    )
    private Set<Sidreria>sidreriasFavoritas=new HashSet<Sidreria>();
    @JsonIgnore
    @OneToMany( cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "usuario_id") // this is the column that will be added to 'direccion' table
    @EqualsAndHashCode.Exclude // Excluye la colección de direcciones en hashCode y equals evitando un buble infinito en la relaccion bidireccional
    @ToString.Exclude
    private Set<Review>reviews=new HashSet<Review>();

    @JsonIgnore
    @OneToOne(mappedBy = "gerente", cascade = CascadeType.ALL)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Sidreria sidreria;

    public Usuario(Long id){
        setId(id);
    }
    @Override
    public String toString() {
        return "Usuario{" +
                "nombre='" + nombre + '\'' +
                ", apellidos='" + apellidos + '\'' +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", fechaNacimiento=" + fechaNacimiento +
                '}';
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role));
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
