package com.sidrerias.repository;

import com.sidrerias.modelo.Menu;
import com.sidrerias.modelo.Plato;
import com.sidrerias.modelo.Sidreria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * Repositorio JPA para la entidad Menu.
 * Proporciona una forma automatizada de interactuar con la base de datos para la entidad Menu.
 */
@Repository
public interface MenuRepository extends JpaRepository<Menu, Long> {

    /**
     * Guarda un menú en la base de datos.
     *
     * @param menu El menú a guardar.
     * @return El menú guardado.
     */
    Menu save(Menu menu);

    /**
     * Busca un menú por su identificador único.
     *
     * @param id El identificador del menú.
     * @return Un Optional que contiene el menú si se encuentra, o vacío si no se encuentra.
     */
    Optional<Menu> findById(Long id);

    /**
     * Recupera todos los menús de la base de datos.
     *
     * @return Una lista de todos los menús.
     */
    List<Menu> findAll();

    /**
     * Elimina un menú de la base de datos.
     *
     * @param menu El menú a eliminar.
     */
    void delete(Menu menu);

    /**
     * Elimina un menú por su identificador único.
     *
     * @param id El identificador del menú a eliminar.
     */
    @Override
    void deleteById(Long id);

    /**
     * Encuentra los platos asociados a un menú específico.
     *
     * @param menu El menú del cual obtener los platos.
     * @return Una lista de platos asociados al menú.
     */
    @Query("SELECT p FROM Plato p WHERE p.menu = :menu")
    List<Plato> findPlatosByMenu(@Param("menu") Menu menu);

    /**
     * Busca un menú por la sidrería y la fecha de publicación.
     *
     * @param sidreria La sidrería asociada al menú.
     * @param fecha La fecha de publicación del menú.
     * @return Un Optional que contiene el menú si se encuentra.
     */
    Optional<Menu> findBySidreriaAndFechaPublicacion(Sidreria sidreria, Date fecha);
}
