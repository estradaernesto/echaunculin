package com.sidrerias.service.impl;

import com.sidrerias.modelo.Menu;
import com.sidrerias.modelo.Plato;
import com.sidrerias.modelo.Sidreria;
import com.sidrerias.repository.MenuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * Servicio que proporciona la lógica de negocio para los menús.
 * Utiliza {@link MenuRepository} para interactuar con la base de datos.
 */
@Service
public class MenuService {

    @Autowired
    private MenuRepository menuRepository;

    /**
     * Guarda o actualiza un menú en la base de datos.
     *
     * @param menu El menú a guardar o actualizar.
     * @return El menú guardado o actualizado.
     */
    public Menu saveMenu(Menu menu) {
        return menuRepository.save(menu);
    }

    /**
     * Recupera un menú por su identificador único.
     *
     * @param id El identificador del menú.
     * @return Un {@link Optional} que contiene el menú si es encontrado, o vacío si no existe.
     */
    public Optional<Menu> getMenuById(Long id) {
        return menuRepository.findById(id);
    }

    /**
     * Obtiene todos los menús disponibles.
     *
     * @return Una lista de todos los menús.
     */
    public List<Menu> getAllMenus() {
        return menuRepository.findAll();
    }

    /**
     * Actualiza la información de un menú existente.
     *
     * @param menu El menú con la información actualizada.
     * @return El menú actualizado.
     */
    public Menu updateMenu(Menu menu) {
        return menuRepository.save(menu);
    }

    /**
     * Elimina un menú de la base de datos por su identificador.
     *
     * @param id El identificador del menú a eliminar.
     */
    public void deleteMenuById(Long id) {
        menuRepository.deleteById(id);
    }

    /**
     * Obtiene los platos asociados a un menú específico.
     *
     * @param id El identificador del menú del cual obtener los platos.
     * @return Una lista de platos asociados al menú especificado.
     */
    public List<Plato> getPlatosByMenu(Long id) {
        Menu menuById = getMenuById(id).orElse(null);
        return menuById != null ? menuRepository.findPlatosByMenu(menuById) : List.of();
    }

    /**
     * Obtiene un menú único para una sidrería específica en una fecha dada.
     *
     * @param sidreria La sidrería a la que pertenece el menú.
     * @param fecha La fecha de publicación del menú.
     * @return El menú encontrado o un nuevo menú si no existe.
     */
    public Menu obtenerMenuUnicoPorSidreriaYFecha(Sidreria sidreria, Date fecha) {
        return menuRepository.findBySidreriaAndFechaPublicacion(sidreria, fecha).orElse(new Menu());
    }

}
