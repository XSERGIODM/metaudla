package com.udlaverso.metaudla.config;

import com.udlaverso.metaudla.entities.Auditoria;
import com.udlaverso.metaudla.entities.Usuario;
import com.udlaverso.metaudla.repositories.AuditoriaRepository;
import com.udlaverso.metaudla.repositories.UsuarioRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.LocalDateTime;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class AuditoriaAspect {

    private final AuditoriaRepository auditoriaRepository;
    private final UsuarioRepository usuarioRepository;

    @Pointcut("within(@org.springframework.web.bind.annotation.RestController *)")
    public void restControllerMethods() {}

    @Around("restControllerMethods()")
    public Object auditarLlamada(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();

        ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attrs.getRequest();
        HttpServletResponse response = attrs.getResponse();

        String endpoint = request.getRequestURI();
        String metodoHttp = request.getMethod();
        String ipCliente = getClientIpAddress(request);
        String userAgent = request.getHeader("User-Agent");

        // Obtener usuario del contexto de seguridad
        Usuario usuario = getUsuarioActual();

        Object result = null;
        boolean exito = true;
        String mensajeError = null;
        Integer codigoRespuesta = null;

        try {
            result = joinPoint.proceed();

            if (response != null) {
                codigoRespuesta = response.getStatus();
            }
        } catch (Throwable throwable) {
            exito = false;
            mensajeError = throwable.getMessage();
            if (response != null) {
                codigoRespuesta = response.getStatus();
            }
            throw throwable;
        } finally {
            long endTime = System.currentTimeMillis();
            long tiempoRespuesta = endTime - startTime;

            // Crear registro de auditoría
            Auditoria auditoria = new Auditoria();
            auditoria.setUsuario(usuario);
            auditoria.setEndpoint(endpoint);
            auditoria.setMetodoHttp(metodoHttp);
            auditoria.setTimestamp(LocalDateTime.now());
            auditoria.setIpCliente(ipCliente);
            auditoria.setCodigoRespuesta(codigoRespuesta);
            auditoria.setTiempoRespuesta(tiempoRespuesta);
            auditoria.setUserAgent(userAgent);
            auditoria.setParametrosConsulta(request.getQueryString());
            auditoria.setCuerpoSolicitud(getRequestBody(request));
            auditoria.setExito(exito);
            auditoria.setMensajeError(mensajeError);

            try {
                auditoriaRepository.save(auditoria);
                log.debug("Auditoría registrada para endpoint: {} {}", metodoHttp, endpoint);
            } catch (Exception e) {
                log.error("Error al guardar registro de auditoría: {}", e.getMessage());
            }
        }

        return result;
    }

    @AfterThrowing(pointcut = "restControllerMethods()", throwing = "ex")
    public void auditarExcepcion(Exception ex) {
        // Este método se ejecuta adicionalmente si hay una excepción
        // pero la lógica principal ya maneja excepciones en @Around
        log.warn("Excepción capturada en controlador REST: {}", ex.getMessage());
    }

    private Usuario getUsuarioActual() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null && authentication.isAuthenticated() && !(authentication.getPrincipal() instanceof String)) {
                // Si el principal es un UserDetails, extraer el username
                if (authentication.getPrincipal() instanceof org.springframework.security.core.userdetails.User userDetails) {
                    String username = userDetails.getUsername();
                    return usuarioRepository.findByUsernameOrCorreo(username, username);
                }
                // Si ya es un Usuario, devolverlo directamente
                else if (authentication.getPrincipal() instanceof Usuario) {
                    return (Usuario) authentication.getPrincipal();
                }
            }
        } catch (Exception e) {
            log.warn("Error al obtener usuario del contexto de seguridad: {}", e.getMessage());
        }
        return null; // Usuario anónimo
    }

    private String getClientIpAddress(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
            return xForwardedFor.split(",")[0].trim();
        }

        String xRealIp = request.getHeader("X-Real-IP");
        if (xRealIp != null && !xRealIp.isEmpty()) {
            return xRealIp;
        }

        return request.getRemoteAddr();
    }

    private String getRequestBody(HttpServletRequest request) {
        // Para capturar el cuerpo de la solicitud, necesitaríamos un wrapper
        // Por simplicidad, devolveremos null o una cadena vacía
        // En una implementación completa, se podría usar ContentCachingRequestWrapper
        return null;
    }
}