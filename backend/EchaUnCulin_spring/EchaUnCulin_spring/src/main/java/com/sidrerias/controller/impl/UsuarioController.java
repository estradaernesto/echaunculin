package com.sidrerias.controller.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sidrerias.dto.SidreriaDTO;
import com.sidrerias.dto.SidreriaRequest;
import com.sidrerias.modelo.Usuario;
import com.sidrerias.service.impl.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


/**
 * Controlador REST para gestionar las operaciones CRUD relacionadas con los usuarios.
 * Proporciona endpoints para crear, recuperar, actualizar y eliminar usuarios.
 */
@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {
    @Autowired
    private UsuarioService usuarioService;

    /**
     * Crea un nuevo usuario.
     *
     * @param usuario El usuario a ser creado.
     * @return El usuario creado con estado HTTP 200.
     */
    @PostMapping
    public ResponseEntity<Usuario> createUsuario(@RequestBody Usuario usuario) {
        Usuario newUsuario = usuarioService.saveUsuario(usuario);
        return ResponseEntity.ok(newUsuario);
    }

    /**
     * Obtiene todos los usuarios registrados.
     *
     * @return Lista de todos los usuarios con estado HTTP 200.
     */
    @GetMapping
    public ResponseEntity<List<Usuario>> getAllUsuarios() {
        List<Usuario> usuarios = usuarioService.getAllUsuarios();
        return ResponseEntity.ok(usuarios);
    }

    /**
     * Obtiene un usuario específico por su identificador único.
     *
     * @param id El identificador del usuario.
     * @return El usuario solicitado envuelto en un objeto Optional con estado HTTP 200.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Optional<Usuario>> getUsuarioById(@PathVariable Long id) {
        Optional<Usuario> usuario = usuarioService.getUsuarioById(id);
        return ResponseEntity.ok(usuario);
    }

    @GetMapping("/{id}/sidrerias/favoritos")
    public ResponseEntity<List<SidreriaDTO>>getSidreriasFavoritasByUsuario(@PathVariable Long id){
        return ResponseEntity.ok(usuarioService.getSidreriasFavoritasByUsuario(id));
    }

    /**
     * Actualiza un usuario existente identificado por su ID.
     *
     * @param updatedUsuarioJSON El usuario con los datos actualizados.
     * @return El usuario actualizado con estado HTTP 200.
     */
    @PutMapping(value="/{id}",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Usuario> updateUsuario(@RequestParam("usuario") String updatedUsuarioJSON,
                                                 @PathVariable Long id, @RequestParam("avatar") MultipartFile avatar) {
        Usuario usuario = deserializarJSON(updatedUsuarioJSON);
        Usuario usuarioActualizado = usuarioService.updateUsuario(usuario,id,avatar);
        return ResponseEntity.ok(usuarioActualizado);
    }

    /**
     * Elimina un usuario basándose en su identificador.
     *
     * @param id El identificador del usuario a eliminar.
     * @return Un cuerpo vacío con estado HTTP 200.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUsuario(@PathVariable Long id) {
        usuarioService.deleteUsuarioById(id);
        return ResponseEntity.ok().build();
    }

    private Usuario deserializarJSON(String usuarioJson) {
        ObjectMapper objectMapper = new ObjectMapper();
        Usuario usuario = null;
        try {
            // Permitir JSON sin todos los campos obligatorios
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            usuario = objectMapper.readValue(usuarioJson, Usuario.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error al deserializar el JSON: " + e.getMessage(), e);
        }
        return usuario;
    }


}

