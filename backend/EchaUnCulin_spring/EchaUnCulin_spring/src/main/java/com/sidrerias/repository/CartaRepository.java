package com.sidrerias.repository;

import com.sidrerias.modelo.Carta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio JPA para la entidad Carta.
 * Proporciona automáticamente métodos CRUD para operar con la base de datos de Cartas.
 */
@Repository
public interface CartaRepository extends JpaRepository<Carta, Long> {

    /**
     * Guarda una carta en la base de datos.
     *
     * @param carta La carta a ser guardada.
     * @return La carta guardada.
     */
    @Override
    Carta save(Carta carta);

    /**
     * Busca una carta por su identificador único.
     *
     * @param id El identificador único de la carta.
     * @return Un Optional conteniendo la carta si es encontrada, o un Optional vacío si no se encuentra.
     */
    @Override
    Optional<Carta> findById(Long id);

    /**
     * Recupera todas las cartas de la base de datos.
     *
     * @return Una lista con todas las cartas.
     */
    @Override
    List<Carta> findAll();

    /**
     * Elimina una carta de la base de datos.
     *
     * @param carta La carta a eliminar.
     */
    @Override
    void delete(Carta carta);
}
