package com.udlaverso.metaudla.servicies.gestion_islas;

import com.udlaverso.metaudla.DTOs.isla.IslaDtoCreate;
import com.udlaverso.metaudla.DTOs.isla.IslaDtoResponse;
import com.udlaverso.metaudla.entities.Isla;
import com.udlaverso.metaudla.mappers.IslaMapper;
import com.udlaverso.metaudla.repositories.IslaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GestionIslaImpl implements IGestionIsla{


    private final IslaRepository islaRepository;
    private final IslaMapper islaMapper;

    @Override
    public IslaDtoResponse crearIsla(IslaDtoCreate islaDto) {
        return null;
    }

    @Override
    public Optional<IslaDtoResponse> obtenerIslaPorId(Long id) {
        return Optional.empty();
    }

    @Override
    public List<IslaDtoResponse> obtenerTodasIslas() {
        List<IslaDtoResponse> islaDtoResponses = new ArrayList<>();
        List<Isla> islas = islaRepository.findAll();
        if (!islas.isEmpty()) {
            for (Isla isla : islas) {
                islaDtoResponses.add(islaMapper.toDto(isla));
            }
            return islaDtoResponses;
        }
        return List.of();
    }

    @Override
    public Page<IslaDtoResponse> obtenerIslasPaginadas(Pageable pageable) {
        Page<Isla> islasPage = islaRepository.findAll(pageable);
        return islasPage.map(islaMapper::toDto);
    }

    @Override
    public List<IslaDtoResponse> buscarIslasPorNombre(String nombre) {
        return List.of();
    }

    @Override
    public List<IslaDtoResponse> buscarIslasPorEtiqueta(String etiqueta) {
        return List.of();
    }

    @Override
    public List<IslaDtoResponse> obtenerIslasPorCategoria(Long categoriaId) {
        return List.of();
    }

    @Override
    public List<IslaDtoResponse> obtenerIslasPorAutor(Long autorId) {
        return List.of();
    }

    @Override
    public IslaDtoResponse actualizarIsla(Long id, IslaDtoCreate islaDto) {
        return null;
    }

    @Override
    public boolean eliminarIsla(Long id) {
        return false;
    }

    @Override
    public void incrementarVisitas(Long id) {

    }

    @Override
    public boolean existeIsla(Long id) {
        return false;
    }
}
