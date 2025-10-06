package com.udlaverso.metaudla.servicies.gestion_usuarios;

import com.udlaverso.metaudla.DTOs.usuario.UsuarioDtoCreate;
import com.udlaverso.metaudla.DTOs.usuario.UsuarioResponseDto;
import com.udlaverso.metaudla.enums.EstadoBasico;
import com.udlaverso.metaudla.enums.Rol;
import com.udlaverso.metaudla.entities.Usuario;
import com.udlaverso.metaudla.mappers.UsuarioMapper;
import com.udlaverso.metaudla.repositories.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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
        usuario.setContrasena(passwordEncoder.encode(usuario.getContrasena()));
        usuario = usuarioRepository.save(usuario);
        return usuarioMapper.toDto(usuario);
    }

    @Override
    public Optional<UsuarioResponseDto> obtenerUsuarioPorId(Long id) {
        return Optional.empty();
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
    public List<UsuarioResponseDto> obtenerTodosLosUsuarios() {
        List<Usuario> usuarios = usuarioRepository.findAll();
        List<UsuarioResponseDto> usuarioResponseDtos = new ArrayList<>();
        if (!usuarios.isEmpty()) {
            for (Usuario usuario : usuarios) {
                usuarioResponseDtos.add(usuarioMapper.toDto(usuario));
            }
        }
        return usuarioResponseDtos;
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
}
