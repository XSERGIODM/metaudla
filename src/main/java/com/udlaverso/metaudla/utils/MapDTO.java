package com.udlaverso.metaudla.utils;

import com.udlaverso.metaudla.DTO.UsuarioDTO;
import com.udlaverso.metaudla.models.Usuario;

import java.util.ArrayList;
import java.util.List;

public class MapDTO {
    public static UsuarioDTO mapModelToDTO(Usuario usuario) {
        UsuarioDTO usuarioDTO = new UsuarioDTO();
        usuarioDTO.setId(usuario.getId());
        usuarioDTO.setNombre(usuario.getNombre());
        usuarioDTO.setUsername(usuario.getUsername());
        usuarioDTO.setCorreo(usuario.getCorreo());
        usuarioDTO.setFotoPerfilUrl(usuario.getFotoPerfilUrl());
        usuarioDTO.setCreatedAt(usuario.getCreatedAt());
        usuarioDTO.setUpdatedAt(usuario.getUpdatedAt());
        usuarioDTO.setRol(usuario.getRol());
        usuarioDTO.setEstado(usuario.getEstado());
        //usuarioDTO.setCreatedById(usuario.getCreatedBy().getId());
        //usuarioDTO.setUpdatedById(usuario.getUpdatedBy().getId());
        //usuarioDTO.setCreatedByNombre(usuario.getCreatedBy().getNombre());
        //usuarioDTO.setUpdatedByNombre(usuario.getUpdatedBy().getNombre());
        return usuarioDTO;
    }

    public static List<UsuarioDTO> listMapModelToDTO(List<Usuario> usuarios) {
        List<UsuarioDTO> usuarioDTOS = new ArrayList<>();
        for(Usuario usuario: usuarios) {
            usuarioDTOS.add(mapModelToDTO(usuario));
        }
        return usuarioDTOS;
    }
}
