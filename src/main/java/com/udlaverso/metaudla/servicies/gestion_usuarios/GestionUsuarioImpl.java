package com.udlaverso.metaudla.servicies.gestion_usuarios;

import com.udlaverso.metaudla.DTOs.usuario.UsuarioDtoCreate;
import com.udlaverso.metaudla.DTOs.usuario.UsuarioResponseDto;
import com.udlaverso.metaudla.enums.EstadoBasico;
import com.udlaverso.metaudla.enums.Rol;
import com.udlaverso.metaudla.entities.Usuario;
import com.udlaverso.metaudla.mappers.UsuarioMapper;
import com.udlaverso.metaudla.repositories.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GestionUsuarioImpl implements IGestionUsuario {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final UsuarioMapper usuarioMapper;


    @Override
    public UsuarioResponseDto crearUsuario(UsuarioDtoCreate usuarioDtoCreate) {
        Usuario usuario = usuarioMapper.toEntity(usuarioDtoCreate);
        System.out.println("Creando usuario servicio: " + usuario.toString());
        usuario.setContrasena(passwordEncoder.encode(usuario.getContrasena()));
        usuario = usuarioRepository.save(usuario);
        return usuarioMapper.toDto(usuario);
    }

    @Override
    public Optional<UsuarioResponseDto> obtenerUsuarioPorId(Long id) {
        Optional<Usuario> usuarioOptional = usuarioRepository.findById(id);
        return usuarioOptional.map(usuarioMapper::toDto);
    }

    @Override
    public Optional<UsuarioResponseDto> obtenerUsuarioPorUsername(String username) {
        return Optional.empty();
    }

    @Override
    public Optional<UsuarioResponseDto> obtenerUsuarioPorCorreo(String correo) {
        return Optional.empty();
    }

    @Override
    public Page<UsuarioResponseDto> obtenerTodosLosUsuarios(Pageable pageable) {
        Page<Usuario> usuarios = usuarioRepository.findAll(pageable);
        return usuarios.map(usuarioMapper::toDto);
    }

    @Override
    public boolean eliminarUsuario(Long id) {
        return false;
    }

    @Override
    public boolean existeUsuarioPorId(Long id) {
        return false;
    }

    @Override
    public boolean existeUsuarioPorUsername(String username) {
        return false;
    }

    @Override
    public boolean existeUsuarioPorCorreo(String correo) {
        return false;
    }

    @Override
    public long contarUsuarios() {
        return 0;
    }

    @Override
    public List<UsuarioResponseDto> obtenerUsuariosPorEstado(EstadoBasico estado) {
        return List.of();
    }

    @Override
    public List<UsuarioResponseDto> obtenerUsuariosPorRol(Rol rol) {
        return List.of();
    }

    @Override
    public UsuarioResponseDto cambiarContrasena(Long id, String contrasenaVieja, String contrasenaNueva) {
        Optional<Usuario> usuarioOptional = usuarioRepository.findById(id);
        if (usuarioOptional.isPresent()) {
            Usuario usuario = usuarioOptional.get();
            if (passwordEncoder.matches(contrasenaVieja, usuario.getContrasena())) {
                usuario.setContrasena(passwordEncoder.encode(contrasenaNueva));
                usuario = usuarioRepository.save(usuario);
                return usuarioMapper.toDto(usuario);
            }
        }
        return null;
    }

    @Override
    public void deshabilitarUsuario(Long id) {
        Usuario usuario = usuarioRepository.findById(id).orElse(null);
        if (usuario == null) {
            throw new IllegalArgumentException("El usuario no existe");
        }
        usuario.setEstado(EstadoBasico.DESHABILITADO);
        usuarioRepository.save(usuario);
    }

    @Override
    public void habilitarUsuario(Long id) {
        Usuario usuario = usuarioRepository.findById(id).orElse(null);
        if (usuario == null) {
            throw new IllegalArgumentException("El usuario no existe");
        }
        usuario.setEstado(EstadoBasico.HABILITADO);
        usuarioRepository.save(usuario);
    }

    @Override
    public Optional<UsuarioResponseDto> actualizarUsuario(UsuarioDtoCreate usuarioDtoCreate) {
        Optional<Usuario> usuarioOptional = usuarioRepository.findById(usuarioDtoCreate.getId());
        if (usuarioOptional.isPresent()) {
            Usuario usuario = usuarioOptional.get();
            usuario.setNombre(usuarioDtoCreate.getNombre());
            usuario.setCorreo(usuarioDtoCreate.getCorreo());
            usuario.setContrasena(passwordEncoder.encode(usuarioDtoCreate.getContrasena()));
            usuario.setRol(usuarioDtoCreate.getRol());
            usuario = usuarioRepository.save(usuario);
            return Optional.of(usuarioMapper.toDto(usuario));
        }
        return Optional.empty();
    }
}
