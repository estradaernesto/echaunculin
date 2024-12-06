package com.sidrerias.controller;

import com.sidrerias.modelo.Plato;
import com.sidrerias.service.impl.PlatoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * Controlador REST para gestionar las operaciones CRUD relacionadas con los platos.
 * Proporciona endpoints para crear, recuperar, actualizar, eliminar platos,
 * y calcular el precio medio de un plato específico.
 */
@RestController
@RequestMapping("/api/platos")
public class PlatoController {

    @Autowired
    private PlatoService platoService;

    /**
     * Crea un nuevo plato.
     *
     * @param plato El plato a ser creado.
     * @return El plato creado con estado HTTP 200.
     */
    @PostMapping
    public ResponseEntity<Plato> createPlato(@RequestBody Plato plato) {
        Plato newPlato = platoService.savePlato(plato);
        return ResponseEntity.ok(newPlato);
    }

    /**
     * Obtiene todos los platos disponibles.
     *
     * @return Una lista de todos los platos con estado HTTP 200.
     */
    @GetMapping
    public ResponseEntity<List<Plato>> getAllPlatos() {
        List<Plato> platos = platoService.getAllPlatos();
        return ResponseEntity.ok(platos);
    }

    /**
     * Obtiene un plato por su identificador único.
     *
     * @param id El identificador del plato.
     * @return El plato solicitado envuelto en un objeto Optional con estado HTTP 200.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Optional<Plato>> getPlatoById(@PathVariable Long id) {
        Optional<Plato> plato = platoService.getPlatoById(id);
        return ResponseEntity.ok(plato);
    }

    /**
     * Actualiza un plato existente por su identificador único.
     *
     * @param updatedPlato El plato con las actualizaciones aplicadas.
     * @return El plato actualizado con estado HTTP 200.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Plato> updatePlato(@RequestBody Plato updatedPlato) {
        Plato plato = platoService.updatePlato(updatedPlato);
        return ResponseEntity.ok(plato);
    }

    /**
     * Elimina un plato basándose en su identificador único.
     *
     * @param id El identificador del plato a eliminar.
     * @return Un cuerpo vacío con estado HTTP 200.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePlato(@PathVariable Long id) {
        platoService.deletePlatoById(id);
        return ResponseEntity.ok().build();
    }

    /**
     * Calcula el precio medio de un plato basado en su nombre.
     *
     * @param nombre El nombre del plato para calcular el precio medio.
     * @return El precio medio del plato con estado HTTP 200 o NOT_FOUND si no se encuentra.
     */
    @GetMapping("/plato/{nombre}/precio-medio/")
    public ResponseEntity<Double> getPrecioMedioDelPlato(@RequestParam String nombre) {
        Double precioMedio = platoService.calcularPrecioMedioDelPlato(nombre);
        if (precioMedio == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(precioMedio, HttpStatus.OK);
    }
}
