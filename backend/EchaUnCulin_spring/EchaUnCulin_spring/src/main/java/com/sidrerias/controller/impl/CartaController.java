package com.sidrerias.controller;

import com.sidrerias.modelo.Carta;
import com.sidrerias.service.impl.CartaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * Controlador REST para gestionar las operaciones CRUD de las cartas.
 */
@RestController
@RequestMapping("/api/cartas")
public class CartaController {

    @Autowired
    private CartaService cartaService;

    /**
     * Crea una nueva carta.
     *
     * @param carta La entidad carta a crear.
     * @return La carta creada con un estado HTTP 200.
     */
    @PostMapping
    public ResponseEntity<Carta> createCarta(@RequestBody Carta carta){
        Carta newCarta = cartaService.saveCarta(carta);
        return ResponseEntity.ok(newCarta);
    }

    /**
     * Obtiene todas las cartas disponibles.
     *
     * @return Una lista de cartas con un estado HTTP 200.
     */
    @GetMapping
    public ResponseEntity<List<Carta>> getAllCartas() {
        List<Carta> cartas = cartaService.getAllCartas();
        return ResponseEntity.ok(cartas);
    }

    /**
     * Obtiene una carta específica por su identificador.
     *
     * @param id El identificador único de la carta.
     * @return La carta solicitada envuelta en un Optional con un estado HTTP 200.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Optional<Carta>> getCartaById(@PathVariable Long id) {
        Optional<Carta> carta = cartaService.getCartaById(id);
        return ResponseEntity.ok(carta);
    }

    /**
     * Actualiza una carta existente.
     *
     * @param updatedCarta La entidad carta actualizada.
     * @return La carta actualizada con un estado HTTP 200.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Carta> updateCarta(@RequestBody Carta updatedCarta) {
        Carta carta = cartaService.updateCarta(updatedCarta);
        return ResponseEntity.ok(carta);
    }

    /**
     * Elimina una carta específica por su identificador.
     *
     * @param id El identificador único de la carta a eliminar.
     * @return Un cuerpo vacío con un estado HTTP 200.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCarta(@PathVariable Long id) {
        cartaService.deleteCartaById(id);
        return ResponseEntity.ok().build();
    }
}
