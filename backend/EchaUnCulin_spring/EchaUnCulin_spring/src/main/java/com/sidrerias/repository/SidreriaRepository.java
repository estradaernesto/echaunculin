package com.sidrerias.repository;

import com.sidrerias.modelo.Menu;
import com.sidrerias.modelo.Sidreria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * Repositorio JPA para la entidad Sidreria.
 * Proporciona una forma automatizada de interactuar con la base de datos para la entidad Sidreria.
 */
@Repository
public interface SidreriaRepository extends JpaRepository<Sidreria, Long> {

    /**
     * Guarda una sidrería en la base de datos.
     *
     * @param sidreria La sidrería a guardar.
     * @return La sidrería guardada.
     */
  Sidreria save(Sidreria sidreria);

  Optional<Sidreria> findByGerenteId(Long idGerente);




  /**
     * Busca una sidrería por su identificador único.
     *
     * @param id El identificador de la sidrería.
     * @return Un Optional que contiene la sidrería si se encuentra, o vacío si no se encuentra.
     */
    Optional<Sidreria> findById(Long id);

    /**
     * Recupera todas las sidrerías de la base de datos.
     *
     * @return Una lista de todas las sidrerías.
     */
    List<Sidreria> findAll();

    /**
     * Elimina una sidrería de la base de datos.
     *
     * @param sidreria La sidrería a eliminar.
     */
    void delete(Sidreria sidreria);

    /**
     * Encuentra un menú específico por el ID de la sidrería y la fecha de publicación.
     *
     * @param sidreriaId El identificador de la sidrería.
     * @param fecha La fecha de publicación del menú.
     * @return Un Optional que contiene el menú correspondiente a la fecha y sidrería dadas.
     */
    @Query("SELECT m FROM Sidreria s JOIN s.menus m WHERE s.id = :sidreriaId AND :fecha = m.fechaPublicacion")
    Optional<Menu> findMenuBySidreriaIdAndFecha(@Param("sidreriaId") Long sidreriaId, @Param("fecha") Date fecha);

    @Query("SELECT CASE WHEN COUNT(s) > 0 THEN true ELSE false END " +
            "FROM Sidreria s JOIN s.usuariosGustan u " +
            "WHERE s.id = :sidreriaId AND u.id = :usuarioId")
    boolean IsSidreriaFavorita(@Param("usuarioId") Long usuarioId,
                                           @Param("sidreriaId") Long sidreriaId);


    @Modifying
    @Query(value = "INSERT INTO usuarios_sidrerias_favoritas (usuario_id, sidreria_id) VALUES (:usuarioId, :sidreriaId)", nativeQuery = true)
    int agregarSidreriaAFavoritos(@Param("usuarioId") Long usuarioId, @Param("sidreriaId") Long sidreriaId);


    @Modifying
    @Query(value = "DELETE FROM usuarios_sidrerias_favoritas WHERE usuario_id = :usuarioId AND sidreria_id = :sidreriaId", nativeQuery = true)
    int eliminarSidreriaDeFavoritos(@Param("usuarioId") Long usuarioId, @Param("sidreriaId") Long sidreriaId);







}

