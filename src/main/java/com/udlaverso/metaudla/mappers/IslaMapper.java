package com.udlaverso.metaudla.mappers;

import com.udlaverso.metaudla.DTOs.isla.IslaDtoCreate;
import com.udlaverso.metaudla.DTOs.isla.IslaDtoResponse;
import com.udlaverso.metaudla.entities.*;
import com.udlaverso.metaudla.enums.TipoLike;
import com.udlaverso.metaudla.repositories.CategoriaRepository;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING, uses = IslaMapperHelper.class)
public interface IslaMapper {

    @Mapping(source = "autorRol", target = "autor.rol")
    @Mapping(source = "autorFotoPerfilUrl", target = "autor.fotoPerfilUrl")
    @Mapping(source = "autorCorreo", target = "autor.correo")
    @Mapping(source = "autorNombre", target = "autor.nombre")
    @Mapping(source = "autorId", target = "autor.id")
    Isla toEntity(IslaDtoResponse islaDtoResponse);

    @InheritInverseConfiguration(name = "toEntity")
    @Mapping(target = "favoritoIds", expression = "java(favoritosToFavoritoIds(isla.getFavoritos()))")
    @Mapping(target = "puntuacioneCalificacions", expression = "java(puntuacionesToPuntuacioneCalificacions(isla.getPuntuaciones()))")
    @Mapping(target = "meGustaTipos", expression = "java(meGustasToMeGustaTipos(isla.getMeGustas()))")
    IslaDtoResponse toDto(Isla isla);

    @InheritConfiguration(name = "toEntity")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Isla partialUpdate(IslaDtoResponse islaDtoResponse, @MappingTarget Isla isla);

    default List<Long> favoritosToFavoritoIds(List<Favorito> favoritos) {
        return favoritos.stream().map(Favorito::getId).collect(Collectors.toList());
    }

    default List<Integer> puntuacionesToPuntuacioneCalificacions(List<Puntuacion> puntuaciones) {
        return puntuaciones.stream().map(Puntuacion::getCalificacion).collect(Collectors.toList());
    }

    default List<TipoLike> meGustasToMeGustaTipos(List<MeGusta> meGustas) {
        return meGustas.stream().map(MeGusta::getTipo).collect(Collectors.toList());
    }

    @Mapping(source = "autorId", target = "autor.id")
    Isla toEntity(IslaDtoCreate islaDtoCreate);

    @Mapping(source = "autor.id", target = "autorId")
    IslaDtoCreate toDto1(Isla isla);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "autorId", target = "autor.id")
    Isla partialUpdate(IslaDtoCreate islaDtoCreate, @MappingTarget Isla isla);
}