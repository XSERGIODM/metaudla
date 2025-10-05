package com.udlaverso.metaudla.servicies.gestion_usuarios;


import com.udlaverso.metaudla.DTO.UsuarioDTO;
import com.udlaverso.metaudla.DTO.create.CreateUsuarioDTO;
import com.udlaverso.metaudla.DTO.update.UpdateUsuarioDTO;
import com.udlaverso.metaudla.enums.EstadoBasico;
import com.udlaverso.metaudla.enums.Rol;
import com.udlaverso.metaudla.models.Usuario;
import com.udlaverso.metaudla.repositories.UsuarioRepository;
import com.udlaverso.metaudla.utils.MapDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GestionUsuarioImpl implements IGestionUsuario {

    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UsuarioDTO crearUsuario(CreateUsuarioDTO createUsuarioDTO) {
        Usuario usuario = new Usuario();
        usuario.setNombre(createUsuarioDTO.getNombre());
        usuario.setUsername(createUsuarioDTO.getUsername());
        usuario.setCorreo(createUsuarioDTO.getCorreo());
        usuario.setContrasena(passwordEncoder.encode(createUsuarioDTO.getContrasena()));
        usuario.setRol(Rol.ESTUDIANTE);
        usuario.setCreatedBy(usuario);
        usuario.setUpdatedBy(usuario);

        return MapDTO.mapModelToDTO(usuarioRepository.save(usuario));
    }

    @Override
    public Optional<UsuarioDTO> obtenerUsuarioPorId(Long id) {
        Usuario usuario = usuarioRepository.findById(id).orElse(null);
        if (usuario != null) {
            return Optional.of(MapDTO.mapModelToDTO(usuario));
        }
        return Optional.empty();
    }

    @Override
    public Optional<UsuarioDTO> obtenerUsuarioPorUsername(String username) {
        Usuario usuario = usuarioRepository.findUsuarioByUsername(username);
        if (usuario != null) {
            return Optional.of(MapDTO.mapModelToDTO(usuario));
        }
        return Optional.empty();
    }

    @Override
    public Optional<UsuarioDTO> obtenerUsuarioPorCorreo(String correo) {
        Usuario usuario = usuarioRepository.findUsuarioByCorreo(correo);
        if (usuario != null) {
            return Optional.of(MapDTO.mapModelToDTO(usuario));
        }
        return Optional.empty();
    }

    @Override
    public List<UsuarioDTO> obtenerTodosLosUsuarios() {
        List<Usuario> usuarios = usuarioRepository.findAll();

        if (!usuarios.isEmpty()) {
            return MapDTO.listMapModelToDTO(usuarios);
        }
        return List.of();
    }

    @Override
    public Optional<UsuarioDTO> actualizarUsuario(Long id, UpdateUsuarioDTO updateUsuarioDTO) {
        return Optional.empty();
    }

    @Override
    public boolean eliminarUsuario(Long id) {
        Usuario usuario = usuarioRepository.findById(id).orElse(null);
        if (usuario != null) {
            usuarioRepository.delete(usuario);
            return true;
        }
        return false;
    }

    @Override
    public boolean existeUsuarioPorId(Long id) {
        return usuarioRepository.existsById(id);
    }

    @Override
    public boolean existeUsuarioPorUsername(String username) {
        return usuarioRepository.existsByUsername(username);
    }

    @Override
    public boolean existeUsuarioPorCorreo(String correo) {
        return usuarioRepository.existsByCorreo(correo);
    }

    @Override
    public long contarUsuarios() {
        return usuarioRepository.count();
    }

    @Override
    public List<UsuarioDTO> obtenerUsuariosPorEstado(EstadoBasico estado) {
        List<Usuario> usuarios = usuarioRepository.findUsuarioByEstado(estado);
        if (!usuarios.isEmpty()) {
            return MapDTO.listMapModelToDTO(usuarios);
        }
        return List.of();
    }

    @Override
    public List<UsuarioDTO> obtenerUsuariosPorRol(Rol rol) {
        List<Usuario> usuarios = usuarioRepository.findAllByRol(rol);
        if (!usuarios.isEmpty()) {
            return MapDTO.listMapModelToDTO(usuarios);
        }
        return List.of();
    }
}
