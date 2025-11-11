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

    @PostMapping
    public ResponseEntity<IslaDtoResponse> crearIsla(@RequestBody IslaDtoCreate islaDtoCreate) {
        return ResponseEntity.ok(gestionIsla.crearIsla(islaDtoCreate));
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

            // Crear bucket dedicado para la isla
            String bucketName = "isla-" + id;
            minioService.createBucket(bucketName);

            // Generar nombre único para el archivo
            String originalFilename = file.getOriginalFilename();
            String extension = originalFilename != null && originalFilename.contains(".") ?
                    originalFilename.substring(originalFilename.lastIndexOf(".")) : "";
            String objectName = UUID.randomUUID().toString() + extension;

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
