package com.udlaverso.metaudla.controllers;

import com.udlaverso.metaudla.DTOs.isla.IslaDtoCreate;
import com.udlaverso.metaudla.DTOs.isla.IslaDtoResponse;
import com.udlaverso.metaudla.servicies.gestion_islas.IGestionIsla;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/isla")
@CrossOrigin("*")
@RequiredArgsConstructor
public class IslaController {
    private final IGestionIsla gestionIsla;


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


}
