package com.udlaverso.metaudla.servicies.gestion_categorias;

import com.udlaverso.metaudla.DTOs.CategoriaDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface GestionCategoria {
    Page<CategoriaDto> obtenerTodasCategorias(Pageable pageable);
    CategoriaDto obtenerCategoria(Long id);
    CategoriaDto crearCategoria(CategoriaDto categoriaDto);
    CategoriaDto actualizarCategoria(CategoriaDto categoriaDto);
    void deshabilitarCategoria(Long id);
    void habilitarCategoria(Long id);
}
