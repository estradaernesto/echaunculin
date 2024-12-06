package com.sidrerias.service.impl;

import com.sidrerias.dto.*;
import com.sidrerias.modelo.*;
import com.sidrerias.repository.SidreriaRepository;
import com.sidrerias.repository.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.WeekFields;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Servicio que proporciona la lógica de negocio para las sidrerías.
 * Utiliza {@link SidreriaRepository} para interactuar con la base de datos.
 */
@Service

public class SidreriaService {
    private static final String IMAGENES_SIDRERIA="src/main/resources/static/imagenes/sidrerias";
    @Autowired
    private SidreriaRepository sidreriaRepository;

    @Autowired
    private SidreriaRepository usuarioRepository;




    /**
     * Guarda o actualiza una sidrería en la base de datos.
     *
     * @param idGerente
     * La sidrería a guardar
     * @return La sidrería guardada o actualizada.
     */
    public Optional<Sidreria> saveSidreriaByGerente(SidreriaRequest sidreriaRequest, Long idGerente, List<MultipartFile>imagenes){
        Sidreria sidreria=sidreriaRequest.toEntity();
        sidreria.setGerente(new Usuario(idGerente));
        sidreria.setImagenes(imagenes.stream().map(this::guardarImagen).map(rut->
                new ImagenSidreria(sidreria,rut)).collect(Collectors.toSet()));
       return Optional.of( sidreriaRepository.save(sidreria));

    }

    /**
     * Recupera una sidrería por su identificador único.
     *
     * @param id El identificador de la sidrería.
     * @return Un {@link Optional} que contiene la sidrería si es encontrada, o vacío si no existe.
     */
    public SidreriaDetalleDTO getSidreriaById(Long id){
        return sidreriaRepository.findById(id).map(sidreria ->
                mapToSidreriaDetalleDto(sidreria)
        ).orElseThrow(() -> new EntityNotFoundException("La sidrería con ID " + id + " no existe"));
    }

    /**
     * Obtiene todas las sidrerías disponibles.
     *
     * @return Una lista de todas las sidrerías.
     */
    /*
    public List<Sidreria> getAllSidrerias(){
        return sidreriaRepository.findAll();
    }

    /**
     * Actualiza la información de una sidrería existente.
     *
     * @param sidreria La sidrería con la información actualizada.
     * @return La sidrería actualizada.
     */
    public Optional<Sidreria> updateSidreriaByGerente(SidreriaRequest sidreriaRequest,Long idGerente, List<MultipartFile>imagenes){
        // Buscar la sidrería asociada al gerente
        Sidreria existingSidreria = sidreriaRepository.findByGerenteId(idGerente)
                .orElseThrow(() -> new NoSuchElementException("No se encontró una sidrería asociada al gerente con ID: " + idGerente));
        if (!imagenes.isEmpty()){
            existingSidreria.getImagenes().clear();
            existingSidreria.getImagenes().addAll(imagenes.stream().map(this::guardarImagen).map(rut->
                    new ImagenSidreria(existingSidreria,rut)).collect(Collectors.toSet()));
            actualizarCamposSidreria(sidreriaRequest, existingSidreria);
        }

        // Persistir y retornar la entidad actualizada
        return Optional.of(sidreriaRepository.save(existingSidreria));

    }

    public SidreriaDetalleDTO  getSidreriaByGerente(Long idGerente){
      Sidreria sidreria= sidreriaRepository.findByGerenteId(idGerente).get();
      return mapToSidreriaDetalleDto(sidreria);

    }



    /**
     * Elimina una sidrería de la base de datos por su identificador.
     *
     * @param id El identificador de la sidrería a eliminar.
     */
    public void deleteSidreriaById(Long id){
        sidreriaRepository.deleteById(id);
    }

    /**
     * Obtiene el menú de una sidrería en una fecha específica.
     *
     * @param sidreriaId El identificador de la sidrería.
     * @param fecha La fecha para la cual obtener el menú.
     * @return Un {@link Optional} que contiene el menú si está presente.
     */
    public Optional<Menu> getMenuBySidreriaAndFecha(Long sidreriaId, Date fecha) {
        return sidreriaRepository.findMenuBySidreriaIdAndFecha(sidreriaId, fecha);
    }

    /**
     * Obtiene los menús de una sidrería para una semana específica basada en una fecha dada.
     *
     * @param sidreriaId El identificador de la sidrería.
     * @param fecha La fecha dentro de la semana de interés.
     * @return Una lista de menús para la semana de la fecha dada.
     */
    public List<Menu> getMenusBySidreriaPorSemana(Long sidreriaId, Date fecha) {
        return sidreriaRepository.findById(sidreriaId).orElse(new Sidreria()).getMenus().stream()
                .filter(menu -> isSameWeek(menu.getFechaPublicacion(), fecha))
                .collect(Collectors.toList());
    }

    /**
     * Obtiene la carta de platos de una sidrería.
     *
     * @param sidreriaId El identificador de la sidrería.
     * @return Un conjunto de platos disponibles en la sidrería.
     */
    /*
    public Set<Plato> getCartaBySidreria(Long sidreriaId) {
        return sidreriaRepository.findById(sidreriaId)
                .map(Sidreria::getCarta)
                .map(Carta::getPlatos) // Asegura que no se llame getPlatos en un objeto nulo.
                .orElse(Collections.emptySet()); // Devuelve un conjunto vacío si no hay Carta.
    }

*/

    /**
     * Encuentra la sidrería con el mayor número de reseñas negativas.
     *
     * @return La sidrería con el mayor número de reseñas negativas o null si no hay ninguna.
     */
    public Sidreria getSidreriaConMasResenasNegativas() {
        return sidreriaRepository.findAll().stream()
                .map(sidreria -> new AbstractMap.SimpleEntry<>(
                        sidreria,
                        sidreria.getReviews().stream()
                                .filter(review -> review.getValoracion() < 2.5)
                                .count()))
                .filter(entry -> entry.getValue() > 0)
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);
    }

    /**
     * Agrupa las sidrerías por el número de usuarios que las han visitado y las ordena de mayor a menor.
     *
     * @return Una lista de {@link SidreriaDTOEjemplo} que representa las sidrerías agrupadas por usuarios.
     */
    public List<SidreriaDTOEjemplo> getSidreriasAgrupadasPorUsuarios() {
        return sidreriaRepository.findAll().stream()
                .map(sidreria -> new SidreriaDTOEjemplo(
                        sidreria.getNombre(),
                        sidreria.getUsuariosVisitan().size()))
                .sorted(Comparator.comparing(SidreriaDTOEjemplo::getNumeroDeUsuarios).reversed())
                .collect(Collectors.toList());
    }

    /**
     * Comprueba si dos fechas caen en la misma semana.
     *
     * @param date1 La primera fecha a comparar.
     * @param date2 La segunda fecha a comparar.
     * @return true si las fechas están en la misma semana, false en caso contrario.
     */
    private boolean isSameWeek(Date date1, Date date2) {
        LocalDate localDate1 = LocalDate.ofInstant(date1.toInstant(), ZoneId.systemDefault());
        LocalDate localDate2 = LocalDate.ofInstant(date2.toInstant(), ZoneId.systemDefault());
        return localDate1.get(WeekFields.ISO.weekOfYear()) == localDate2.get(WeekFields.ISO.weekOfYear());
    }

    public List<SidreriaDTO> getAllSidrerias(Long idUsuario, boolean isWithFavoritos){
       return sidreriaRepository.findAll().stream().map(sidreria-> SidreriaDTO.builder()
                .idSidreria(sidreria.getId())
                .idHostelero(sidreria.getGerente().getId())
                .nombre(sidreria.getNombre())
                .imagenes(sidreria.getImagenes().stream().map(ImagenSidreria::getRuta).collect(Collectors.toSet()))
                .precioComensal(sidreria.getPrecioComensal())
                .ubicacion(sidreria.getUbicacion())
                .esEscanciado(sidreria.isEscanciado())
                .carta(sidreria.getRutaCarta())
                .valoracion(sidreria.getValoracion())
                .isFavorita(isWithFavoritos?isSidreriaFavorita(idUsuario, sidreria.getId()):false)
                .build()
        ).toList();



    }

    public List<SidreriaDTO>filtrarSidrerias(List<SidreriaDTO>sidrerias, String nombre,Double precioComensal,Double valoracion) {
        return sidrerias.stream()
                .filter(sidreria -> nombre == null || sidreria.getNombre().toLowerCase().contains(nombre.toLowerCase()))
                .filter(sidreria -> precioComensal ==null|| sidreria.getPrecioComensal() <= precioComensal)
                .filter(sidreria -> valoracion ==null|| sidreria.getValoracion() >= valoracion)
                .collect(Collectors.toList());
    }

    @Transactional
    public boolean  updateSidreriaFavoritaByUsuario(FavoritoRequest favoritoRequest){
   int resultado=     favoritoRequest.isAltaFavorito()?
                sidreriaRepository.agregarSidreriaAFavoritos(favoritoRequest.getIdUsuario(), favoritoRequest.getIdSidreria()):
                sidreriaRepository.eliminarSidreriaDeFavoritos(favoritoRequest.getIdUsuario(),favoritoRequest.getIdSidreria());
   return resultado>0;
}

private boolean isSidreriaFavorita(Long idUsuario,Long idSidreria){
       return  sidreriaRepository.IsSidreriaFavorita(idUsuario, idSidreria);
    }

    private SidreriaDetalleDTO mapToSidreriaDetalleDto(Sidreria sidreria){
        return SidreriaDetalleDTO.builder()
                .sidreriaDTO(mapToSidreriaDto(sidreria,false,-1L))
                .reviews(sidreria.getReviews().stream()
                        .map(this::toReviewDTO)
                        .sorted(Comparator.comparing(ReviewDTO::getFechaPublicacion)
                                .reversed())
                        .toList())
                .build();
    }

    private SidreriaDTO mapToSidreriaDto(Sidreria sidreria, boolean isWithFavoritos, Long idUsuario){
        return SidreriaDTO.builder()
                .idSidreria(sidreria.getId())
                .idHostelero(sidreria.getGerente().getId())
                .nombre(sidreria.getNombre())
                .imagenes(sidreria.getImagenes().stream().map(ImagenSidreria::getRuta).collect(Collectors.toSet()))
                .precioComensal(sidreria.getPrecioComensal())
                .esEscanciado(sidreria.isEscanciado())
                .ubicacion(sidreria.getUbicacion())
                .carta(sidreria.getRutaCarta())
                .valoracion(sidreria.getValoracion())
                .isFavorita(isWithFavoritos?isSidreriaFavorita(idUsuario, sidreria.getId()):false)
                .build();
    }

    public ReviewDTO toReviewDTO(Review review){
        return ReviewDTO.builder()
                .id(review.getId())
                .valoracion(review.getValoracion())
                .contenido(review.getContenido())
                .fechaPublicacion(review.getFechaPublicacion())
                .isEditado(review.isEditado())
                .titulo(review.getTitulo())
                .respuesta(review.getRespuesta())
                .idUsuario(review.getUsuario().getId())
                .nombreUsuario(review.getUsuario().getNombre())
                .apellidosUsuario(review.getUsuario().getApellidos())
                .role(review.getUsuario().getRole())
                .rutaAvatar(review.getUsuario().getRutaAvatar())
                                .build();
    }


    public Sidreria saveSidreria(Sidreria sidreria,List<MultipartFile> imagenes) {
     return    sidreriaRepository.save(sidreria);
    }

    private  Sidreria actualizarCamposSidreria(SidreriaRequest sidreriaRequest, Sidreria existingSidreria) {
        // Actualizar campos básicos
        if (sidreriaRequest.getNombre() != null) {
            existingSidreria.setNombre(sidreriaRequest.getNombre());
        }
        if (sidreriaRequest.getUbicacion() != null) {
            existingSidreria.setUbicacion(sidreriaRequest.getUbicacion());
        }
        if (sidreriaRequest.getValoracion() > 0) {
            existingSidreria.setValoracion(sidreriaRequest.getValoracion());
        }
        if (sidreriaRequest.getPrecioComensal() > 0) {
            existingSidreria.setPrecioComensal(sidreriaRequest.getPrecioComensal());
        }
        existingSidreria.setEscanciado(sidreriaRequest.isEsEscanciado());
        if (sidreriaRequest.getRutaCarta() != null) {
            existingSidreria.setRutaCarta(sidreriaRequest.getRutaCarta());
        }

        return existingSidreria;
    }
private String guardarImagen(MultipartFile imagen){
        try {
            Path directorioImagenesSidreria = Paths.get(IMAGENES_SIDRERIA);
            if (!Files.exists(directorioImagenesSidreria)) {
                Files.createDirectories(directorioImagenesSidreria);
            }
            String nombreArchivo = UUID.randomUUID() + "_" + imagen.getOriginalFilename();
            Path rutaArchivo = directorioImagenesSidreria.resolve(nombreArchivo);
            Files.copy(imagen.getInputStream(), rutaArchivo, StandardCopyOption.REPLACE_EXISTING);
            return "imagenes/sidrerias/"+nombreArchivo;
        }catch (IOException ioException){
            throw new RuntimeException("Error al guardar la imagen: " + imagen.getOriginalFilename(), ioException);
        }
}



}
