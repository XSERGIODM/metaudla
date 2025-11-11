package com.udlaverso.metaudla.servicies.gestion_login;

import com.udlaverso.metaudla.DTOs.usuario.UsuarioResponseDto;
import com.udlaverso.metaudla.entities.Usuario;
import com.udlaverso.metaudla.mappers.UsuarioMapper;
import com.udlaverso.metaudla.repositories.UsuarioRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class GestionLoginImpl implements IGestionLogin {

    private final UsuarioRepository usuarioRepository;
    private  final PasswordEncoder passwordEncoder;
    private final UsuarioMapper usuarioMapper;


    public Optional<UsuarioResponseDto> autenticarUsuario(String usernameOrEmail, String contrasena) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findByUsernameOrCorreo(usernameOrEmail, usernameOrEmail);
        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            if (passwordEncoder.matches(contrasena, usuario.getContrasena())) {
                return Optional.of(usuarioMapper.toDto(usuario));
            }
        }
        return Optional.empty();
    }
}
