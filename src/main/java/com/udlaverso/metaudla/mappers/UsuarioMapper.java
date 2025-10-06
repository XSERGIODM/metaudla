package com.udlaverso.metaudla.mappers;

import com.udlaverso.metaudla.DTOs.usuario.UsuarioDtoCreate;
import com.udlaverso.metaudla.DTOs.usuario.UsuarioResponseDto;
import com.udlaverso.metaudla.entities.Usuario;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface UsuarioMapper {
    @Mapping(source = "updatedByNombre", target = "updatedBy.nombre")
    @Mapping(source = "updatedById", target = "updatedBy.id")
    @Mapping(source = "createdByRol", target = "createdBy.rol")
    @Mapping(source = "createdByEstado", target = "createdBy.estado")
    @Mapping(source = "createdByNombre", target = "createdBy.nombre")
    @Mapping(source = "createdById", target = "createdBy.id")
    Usuario toEntity(UsuarioResponseDto usuarioResponseDto);

    @AfterMapping
    default void linkFavoritos(@MappingTarget Usuario usuario) {
        usuario.getFavoritos().forEach(favorito -> favorito.setUsuario(usuario));
    }

    @AfterMapping
    default void linkPuntuaciones(@MappingTarget Usuario usuario) {
        usuario.getPuntuaciones().forEach(puntuacione -> puntuacione.setUsuario(usuario));
    }

    @AfterMapping
    default void linkMeGustas(@MappingTarget Usuario usuario) {
        usuario.getMeGustas().forEach(meGusta -> meGusta.setUsuario(usuario));
    }

    @AfterMapping
    default void linkIslas(@MappingTarget Usuario usuario) {
        usuario.getIslas().forEach(isla -> isla.setAutor(usuario));
    }

    @InheritInverseConfiguration(name = "toEntity")
    UsuarioResponseDto toDto(Usuario usuario);

    @InheritConfiguration(name = "toEntity")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Usuario partialUpdate(UsuarioResponseDto usuarioResponseDto, @MappingTarget Usuario usuario);

    Usuario toEntity(UsuarioDtoCreate usuarioDtoCreate);

    UsuarioDtoCreate toDto1(Usuario usuario);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Usuario partialUpdate(UsuarioDtoCreate usuarioDtoCreate, @MappingTarget Usuario usuario);
}