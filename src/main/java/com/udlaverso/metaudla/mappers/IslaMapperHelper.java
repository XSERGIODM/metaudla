package com.udlaverso.metaudla.mappers;

import com.udlaverso.metaudla.entities.Categoria;
import com.udlaverso.metaudla.repositories.CategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class IslaMapperHelper {

    @Autowired
    private CategoriaRepository categoriaRepository;

    public List<Long> categoriasToCategoriaIds(List<Categoria> categorias) {
        if (categorias == null) {
            return new ArrayList<>();
        }
        return categorias.stream().map(Categoria::getId).collect(Collectors.toList());
    }

    public List<Categoria> categoriaIdsToCategorias(List<Long> categoriaIds) {
        if (categoriaIds == null || categoriaIds.isEmpty()) {
            return new ArrayList<>();
        }
        return categoriaRepository.findAllById(categoriaIds);
    }
}