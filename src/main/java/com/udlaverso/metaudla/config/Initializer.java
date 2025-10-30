package com.udlaverso.metaudla.config;

import com.udlaverso.metaudla.entities.Categoria;
import com.udlaverso.metaudla.entities.Isla;
import com.udlaverso.metaudla.entities.Usuario;
import com.udlaverso.metaudla.enums.EstadoBasico;
import com.udlaverso.metaudla.enums.Rol;
import com.udlaverso.metaudla.repositories.CategoriaRepository;
import com.udlaverso.metaudla.repositories.IslaRepository;
import com.udlaverso.metaudla.repositories.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;

@Component
@RequiredArgsConstructor
public class Initializer implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final CategoriaRepository categoriaRepository;
    private final IslaRepository islaRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Crear usuarios por defecto si no hay ninguno
        if (usuarioRepository.count() == 0) {
            Usuario admin = new Usuario();
            admin.setNombre("Administrador");
            admin.setUsername("admin");
            admin.setCorreo("admin@metaudla.com");
            admin.setContrasena(passwordEncoder.encode("Admin123!"));
            admin.setRol(Rol.ADMINISTRADOR);
            admin.setCreatedAt(LocalDateTime.now());

            Usuario estudiante1 = new Usuario();
            estudiante1.setNombre("Juan Pérez");
            estudiante1.setUsername("juanp");
            estudiante1.setCorreo("juan.perez@udla.edu.ec");
            estudiante1.setContrasena(passwordEncoder.encode("Juan123!"));
            estudiante1.setRol(Rol.ESTUDIANTE);
            estudiante1.setCreatedAt(LocalDateTime.now());

            Usuario estudiante2 = new Usuario();
            estudiante2.setNombre("María García");
            estudiante2.setUsername("mariag");
            estudiante2.setCorreo("maria.garcia@udla.edu.ec");
            estudiante2.setContrasena(passwordEncoder.encode("Maria123!"));
            estudiante2.setRol(Rol.ESTUDIANTE);
            estudiante2.setCreatedAt(LocalDateTime.now());

            Usuario profesor = new Usuario();
            profesor.setNombre("Carlos Rodríguez");
            profesor.setUsername("carlosr");
            profesor.setCorreo("carlos.rodriguez@udla.edu.ec");
            profesor.setContrasena(passwordEncoder.encode("Carlos123!"));
            profesor.setRol(Rol.PROFESOR);
            profesor.setCreatedAt(LocalDateTime.now());

            usuarioRepository.save(admin);
            usuarioRepository.save(estudiante1);
            usuarioRepository.save(estudiante2);
            usuarioRepository.save(profesor);

            System.out.println("Usuarios por defecto creados.");
        }

        // Crear categorías por defecto si no hay ninguna
        if (categoriaRepository.count() == 0) {
            Categoria categoria1 = new Categoria();
            categoria1.setNombre("Tecnología");
            categoria1.setDescripcion("Categoría relacionada con temas tecnológicos");
            categoria1.setEstado(EstadoBasico.HABILITADO);

            Categoria categoria2 = new Categoria();
            categoria2.setNombre("Ciencias");
            categoria2.setDescripcion("Categoría relacionada con ciencias naturales");
            categoria2.setEstado(EstadoBasico.HABILITADO);

            Categoria categoria3 = new Categoria();
            categoria3.setNombre("Humanidades");
            categoria3.setDescripcion("Categoría relacionada con humanidades y artes");
            categoria3.setEstado(EstadoBasico.HABILITADO);

            categoriaRepository.save(categoria1);
            categoriaRepository.save(categoria2);
            categoriaRepository.save(categoria3);

            System.out.println("Categorías por defecto creadas.");
        }

        // Crear islas por defecto si no hay ninguna
        if (islaRepository.count() == 0) {
            Usuario admin = usuarioRepository.findUsuarioByUsername("admin");

            Isla isla1 = new Isla();
            isla1.setNombre("Introducción a la Programación");
            isla1.setDescripcion("Una isla básica para aprender los fundamentos de la programación");
            isla1.setEtiquetas(Arrays.asList("programación", "básico", "introducción"));
            isla1.setImagenes(Arrays.asList("https://www.unir.net/wp-content/uploads/2024/02/La-importancia-de-la-programacion-segura-o-desarrollo-seguro-de-software.jpg", "https://example.com/programacion2.jpg"));
            isla1.setEstado(EstadoBasico.HABILITADO);
            isla1.setAutor(admin);
            isla1.setFechaCreacion(LocalDateTime.now());

            Isla isla2 = new Isla();
            isla2.setNombre("Matemáticas Avanzadas");
            isla2.setDescripcion("Contenido avanzado de matemáticas para estudiantes universitarios");
            isla2.setEtiquetas(Arrays.asList("matemáticas", "avanzado", "universitario"));
            isla2.setImagenes(Arrays.asList("https://imagenes2.eltiempo.com/files/image_1200_535/uploads/2019/10/27/5db5cf6ca49ea.jpeg", "https://example.com/matematicas2.jpg"));
            isla2.setEstado(EstadoBasico.HABILITADO);
            isla2.setAutor(admin);
            isla2.setFechaCreacion(LocalDateTime.now());

            Isla isla3 = new Isla();
            isla3.setNombre("Historia del Arte");
            isla3.setDescripcion("Exploración de las diferentes épocas y movimientos artísticos");
            isla3.setEtiquetas(Arrays.asList("arte", "historia", "cultura"));
            isla3.setImagenes(Arrays.asList("https://media.admagazine.com/photos/618a6acacc7069ed5077ca7c/16:9/w_2560%2Cc_limit/69052.jpg", "https://example.com/arte2.jpg"));
            isla3.setEstado(EstadoBasico.HABILITADO);
            isla3.setAutor(admin);
            isla3.setFechaCreacion(LocalDateTime.now());

            islaRepository.save(isla1);
            islaRepository.save(isla2);
            islaRepository.save(isla3);

            System.out.println("Islas por defecto creadas.");
        }
    }
}