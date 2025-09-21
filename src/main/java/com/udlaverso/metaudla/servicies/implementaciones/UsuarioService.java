package com.udlaverso.metaudla.servicies.implementaciones;

import com.udlaverso.metaudla.enums.EstadoBasico;
import com.udlaverso.metaudla.enums.Rol;
import com.udlaverso.metaudla.models.Usuario;
import com.udlaverso.metaudla.repositories.UsuarioRepository;
import com.udlaverso.metaudla.servicies.IUsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UsuarioService implements IUsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Crear usuario
    public Usuario crearUsuario(Usuario usuario, Usuario creador) {
        // Validar datos
        validarUsuario(usuario);

        // Encriptar contraseña
        usuario.setContrasena(passwordEncoder.encode(usuario.getContrasena()));

        // Establecer valores por defecto
        usuario.setEstado(EstadoBasico.HABILITADO);
        if (usuario.getRol() == null) {
            usuario.setRol(Rol.ESTUDIANTE);
        }

        // Auditoría
        usuario.setCreatedBy(creador);
        usuario.setCreatedAt(LocalDateTime.now());

        return usuarioRepository.save(usuario);
    }

    // Actualizar usuario
    public Usuario actualizarUsuario(Long id, Usuario usuarioActualizado, Usuario actualizador) {
        Usuario usuario = usuarioRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Actualizar campos
        usuario.setNombre(usuarioActualizado.getNombre());
        usuario.setUsername(usuarioActualizado.getUsername());
        usuario.setCorreo(usuarioActualizado.getCorreo());
        usuario.setFotoPerfilUrl(usuarioActualizado.getFotoPerfilUrl());
        usuario.setRol(usuarioActualizado.getRol());
        usuario.setEstado(usuarioActualizado.getEstado());

        // Si se proporciona nueva contraseña, encriptarla
        if (usuarioActualizado.getContrasena() != null && !usuarioActualizado.getContrasena().isEmpty()) {
            usuario.setContrasena(passwordEncoder.encode(usuarioActualizado.getContrasena()));
        }

        // Auditoría
        usuario.setUpdatedBy(actualizador);
        usuario.setUpdatedAt(LocalDateTime.now());

        return usuarioRepository.save(usuario);
    }

    // Buscar por ID
    public Optional<Usuario> buscarPorId(Long id) {
        return usuarioRepository.findById(id);
    }

    // Buscar por username
    public Optional<Usuario> buscarPorUsername(String username) {
        return usuarioRepository.findByUsername(username);
    }

    // Buscar por email
    public Optional<Usuario> buscarPorCorreo(String correo) {
        return usuarioRepository.findByCorreo(correo);
    }

    // Listar todos los usuarios
    public List<Usuario> listarTodos() {
        return usuarioRepository.findAll();
    }

    // Listar por estado
    public List<Usuario> listarPorEstado(EstadoBasico estado) {
        return usuarioRepository.findByEstado(estado);
    }

    // Listar por rol
    public List<Usuario> listarPorRol(Rol rol) {
        return usuarioRepository.findByRol(rol);
    }

    // Deshabilitar usuario
    public Usuario deshabilitarUsuario(Long id, Usuario actualizador) {
        Usuario usuario = usuarioRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        usuario.setEstado(EstadoBasico.DESHABILITADO);
        usuario.setUpdatedBy(actualizador);
        usuario.setUpdatedAt(LocalDateTime.now());

        return usuarioRepository.save(usuario);
    }

    // Habilitar usuario
    public Usuario habilitarUsuario(Long id, Usuario actualizador) {
        Usuario usuario = usuarioRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        usuario.setEstado(EstadoBasico.HABILITADO);
        usuario.setUpdatedBy(actualizador);
        usuario.setUpdatedAt(LocalDateTime.now());

        return usuarioRepository.save(usuario);
    }

    // Cambiar contraseña
    public void cambiarContrasena(Long id, String nuevaContrasena, Usuario actualizador) {
        Usuario usuario = usuarioRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Validar nueva contraseña
        validarContrasena(nuevaContrasena);

        usuario.setContrasena(passwordEncoder.encode(nuevaContrasena));
        usuario.setUpdatedBy(actualizador);
        usuario.setUpdatedAt(LocalDateTime.now());

        usuarioRepository.save(usuario);
    }

    // Verificar contraseña
    public boolean verificarContrasena(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

    // Autenticar usuario
    public Optional<Usuario> autenticar(String username, String password) {
        Optional<Usuario> usuario = buscarPorUsername(username);

        if (usuario.isPresent() && usuario.get().getEstado() == EstadoBasico.HABILITADO) {
            if (verificarContrasena(password, usuario.get().getContrasena())) {
                return usuario;
            }
        }

        return Optional.empty();
    }

    // Validaciones
    private void validarUsuario(Usuario usuario) {
        if (usuario.getNombre() == null || usuario.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre es obligatorio");
        }

        if (usuario.getUsername() == null || usuario.getUsername().trim().isEmpty()) {
            throw new IllegalArgumentException("El username es obligatorio");
        }

        if (usuario.getCorreo() == null || usuario.getCorreo().trim().isEmpty()) {
            throw new IllegalArgumentException("El correo es obligatorio");
        }

        if (usuario.getContrasena() == null || usuario.getContrasena().trim().isEmpty()) {
            throw new IllegalArgumentException("La contraseña es obligatoria");
        }

        validarContrasena(usuario.getContrasena());

        // Verificar unicidad
        if (usuarioRepository.existsByUsername(usuario.getUsername())) {
            throw new IllegalArgumentException("El username ya está en uso");
        }

        if (usuarioRepository.existsByCorreo(usuario.getCorreo())) {
            throw new IllegalArgumentException("El correo ya está registrado");
        }
    }

    private void validarContrasena(String contrasena) {
        if (contrasena.length() < 8) {
            throw new IllegalArgumentException("La contraseña debe tener al menos 8 caracteres");
        }

        if (!contrasena.matches(".*[a-z].*")) {
            throw new IllegalArgumentException("La contraseña debe contener al menos una letra minúscula");
        }

        if (!contrasena.matches(".*[A-Z].*")) {
            throw new IllegalArgumentException("La contraseña debe contener al menos una letra mayúscula");
        }

        if (!contrasena.matches(".*[0-9].*")) {
            throw new IllegalArgumentException("La contraseña debe contener al menos un número");
        }

        if (!contrasena.matches(".*[@#$%^&+=].*")) {
            throw new IllegalArgumentException("La contraseña debe contener al menos un carácter especial (@#$%^&+=)");
        }
    }

    // Eliminar usuario
    public void eliminarUsuario(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        usuarioRepository.delete(usuario);
    }

    // Búsquedas avanzadas
    public List<Usuario> buscarPorNombreOrUsername(String nombre, String username) {
        return usuarioRepository.findByNombreOrUsername(nombre, username);
    }

    // Estadísticas
    public long contarUsuariosActivos() {
        return usuarioRepository.countByEstado(EstadoBasico.HABILITADO);
    }

    public long contarUsuariosPorRol(Rol rol) {
        return usuarioRepository.countByRol(rol);
    }
}