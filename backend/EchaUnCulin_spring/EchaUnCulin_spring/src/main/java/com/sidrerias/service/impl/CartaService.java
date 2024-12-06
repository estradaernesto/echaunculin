package com.sidrerias.service.impl;

import com.sidrerias.modelo.Carta;
import com.sidrerias.repository.CartaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Servicio que proporciona la lógica de negocio para las cartas.
 * Utiliza {@link CartaRepository} para interactuar con la base de datos.
 */
@Service
public class CartaService {

    @Autowired
    private CartaRepository cartaRepository;

    /**
     * Guarda o actualiza una carta en la base de datos.
     *
     * @param carta La carta a guardar o actualizar.
     * @return La carta guardada o actualizada.
     */
    public Carta saveCarta(Carta carta){
        return cartaRepository.save(carta);
    }

    /**
     * Recupera una carta por su identificador único.
     *
     * @param id El identificador de la carta.
     * @return Un {@link Optional} que contiene la carta si es encontrada, o vacío si no existe.
     */
    public Optional<Carta> getCartaById(Long id){
        return cartaRepository.findById(id);
    }

    /**
     * Obtiene todas las cartas disponibles.
     *
     * @return Una lista de todas las cartas.
     */
    public List<Carta> getAllCartas(){
        return cartaRepository.findAll();
    }

    /**
     * Actualiza la información de una carta existente.
     *
     * @param carta La carta con la información actualizada.
     * @return La carta actualizada.
     */
    public Carta updateCarta(Carta carta){
        return cartaRepository.save(carta);
    }

    /**
     * Elimina una carta de la base de datos por su identificador.
     *
     * @param id El identificador de la carta a eliminar.
     */
    public void deleteCartaById(Long id){
        cartaRepository.deleteById(id);
    }
}
