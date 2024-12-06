package com.sidrerias.controller;

import com.sidrerias.modelo.Menu;
import com.sidrerias.modelo.Plato;
import com.sidrerias.service.impl.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * Controlador REST para gestionar las operaciones CRUD relacionadas con los menús.
 * Proporciona endpoints para crear, recuperar, actualizar y eliminar menús,
 * así como para obtener los platos asociados a un menú.
 */
@RestController
@RequestMapping("/api/menus")
public class MenuController {

    @Autowired
    private MenuService menuService;

    /**
     * Crea un nuevo menú.
     *
     * @param menu El menú a crear.
     * @return El menú creado con estado HTTP 200.
     */
    @PostMapping
    public ResponseEntity<Menu> createMenu(@RequestBody Menu menu) {
        Menu newMenu = menuService.saveMenu(menu);
        return ResponseEntity.ok(newMenu);
    }

    /**
     * Obtiene todos los menús disponibles.
     *
     * @return Una lista de todos los menús con estado HTTP 200.
     */
    @GetMapping
    public ResponseEntity<List<Menu>> getAllMenus() {
        List<Menu> menus = menuService.getAllMenus();
        return ResponseEntity.ok(menus);
    }

    /**
     * Obtiene un menú por su identificador único.
     *
     * @param id El identificador del menú.
     * @return El menú solicitado envuelto en un objeto Optional con estado HTTP 200.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Optional<Menu>> getMenuById(@PathVariable Long id) {
        Optional<Menu> menu = menuService.getMenuById(id);
        return ResponseEntity.ok(menu);
    }

    /**
     * Actualiza un menú existente por su identificador único.
     *
     * @param updatedMenu El menú con las actualizaciones aplicadas.
     * @return El menú actualizado con estado HTTP 200.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Menu> updateMenu(@RequestBody Menu updatedMenu) {
        Menu menu = menuService.updateMenu(updatedMenu);
        return ResponseEntity.ok(menu);
    }

    /**
     * Elimina un menú basándose en su identificador único.
     *
     * @param id El identificador del menú a eliminar.
     * @return Un cuerpo vacío con estado HTTP 200.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMenu(@PathVariable Long id) {
        menuService.deleteMenuById(id);
        return ResponseEntity.ok().build();
    }

    /**
     * Obtiene todos los platos asociados a un menú específico.
     *
     * @param id El identificador del menú del cual obtener los platos.
     * @return Una lista de platos del menú especificado con estado HTTP 200.
     */
    @GetMapping("/{id}/platos")
    public ResponseEntity<List<Plato>> getPlatosMenu(@PathVariable Long id) {
        List<Plato> platosMenu = menuService.getPlatosByMenu(id);
        return ResponseEntity.ok(platosMenu);
    }
}
