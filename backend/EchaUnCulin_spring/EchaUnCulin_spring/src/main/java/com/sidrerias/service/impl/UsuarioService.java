package com.sidrerias.service.impl;


import com.sidrerias.dto.SidreriaDTO;
import com.sidrerias.dto.UserResponse;
import com.sidrerias.modelo.Sidreria;
import com.sidrerias.modelo.Usuario;
import com.sidrerias.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UsuarioService {
    private static final String  IMAGEN_AVATAR="src/main/resources/static/imagenes/avatares";
    @Autowired
    private UsuarioRepository usuarioRepository;

    /**
     * Guarda un nuevo usuario en la base de datos o actualiza uno existente.
     * Si el usuario ya existe (determinado por su ID), se actualizará; de lo contrario, se creará uno nuevo.
     *
     * @param usuario El usuario a guardar o actualizar.
     * @return El usuario guardado o actualizado.
     */
    public Usuario saveUsuario(Usuario usuario){
        return usuarioRepository.save(usuario);
    }

    /**
     * Recupera un usuario por su identificador único.
     *
     * @param id El ID del usuario a buscar.
     * @return Un Optional conteniendo el usuario encontrado, o un Optional vacío si no se encuentra ningún usuario con el ID proporcionado.
     */
    public Optional<Usuario> getUsuarioById(Long id){
        return usuarioRepository.findById(id);
    }

    /**
     * Obtiene una lista de todos los usuarios en la base de datos.
     *
     * @return Una lista de todos los usuarios.
     */
    public List<Usuario> getAllUsuarios(){
        return usuarioRepository.findAll();
    }

    /**
     * Actualiza la información de un usuario existente. Si el usuario no existe, se creará un nuevo registro.
     *
     * @param usuario El usuario con la información actualizada.
     * @return El usuario actualizado.
     */
    public Usuario updateUsuario(Usuario usuario){
        return usuarioRepository.save(usuario);
    }

    /**
     * Elimina un usuario de la base de datos utilizando su identificador único.
     *
     * @param id El ID del usuario a eliminar.
     */
    public void deleteUsuarioById(Long id){
        usuarioRepository.deleteById(id);
    }


    public List<SidreriaDTO>getSidreriasFavoritasByUsuario(Long idUsuario){
        return usuarioRepository.findById(idUsuario).get().getSidreriasFavoritas().stream().map(sidreria -> sidreria.toDto(true)).collect(Collectors.toList());
    }

    public Usuario updateUsuario(Usuario newUser,Long idUsuario,MultipartFile imagen){
      Usuario existingUser  =usuarioRepository.findById(idUsuario).get();
        existingUser = actualizarCamposUsuario(existingUser, newUser);
        existingUser.setRutaAvatar(guardarImagen(imagen));
        return existingUser;
    }

    private Usuario actualizarCamposUsuario(Usuario existingUser, Usuario newUser){
        if(newUser.getNombre() != null && !newUser.getNombre().isBlank()){
            existingUser.setNombre(newUser.getNombre());
        }

        if(newUser.getApellidos()!=null &&!newUser.getApellidos().isBlank()){
            existingUser.setApellidos(newUser.getApellidos());
        }

        if(newUser.getEmail() != null && !newUser.getEmail().isBlank() && !usuarioRepository.findByEmail(newUser.getEmail()).isPresent()) {

            existingUser.setEmail(newUser.getEmail());
        }
        if(newUser.getUsername()!=null && !newUser.getUsername().isBlank()&& !usuarioRepository.findByUsername(newUser.getUsername()).isPresent()){
            existingUser.setUsername(newUser.getUsername());

        }
        return existingUser;

    }
    private String guardarImagen(MultipartFile imagen){
        try {
            Path directorioImagenesSidreria = Paths.get(IMAGEN_AVATAR);
            if (!Files.exists(directorioImagenesSidreria)) {
                Files.createDirectories(directorioImagenesSidreria);
            }
            String nombreArchivo = UUID.randomUUID() + "_" + imagen.getOriginalFilename();
            Path rutaArchivo = directorioImagenesSidreria.resolve(nombreArchivo);
            Files.copy(imagen.getInputStream(), rutaArchivo, StandardCopyOption.REPLACE_EXISTING);
            return "imagenes/avatares/"+nombreArchivo;
        }catch (IOException ioException){
            throw new RuntimeException("Error al guardar la imagen: " + imagen.getOriginalFilename(), ioException);
        }
    }



}

