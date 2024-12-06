package com.sidrerias.repository;

import com.sidrerias.modelo.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio JPA para la entidad Usuario.
 * Proporciona una forma automatizada de interactuar con la base de datos para la entidad Usuario.
 */
@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    /**
     * Guarda un usuario en la base de datos.
     *
     * @param usuario El usuario a guardar.
     * @return El usuario guardado.
     */
    Usuario save(Usuario usuario);

    /**
     * Busca un usuario por su identificador único.
     *
     * @param id El identificador del usuario.
     * @return Un Optional que contiene el usuario si se encuentra, o vacío si no se encuentra.
     */
    Optional<Usuario> findById(Long id);

    /**
     * Recupera todos los usuarios de la base de datos.
     *
     * @return Una lista de todos los usuarios.
     */
    List<Usuario> findAll();

    /**
     * Elimina un usuario de la base de datos.
     *
     * @param usuario El usuario a eliminar.
     */
    void delete(Usuario usuario);

    Optional<Usuario> findByEmail(String email);

    Optional<Usuario>findByUsername(String username);


}
