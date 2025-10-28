package com.udlaverso.metaudla.mappers;

import com.udlaverso.metaudla.DTOs.CategoriaDto;
import com.udlaverso.metaudla.entities.Categoria;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface CategoriaMapper {
    Categoria toEntity(CategoriaDto categoriaDto);

    CategoriaDto toDto(Categoria categoria);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Categoria partialUpdate(CategoriaDto categoriaDto, @MappingTarget Categoria categoria);
}