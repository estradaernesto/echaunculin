package com.sidrerias.service.impl;

import com.sidrerias.modelo.Plato;
import com.sidrerias.repository.PlatoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Servicio que proporciona la lógica de negocio para los platos.
 * Utiliza {@link PlatoRepository} para interactuar con la base de datos.
 */
@Service
public class PlatoService {
    @Autowired
    private PlatoRepository platoRepository;

    /**
     * Guarda o actualiza un plato en la base de datos.
     *
     * @param plato El plato a guardar o actualizar.
     * @return El plato guardado o actualizado.
     */
    public Plato savePlato(Plato plato){
        return platoRepository.save(plato);
    }

    /**
     * Recupera un plato por su identificador único.
     *
     * @param id El identificador del plato.
     * @return Un {@link Optional} que contiene el plato si es encontrado, o vacío si no existe.
     */
    public Optional<Plato> getPlatoById(Long id){
        return platoRepository.findById(id);
    }

    /**
     * Obtiene todos los platos disponibles.
     *
     * @return Una lista de todos los platos.
     */
    public List<Plato> getAllPlatos(){
        return platoRepository.findAll();
    }

    /**
     * Actualiza la información de un plato existente.
     *
     * @param plato El plato con la información actualizada.
     * @return El plato actualizado.
     */
    public Plato updatePlato(Plato plato){
        return platoRepository.save(plato);
    }

    /**
     * Elimina un plato de la base de datos por su identificador.
     *
     * @param id El identificador del plato a eliminar.
     */
    public void deletePlatoById(Long id){
        platoRepository.deleteById(id);
    }

    /**
     * Calcula el precio medio de los platos con un nombre específico.
     *
     * @param nombrePlato El nombre del plato para el cual calcular el precio medio.
     * @return El precio medio de los platos encontrados o un valor por defecto si no se encuentran platos.
     */
    public Double calcularPrecioMedioDelPlato(String nombrePlato) {
        return platoRepository.findByNombre(nombrePlato).stream()
                .mapToDouble(Plato::getPrecio)
                .average()
                .orElse(Double.NaN); // O podría ser un valor por defecto o lanzar una excepción si se prefiere.
    }
}
