package com.udlaverso.metaudla.servicies.gestion_categorias;

import com.udlaverso.metaudla.DTOs.CategoriaDto;
import com.udlaverso.metaudla.entities.Categoria;
import com.udlaverso.metaudla.enums.EstadoBasico;
import com.udlaverso.metaudla.mappers.CategoriaMapper;
import com.udlaverso.metaudla.repositories.CategoriaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GestionCategoriaImpl implements GestionCategoria{

    private final CategoriaRepository categoriaRepository;
    private final CategoriaMapper categoriaMapper;

    @Override
    public Page<CategoriaDto> obtenerTodasCategorias(Pageable pageable) {
        Page<Categoria> categorias = categoriaRepository.findAll(pageable);
        return categorias.map(categoriaMapper::toDto);
    }

    @Override
    public CategoriaDto obtenerCategoria(Long id) {
        Optional<Categoria> categoria = categoriaRepository.findById(id);
        return categoria.map(categoriaMapper::toDto).orElse(null);
    }

    @Override
    public CategoriaDto crearCategoria(CategoriaDto categoriaDto) {
        Categoria categoria = new Categoria();
        categoria.setNombre(categoriaDto.getNombre());
        categoria.setDescripcion(categoriaDto.getDescripcion());
        categoria.setEstado(EstadoBasico.HABILITADO);
        categoriaRepository.save(categoria);
        return categoriaMapper.toDto(categoria);
    }

    @Override
    public CategoriaDto actualizarCategoria(CategoriaDto categoriaDto) {
        if (categoriaDto.getId() == null || categoriaDto.getId() <= 0) {
            throw new IllegalArgumentException("El id de la categoria no puede ser nulo");
        }
        Categoria categoria = categoriaRepository.findById(categoriaDto.getId()).orElse(null);
        assert categoria != null;
        categoria.setNombre(categoriaDto.getNombre());
        categoria.setDescripcion(categoriaDto.getDescripcion());
        categoriaRepository.save(categoria);
        return categoriaMapper.toDto(categoria);
    }

    @Override
    public void deshabilitarCategoria(Long id) {
        Categoria categoria = categoriaRepository.findById(id).orElse(null);
        if (categoria == null) {
            throw new IllegalArgumentException("La categoria no existe");
        }
        categoria.setEstado(EstadoBasico.DESHABILITADO);
        categoriaRepository.save(categoria);
    }

    @Override
    public void habilitarCategoria(Long id) {
        Categoria categoria = categoriaRepository.findById(id).orElse(null);
        if (categoria == null) {
            throw new IllegalArgumentException("La categoria no existe");
        }
        categoria.setEstado(EstadoBasico.HABILITADO);
        categoriaRepository.save(categoria);
    }
}
