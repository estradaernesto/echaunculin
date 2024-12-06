package com.sidrerias.controller.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sidrerias.dto.*;
import com.sidrerias.modelo.Menu;
import com.sidrerias.modelo.Plato;
import com.sidrerias.modelo.Sidreria;
import com.sidrerias.service.impl.SidreriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Controlador REST para gestionar las operaciones CRUD relacionadas con las sidrerías.
 * Proporciona endpoints para crear, recuperar, actualizar y eliminar sidrerías,
 * así como para obtener menús por fecha y la carta de una sidrería.
 */
@RestController
@RequestMapping("/api/sidrerias")
public class SidreriaController {
    @Autowired
    private SidreriaService sidreriaService;



    /**
     * Obtiene todas las sidrerías almacenadas.
     *
     * @return Lista de todas las sidrerías con estado HTTP 200.
     */
    @GetMapping("/")
    public ResponseEntity<List<SidreriaDTO>> getAllSidrerias(
            @RequestParam(value = "nombre", required = false) String nombre,
            @RequestParam(value = "precioComensal", required = false) Double precioComensal,
            @RequestParam(value = "valoracion", required = false) Double valoracion) {
        List<SidreriaDTO> sidrerias = sidreriaService.getAllSidrerias(-1L,false);
        return ResponseEntity.ok(sidreriaService.filtrarSidrerias(sidrerias,nombre,precioComensal,valoracion));
    }
    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<List<SidreriaDTO>> getAllSidrerias(@PathVariable Long idUsuario) {
        List<SidreriaDTO> sidrerias = sidreriaService.getAllSidrerias(idUsuario,true);
        return ResponseEntity.ok(sidrerias);
    }

    @PostMapping("/favorito")
    public ResponseEntity<String>updateSidreriaFavoritaByUsuario(@RequestBody FavoritoRequest favoritoRequest){
        boolean resultado=sidreriaService.updateSidreriaFavoritaByUsuario(favoritoRequest);
        return resultado?ResponseEntity.ok("Favorito actualizado correctamente")
                :ResponseEntity.status(HttpStatus.NOT_FOUND).body("La actualización del favorito no se ha realizado correctamente");
    }



    /**
     * Obtiene una sidrería específica por su identificador.
     *
     * @param id El identificador de la sidrería.
     * @return La sidrería solicitada envuelta en un objeto Optional con estado HTTP 200.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Optional<SidreriaDetalleDTO>> getSidreriaById(@PathVariable Long id) {
        SidreriaDetalleDTO sidreriaDetalleDTO = sidreriaService.getSidreriaById(id);
        return ResponseEntity.ok(Optional.ofNullable(sidreriaDetalleDTO));
    }

    /**
     * Actualiza una sidrería existente identificada por su ID.
     *
     * @param updatedSidreriaJSON La sidrería con los datos actualizados.
     * @return La sidrería actualizada con estado HTTP 200.
     */
    @PutMapping(value = "/gerente/{id}",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<MessageResponse> updateSidreriaByGerente(@RequestParam("sidreria") String updatedSidreriaJSON,
                                                                   @PathVariable Long id,
                                                                   @RequestParam("imagenes") List<MultipartFile> imagenes
    ) {
        SidreriaRequest sidreriaRequest=deserializarJSON(updatedSidreriaJSON);
        Optional<Sidreria> optionalSidreria = sidreriaService.updateSidreriaByGerente(sidreriaRequest, id,imagenes);

        return optionalSidreria
                .map(saved -> ResponseEntity.status(HttpStatus.CREATED)
                        .body(MessageResponse.builder()
                                .status("OK")
                                .mensaje("Sidrería actualizada con éxito.")
                                .build()))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(MessageResponse.builder()
                                .status("ERROR")
                                .mensaje("No se pudo actualizar la sidrería.")
                                .build()));
    }

    @PostMapping(value = "/gerente/{id}",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<MessageResponse> saveSidreriaByGerente(
            @RequestParam("sidreria") String newSidreriaJSON,
            @PathVariable Long id,
            @RequestParam("imagenes") List<MultipartFile> imagenes) {
 SidreriaRequest sidreriaRequest=deserializarJSON(newSidreriaJSON);
        Optional<Sidreria> optionalSidreria = sidreriaService.saveSidreriaByGerente(sidreriaRequest, id,imagenes);

        return optionalSidreria
                .map(saved -> ResponseEntity.status(HttpStatus.CREATED)
                        .body(MessageResponse.builder()
                                .status("OK")
                                .mensaje("Sidrería creada con éxito.")
                                .build()))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(MessageResponse.builder()
                                .status("ERROR")
                                .mensaje("No se pudo crear la sidrería.")
                                .build()));
    }

    /**
     * Crea una nueva sidrería.
     *
     * @param sidreria La sidrería a ser creada.
     * @return La sidrería creada con estado HTTP 200.
     */
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Sidreria> createSidreria(@RequestBody Sidreria sidreria,@RequestParam("imagenes") List<MultipartFile> imagenes){
        Sidreria newSidreria = sidreriaService.saveSidreria(sidreria,imagenes);
        return ResponseEntity.ok(newSidreria);
    }

    @GetMapping("/gerente/{id}")
    public ResponseEntity<SidreriaDetalleDTO> updateSidreriaByGerente( @PathVariable Long id) {
        return ResponseEntity.ok(sidreriaService.getSidreriaByGerente(id));

    }


    /**
     * Elimina una sidrería basándose en su identificador.
     *
     * @param id El identificador de la sidrería a eliminar.
     * @return Un cuerpo vacío con estado HTTP 200.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSidreria(@PathVariable Long id) {
        sidreriaService.deleteSidreriaById(id);
        return ResponseEntity.ok().build();
    }

    /**
     * Obtiene el menú de una sidrería en una fecha específica.
     *
     * @param sidreriaId El identificador de la sidrería.
     * @param fecha La fecha para la cual obtener el menú.
     * @return El menú solicitado o un estado HTTP 404 si no se encuentra.
     */
    @GetMapping("/{id}/menu/{fecha}")
    public ResponseEntity<Menu> getMenuBySidreriaAndFecha(
            @PathVariable Long sidreriaId,
            @PathVariable String fecha) {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            Date date = formatter.parse(fecha);
            Optional<Menu> menuOptional = sidreriaService.getMenuBySidreriaAndFecha(sidreriaId, date);
            return menuOptional.map(ResponseEntity::ok).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
        } catch (ParseException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Obtiene los menús de una sidrería para una semana específica basada en una fecha dada.
     *
     * @param sidreriaId El identificador de la sidrería.
     * @param fecha La fecha dentro de la semana de interés.
     * @return Una lista de menús para la semana de la fecha dada.
     */
    @GetMapping("/{sidreriaId}/menus/semana/{fecha}")
    public List<Menu> getMenusBySidreriaAndWeek(
            @PathVariable Long sidreriaId,
            @PathVariable Date fecha) {
        return sidreriaService.getMenusBySidreriaPorSemana(sidreriaId, fecha);
    }

    /**
     * Obtiene la carta de platos de una sidrería.
     *
     * @param sidreriaId El identificador de la sidrería.
     * @return Un conjunto de platos disponibles en la sidrería con estado HTTP 200.
     */
    /*
    @GetMapping("/{sidreriaId}/carta")
    public ResponseEntity<Set<Plato>> getCartaBySidreria(@PathVariable Long sidreriaId) {
        Set<Plato> carta = sidreriaService.getCartaBySidreria(sidreriaId);
        return new ResponseEntity<>(carta, HttpStatus.OK);
    }
*/
    /**
     * Obtiene la sidrería con el mayor número de reseñas negativas.
     *
     * @return La sidrería con más reseñas negativas o un estado HTTP 204 si no hay ninguna.
     */
    @GetMapping("/sidreria-peor-valorada")
    public ResponseEntity<Sidreria> getSidreriaConMasResenasNegativas() {
        Sidreria sidreriaConMasReviewsNegativas = sidreriaService.getSidreriaConMasResenasNegativas();
        return sidreriaConMasReviewsNegativas != null ? new ResponseEntity<>(sidreriaConMasReviewsNegativas, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /**
     * Obtiene una lista de sidrerías agrupadas por el número de usuarios que las han visitado.
     *
     * @return Una lista de sidrerías DTO agrupadas por usuarios visitantes.
     */
    @GetMapping("/agrupadas-por-usuarios")
    public ResponseEntity<List<SidreriaDTOEjemplo>> getSidreriasAgrupadasPorUsuarios() {
        List<SidreriaDTOEjemplo> sidreriasAgrupadasPorUsuarios = sidreriaService.getSidreriasAgrupadasPorUsuarios();
        return !sidreriasAgrupadasPorUsuarios.isEmpty() ? new ResponseEntity<>(sidreriasAgrupadasPorUsuarios, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    private SidreriaRequest deserializarJSON(String sidreriaJson) {
        ObjectMapper objectMapper = new ObjectMapper();
        SidreriaRequest sidreriaRequest = null;
        try {
            // Permitir JSON sin todos los campos obligatorios
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            sidreriaRequest = objectMapper.readValue(sidreriaJson, SidreriaRequest.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error al deserializar el JSON: " + e.getMessage(), e);
        }
        return sidreriaRequest;
    }

}

