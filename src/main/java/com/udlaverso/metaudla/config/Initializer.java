package com.udlaverso.metaudla.config;

import com.udlaverso.metaudla.entities.Categoria;
import com.udlaverso.metaudla.entities.Isla;
import com.udlaverso.metaudla.entities.Usuario;
import com.udlaverso.metaudla.enums.EstadoBasico;
import com.udlaverso.metaudla.enums.Rol;
import com.udlaverso.metaudla.repositories.CategoriaRepository;
import com.udlaverso.metaudla.repositories.IslaRepository;
import com.udlaverso.metaudla.repositories.UsuarioRepository;
import com.udlaverso.metaudla.servicies.MinioService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class Initializer implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final CategoriaRepository categoriaRepository;
    private final IslaRepository islaRepository;
    private final PasswordEncoder passwordEncoder;
    private final MinioService minioService;

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

            // Crear bucket único "isla" y cargar assets a MinIO y actualizar URLs
            cargarAssetsAMinio();
        }
    }

    private void cargarAssetsAMinio() {
        log.info("Iniciando carga de assets a MinIO...");

        try {
            // Obtener todas las islas creadas
            List<Isla> islas = islaRepository.findAll();
            if (islas.isEmpty()) {
                log.warn("No se encontraron islas para cargar assets.");
                return;
            }

            // Usar bucket único "isla" para todas las islas
            String bucketName = "isla";
            log.info("Verificando/creando bucket único: {}", bucketName);
            minioService.createBucket(bucketName);

            // Mapas para almacenar URLs por isla
            Map<Long, List<String>> imagenesPorIsla = new HashMap<>();
            Map<Long, List<String>> videosPorIsla = new HashMap<>();

            // Cargar imágenes y videos para cada isla
            for (Isla isla : islas) {
                Long islaId = isla.getId();

                // Cargar imágenes con prefijo correcto
                List<String> imagenesUrls = cargarArchivos(bucketName, "assets/image/", islaId + "/imagen/");
                if (!imagenesUrls.isEmpty()) {
                    imagenesPorIsla.put(islaId, imagenesUrls);
                    log.info("Imágenes cargadas para isla {}: {}", islaId, imagenesUrls.size());
                }

                // Cargar videos con prefijo correcto
                List<String> videosUrls = cargarArchivos(bucketName, "assets/video/", islaId + "/video/");
                if (!videosUrls.isEmpty()) {
                    videosPorIsla.put(islaId, videosUrls);
                    log.info("Videos cargados para isla {}: {}", islaId, videosUrls.size());
                }
            }

            // Después de tener todas las URLs, actualizar las entidades Isla
            for (Isla isla : islas) {
                Long islaId = isla.getId();
                List<String> imagenes = imagenesPorIsla.get(islaId);
                if (imagenes != null && !imagenes.isEmpty()) {
                    isla.setImagenes(imagenes);
                }
                List<String> videos = videosPorIsla.get(islaId);
                if (videos != null && !videos.isEmpty()) {
                    isla.setVideos(videos);
                }
                // Guardar la isla actualizada
                islaRepository.save(isla);
            }

            log.info("Carga de assets a MinIO completada exitosamente.");

        } catch (Exception e) {
            log.error("Error durante la carga de assets a MinIO: {}", e.getMessage(), e);
        }
    }

    private List<String> cargarArchivos(String bucketName, String assetsPath, String prefix) {
        List<String> urls = new ArrayList<>();

        try {
            // Obtener archivos desde resources
            Resource resource = new ClassPathResource(assetsPath);
            if (!resource.exists()) {
                log.warn("Directorio de assets no encontrado: {}", assetsPath);
                return urls;
            }

            Path assetsDir = Paths.get(resource.getURI());
            if (!Files.exists(assetsDir) || !Files.isDirectory(assetsDir)) {
                log.warn("Directorio de assets no es válido: {}", assetsPath);
                return urls;
            }

            // Listar archivos en el directorio
            Files.list(assetsDir).forEach(filePath -> {
                if (Files.isRegularFile(filePath)) {
                    try {
                        String fileName = filePath.getFileName().toString();
                        String objectName = prefix + fileName;

                        // Determinar content type
                        String contentType = determinarContentType(fileName);

                        // Subir archivo a MinIO
                        try (var inputStream = Files.newInputStream(filePath)) {
                            long size = Files.size(filePath);
                            minioService.uploadFile(bucketName, objectName, inputStream, size, contentType);
                            log.info("Archivo subido: {} -> {}", fileName, objectName);
                        }

                        // Generar URL presigned (expira en 1 año = 31536000 segundos)
                        String presignedUrl = minioService.getPresignedUrl(bucketName, objectName, 31536000);
                        urls.add(presignedUrl);

                    } catch (Exception e) {
                        log.error("Error al procesar archivo {}: {}", filePath.getFileName(), e.getMessage());
                    }
                }
            });

        } catch (IOException e) {
            log.error("Error al acceder al directorio de assets {}: {}", assetsPath, e.getMessage());
        }

        return urls;
    }

    private String determinarContentType(String fileName) {
        String lowerFileName = fileName.toLowerCase();
        if (lowerFileName.endsWith(".jpg") || lowerFileName.endsWith(".jpeg")) {
            return "image/jpeg";
        } else if (lowerFileName.endsWith(".png")) {
            return "image/png";
        } else if (lowerFileName.endsWith(".gif")) {
            return "image/gif";
        } else if (lowerFileName.endsWith(".mp4")) {
            return "video/mp4";
        } else if (lowerFileName.endsWith(".avi")) {
            return "video/x-msvideo";
        } else if (lowerFileName.endsWith(".mov")) {
            return "video/quicktime";
        } else {
            return "application/octet-stream"; // Tipo por defecto
        }
    }
}