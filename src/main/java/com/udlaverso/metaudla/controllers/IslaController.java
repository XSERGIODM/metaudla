package com.udlaverso.metaudla.controllers;

import com.udlaverso.metaudla.DTOs.isla.IslaDtoCreate;
import com.udlaverso.metaudla.DTOs.isla.IslaDtoResponse;
import com.udlaverso.metaudla.entities.Isla;
import com.udlaverso.metaudla.repositories.IslaRepository;
import com.udlaverso.metaudla.servicies.MinioService;
import com.udlaverso.metaudla.servicies.gestion_islas.IGestionIsla;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/isla")
@CrossOrigin("*")
@RequiredArgsConstructor
public class IslaController {
    private final IGestionIsla gestionIsla;
    private final MinioService minioService;
    private final IslaRepository islaRepository;
    private static final Logger logger = LoggerFactory.getLogger(IslaController.class);


    @GetMapping()
    public ResponseEntity<List<IslaDtoResponse>> listarIsla() {
        return ResponseEntity.ok(gestionIsla.obtenerTodasIslas());
    }

    @GetMapping("/paginadas")
    public ResponseEntity<Page<IslaDtoResponse>> listarIslasPaginadas(
            @RequestParam(required = false,defaultValue = "0") int pagina,
            @RequestParam(required = false,defaultValue = "10") int tamanio
    ) {
        Pageable pageable = PageRequest.of(pagina, tamanio);
        return ResponseEntity.ok(gestionIsla.obtenerIslasPaginadas(pageable));
    }

    @GetMapping("/tendencias")
    public ResponseEntity<Page<IslaDtoResponse>> listarIslasTendencias(
            @RequestParam(required = false,defaultValue = "0") int pagina,
            @RequestParam(required = false,defaultValue = "10") int tamanio
    ) {
        Pageable pageable = PageRequest.of(pagina, tamanio);

        return ResponseEntity.ok(gestionIsla.obtenerIslasTendencias(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<IslaDtoResponse> obtenerIsla(@PathVariable Long id) {
        IslaDtoResponse islaDTORespuesta = gestionIsla.obtenerIslaPorId(id).orElse(null);
        return ResponseEntity.ok(islaDTORespuesta);
    }

    // Agregar en IslaController.java
    @GetMapping("/autor/{autorId}")
    public ResponseEntity<List<IslaDtoResponse>> obtenerIslasPorAutor(@PathVariable Long autorId) {
        return ResponseEntity.ok(gestionIsla.obtenerIslasPorAutor(autorId));
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<IslaDtoResponse> crearIsla(@Validated @ModelAttribute IslaDtoCreate islaDtoCreate) {
        try {
            // Crear la isla en la base de datos primero
            Isla isla = gestionIsla.crearIsla(islaDtoCreate);

            // Usar bucket único "isla"
            String bucketName = "isla";
            minioService.createBucket(bucketName);

            // Procesar imágenes
            if (islaDtoCreate.getImagenesArchivos() != null && !islaDtoCreate.getImagenesArchivos().isEmpty()) {
                for (MultipartFile imagen : islaDtoCreate.getImagenesArchivos()) {
                    if (imagen != null && !imagen.isEmpty()) {
                        // Validar tipo de archivo
                        String contentType = imagen.getContentType();
                        if (contentType == null || !contentType.startsWith("image/")) {
                            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tipo de archivo no permitido para imagen: " + contentType);
                        }
                        // Validar tamaño (ej. máximo 10MB por imagen)
                        long maxSize = 10 * 1024 * 1024; // 10MB
                        if (imagen.getSize() > maxSize) {
                            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La imagen es demasiado grande. Máximo permitido: 10MB.");
                        }

                        // Generar nombre único con prefijo
                        String originalFilename = imagen.getOriginalFilename();
                        String extension = originalFilename != null && originalFilename.contains(".") ?
                                originalFilename.substring(originalFilename.lastIndexOf(".")) : ".jpg";
                        String objectName = "isla-" + isla.getId() + "/imagen/" + UUID.randomUUID().toString() + extension;

                        // Subir a MinIO
                        minioService.uploadFile(bucketName, objectName, imagen.getInputStream(), imagen.getSize(), contentType);

                        // Generar URL presigned (válida por 7 días)
                        String presignedUrl = minioService.getPresignedUrl(bucketName, objectName, 604800);

                        // Agregar URL a la isla
                        isla.getImagenes().add(presignedUrl);
                    }
                }
            }

            // Procesar videos
            if (islaDtoCreate.getVideosArchivos() != null && !islaDtoCreate.getVideosArchivos().isEmpty()) {
                for (MultipartFile video : islaDtoCreate.getVideosArchivos()) {
                    if (video != null && !video.isEmpty()) {
                        // Validar tipo de archivo
                        String contentType = video.getContentType();
                        if (contentType == null || !contentType.startsWith("video/")) {
                            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tipo de archivo no permitido para video: " + contentType);
                        }
                        // Validar tamaño (ej. máximo 50MB por video)
                        long maxSize = 50 * 1024 * 1024; // 50MB
                        if (video.getSize() > maxSize) {
                            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El video es demasiado grande. Máximo permitido: 50MB.");
                        }

                        // Generar nombre único con prefijo
                        String originalFilename = video.getOriginalFilename();
                        String extension = originalFilename != null && originalFilename.contains(".") ?
                                originalFilename.substring(originalFilename.lastIndexOf(".")) : ".mp4";
                        String objectName = "isla-" + isla.getId() + "/video/" + UUID.randomUUID().toString() + extension;

                        // Subir a MinIO
                        minioService.uploadFile(bucketName, objectName, video.getInputStream(), video.getSize(), contentType);

                        // Generar URL presigned (válida por 7 días)
                        String presignedUrl = minioService.getPresignedUrl(bucketName, objectName, 604800);

                        // Agregar URL a la isla
                        isla.getVideos().add(presignedUrl);
                    }
                }
            }

            // Procesar archivo de descarga
            if (islaDtoCreate.getArchivoDescarga() != null && !islaDtoCreate.getArchivoDescarga().isEmpty()) {
                MultipartFile archivo = islaDtoCreate.getArchivoDescarga();
                // Validar tipo de archivo (permitir varios tipos comunes)
                String contentType = archivo.getContentType();
                if (contentType == null || (!contentType.startsWith("application/") && !contentType.equals("text/plain"))) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tipo de archivo no permitido para descarga: " + contentType);
                }
                // Validar tamaño (ej. máximo 100MB)
                long maxSize = 100 * 1024 * 1024; // 100MB
                if (archivo.getSize() > maxSize) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El archivo de descarga es demasiado grande. Máximo permitido: 100MB.");
                }

                // Generar nombre único con prefijo
                String originalFilename = archivo.getOriginalFilename();
                String extension = originalFilename != null && originalFilename.contains(".") ?
                        originalFilename.substring(originalFilename.lastIndexOf(".")) : "";
                String objectName = "isla-" + isla.getId() + "/descarga/" + UUID.randomUUID().toString() + extension;

                // Subir a MinIO
                minioService.uploadFile(bucketName, objectName, archivo.getInputStream(), archivo.getSize(), contentType);

                // Generar URL presigned (válida por 7 días)
                String presignedUrl = minioService.getPresignedUrl(bucketName, objectName, 604800);

                // Establecer link de descarga
                isla.setLinkDescarga(presignedUrl);
            }

            // Actualizar la isla en la base de datos con las URLs generadas
            islaRepository.save(isla);

            logger.info("Isla creada exitosamente con ID {} y archivos multimedia subidos", isla.getId());

            // Retornar IslaDtoResponse con las URLs incluidas
            return ResponseEntity.ok(gestionIsla.obtenerIslaPorId(isla.getId()).orElseThrow(() ->
                    new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error al obtener la isla creada")));

        } catch (ResponseStatusException e) {
            logger.error("Error de validación al crear isla: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Error interno al crear isla: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error interno del servidor", e);
        }
    }

    @PostMapping("/{id}/upload")
    public ResponseEntity<String> uploadFile(@PathVariable Long id, @RequestParam("file") MultipartFile file) {
        try {
            // Validar que la isla existe
            Isla isla = islaRepository.findById(id)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Isla no encontrada"));

            // Validar tipo de archivo
            String contentType = file.getContentType();
            if (contentType == null || (!contentType.startsWith("image/") && !contentType.startsWith("video/") && !contentType.equals("application/octet-stream"))) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tipo de archivo no permitido. Solo se permiten imágenes, videos o archivos de descarga.");
            }

            // Validar tamaño del archivo (ej. máximo 50MB)
            long maxSize = 50 * 1024 * 1024; // 50MB
            if (file.getSize() > maxSize) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El archivo es demasiado grande. Máximo permitido: 50MB.");
            }

            // Usar bucket único "isla"
            String bucketName = "isla";
            minioService.createBucket(bucketName);

            // Generar nombre único para el archivo con prefijo
            String originalFilename = file.getOriginalFilename();
            String extension = originalFilename != null && originalFilename.contains(".") ?
                    originalFilename.substring(originalFilename.lastIndexOf(".")) : "";
            String objectName = "isla-" + id + "/archivo/" + UUID.randomUUID().toString() + extension;

            // Subir archivo a MinIO
            minioService.uploadFile(bucketName, objectName, file.getInputStream(), file.getSize(), contentType);

            // Generar URL presigned (válida por 1 hora)
            String presignedUrl = minioService.getPresignedUrl(bucketName, objectName, 3600);

            // Actualizar la entidad Isla con la URL
            if (contentType.startsWith("image/")) {
                isla.getImagenes().add(presignedUrl);
            } else if (contentType.startsWith("video/")) {
                isla.getVideos().add(presignedUrl);
            } else {
                isla.setLinkDescarga(presignedUrl);
            }
            islaRepository.save(isla);

            logger.info("Archivo subido exitosamente para isla {}: {}", id, objectName);

            return ResponseEntity.ok("Archivo subido exitosamente. URL: " + presignedUrl);

        } catch (ResponseStatusException e) {
            logger.error("Error de validación al subir archivo para isla {}: {}", id, e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Error interno al subir archivo para isla {}: {}", id, e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error interno del servidor", e);
        }
    }


}
