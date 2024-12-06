package com.sidrerias.service.impl;

import com.sidrerias.dto.SidreriaDTO;
import com.sidrerias.modelo.Sidra;
import com.sidrerias.repository.SidraRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Servicio que proporciona la lógica de negocio para las sidras.
 * Utiliza {@link SidraRepository} para interactuar con la base de datos.
 */
@Service
public class SidraService {
    @Autowired
    private SidraRepository sidraRepository;

    /**
     * Guarda o actualiza una sidra en la base de datos.
     *
     * @param sidra La sidra a guardar o actualizar.
     * @return La sidra guardada o actualizada.
     */
    public Sidra saveSidra(Sidra sidra){
        return sidraRepository.save(sidra);
    }

    /**
     * Recupera una sidra por su identificador único.
     *
     * @param id El identificador de la sidra.
     * @return Un {@link Optional} que contiene la sidra si es encontrada, o vacío si no existe.
     */
    public Optional<Sidra> getSidraById(Long id){
        return sidraRepository.findById(id);
    }

    /**
     * Obtiene todas las sidras disponibles.
     *
     * @return Una lista de todas las sidras.
     */
    public List<Sidra> getAllSidras(){
        return sidraRepository.findAll();
    }

    /**
     * Actualiza la información de una sidra existente.
     *
     * @param sidra La sidra con la información actualizada.
     * @return La sidra actualizada.
     */
    public Sidra updateSidra(Sidra sidra){
        return sidraRepository.save(sidra);
    }

    /**
     * Elimina una sidra de la base de datos por su identificador.
     *
     * @param id El identificador de la sidra a eliminar.
     */
    public void deleteSidraById(Long id){
        sidraRepository.deleteById(id);
    }

    /**
     * Obtiene las tres mejores sidras con denominación de origen basadas en su valoración.
     *
     * @return Una lista de las tres mejores sidras con denominación de origen.
     */
    public List<Sidra> topTresSidraDO(){
        return this.getAllSidras().stream()
                .filter(Sidra::isEsDenominacionOrigen)
                .sorted(Comparator.comparing(Sidra::getValoracion).reversed())
                .limit(3)
                .collect(Collectors.toList());
    }


}

