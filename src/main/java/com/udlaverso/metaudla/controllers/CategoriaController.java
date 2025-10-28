package com.udlaverso.metaudla.controllers;

import com.udlaverso.metaudla.DTOs.CategoriaDto;
import com.udlaverso.metaudla.servicies.gestion_categorias.GestionCategoria;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/categorias")
@CrossOrigin("*")
@RequiredArgsConstructor
public class CategoriaController {
    private final GestionCategoria gestionCategoria;

    @GetMapping()
    public ResponseEntity<Page<CategoriaDto>> listarCategorias(
            @RequestParam(required = false,defaultValue = "0") int pagina,
            @RequestParam(required = false,defaultValue = "10") int tamanio
    ) {
        Pageable pageable = PageRequest.of(pagina, tamanio);
        return ResponseEntity.ok(gestionCategoria.obtenerTodasCategorias(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoriaDto> obtenerCategoria(@PathVariable Long id) {
        return ResponseEntity.ok(gestionCategoria.obtenerCategoria(id));
    }

    @PostMapping
    public ResponseEntity<CategoriaDto> crearCategoria(@RequestBody CategoriaDto categoriaDto) {
        return ResponseEntity.ok(gestionCategoria.crearCategoria(categoriaDto));
    }

    @PutMapping
    public ResponseEntity<CategoriaDto> actualizarCategoria(@RequestBody CategoriaDto categoriaDto) {
        return ResponseEntity.ok(gestionCategoria.actualizarCategoria(categoriaDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarCategoria(@PathVariable Long id) {
        gestionCategoria.deshabilitarCategoria(id);
        return ResponseEntity.ok().build();
    }
    @PutMapping("/{id}")
    public ResponseEntity<CategoriaDto> habilitarCategoria(@PathVariable Long id) {
        gestionCategoria.habilitarCategoria(id);
        return ResponseEntity.ok().build();
    }
}
