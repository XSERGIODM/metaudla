package com.udlaverso.metaudla.config;

import org.springframework.context.annotation.Configuration;

@Configuration
public class ServiceConfig {

    // Este archivo muestra cómo configurar la inyección de dependencias
    // usando interfaces. En Spring Boot, esto es automático con @Service

    /*
    Si necesitas múltiples implementaciones del mismo servicio:

    @Bean
    @Primary
    public IUsuarioService usuarioService() {
        return new UsuarioService();
    }

    @Bean("usuarioServiceMock")
    public IUsuarioService usuarioServiceMock() {
        return new UsuarioServiceMock();
    }

    @Bean
    public IIslaService islaService(IUsuarioService usuarioService) {
        return new IslaService(usuarioService);
    }
    */

    // En este caso, Spring Boot detecta automáticamente las implementaciones
    // gracias a @Service y las inyecta donde se necesiten las interfaces
}