package com.sidrerias.controller.impl;

import com.sidrerias.modelo.Sidra;
import com.sidrerias.service.impl.SidraService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * Controlador REST para gestionar las operaciones CRUD relacionadas con las sidras.
 * Proporciona endpoints para crear, recuperar, actualizar y eliminar sidras,
 * así como un endpoint para obtener un ranking de las sidras.
 */
@RestController
@RequestMapping("/api/sidras")
public class SidraController {
    @Autowired
    private SidraService sidraService;

    /**
     * Crea una nueva sidra.
     *
     * @param sidra La sidra a ser creada.
     * @return La sidra creada con estado HTTP 200.
     */
    @PostMapping
    public ResponseEntity<Sidra> createSidra(@RequestBody Sidra sidra) {
        Sidra newSidra = sidraService.saveSidra(sidra);
        return ResponseEntity.ok(newSidra);
    }

    /**
     * Obtiene todas las sidras almacenadas.
     *
     * @return Lista de todas las sidras con estado HTTP 200.
     */
    @GetMapping
    public ResponseEntity<List<Sidra>> getAllSidras() {
        List<Sidra> sidras = sidraService.getAllSidras();
        return ResponseEntity.ok(sidras);
    }

    /**
     * Obtiene una sidra específica por su identificador.
     *
     * @param id El identificador de la sidra.
     * @return La sidra solicitada envuelta en un objeto Optional con estado HTTP 200.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Optional<Sidra>> getSidraById(@PathVariable Long id) {
        Optional<Sidra> sidra = sidraService.getSidraById(id);
        return ResponseEntity.ok(sidra);
    }

    /**
     * Actualiza una sidra existente identificada por su ID.
     *
     * @param updatedSidra La sidra con los datos actualizados.
     * @return La sidra actualizada con estado HTTP 200.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Sidra> updateSidra(@RequestBody Sidra updatedSidra) {
        Sidra sidra = sidraService.updateSidra(updatedSidra);
        return ResponseEntity.ok(sidra);
    }

    /**
     * Elimina una sidra basándose en su identificador.
     *
     * @param id El identificador de la sidra a eliminar.
     * @return Un cuerpo vacío con estado HTTP 200.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSidra(@PathVariable Long id) {
        sidraService.deleteSidraById(id);
        return ResponseEntity.ok().build();
    }

    /**
     * Obtiene un ranking de las sidras basado en la denominación de origen.
     *
     * @return Una lista de las sidras ordenadas según su ranking con estado HTTP 200.
     */
    @GetMapping("/ranking")
    public ResponseEntity<List<Sidra>> getRankingSidras() {
        return ResponseEntity.ok(sidraService.topTresSidraDO());
    }
}

