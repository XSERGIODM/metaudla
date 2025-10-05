package com.udlaverso.metaudla.controllers;

import com.udlaverso.metaudla.DTO.UsuarioDTO;
import com.udlaverso.metaudla.servicies.gestion_usuarios.IGestionUsuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/prueba")
@CrossOrigin("*")
public class PruebaController {

    @Autowired
    IGestionUsuario gestionUsuario;


    @GetMapping("/listar")
    public ResponseEntity<List<UsuarioDTO>> listar() {
        List<UsuarioDTO> usuarioDTOS= gestionUsuario.obtenerTodosLosUsuarios();
        return ResponseEntity.ok(usuarioDTOS);
    }
}
