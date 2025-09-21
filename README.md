<<<<<<< HEAD
# Metaudla - Backend del Metaverso Educativo

## Descripción

Metaudla es una aplicación backend desarrollada con Spring Boot que proporciona una plataforma para un metaverso educativo. Permite a los usuarios crear, compartir y gestionar "islas" virtuales (mundos o recursos educativos) con funcionalidades sociales como comentarios, likes, favoritos y puntuaciones. El sistema incluye gestión de usuarios con roles y estados, así como estadísticas detalladas de interacción.

## Tecnologías Utilizadas

- **Java 21**: Lenguaje de programación principal
- **Spring Boot 3.5.6**: Framework para desarrollo de aplicaciones web
- **Spring Data JPA**: Para persistencia de datos
- **Spring Security**: Para autenticación y autorización
- **Spring Validation**: Para validación de datos
- **MySQL**: Base de datos relacional
- **Lombok**: Para reducción de código boilerplate
- **Maven**: Gestión de dependencias y construcción

## Arquitectura

El proyecto sigue una arquitectura en capas típica de Spring Boot:

- **Controladores (Controllers)**: Manejan las peticiones HTTP y respuestas
- **Servicios (Services)**: Contienen la lógica de negocio
- **Repositorios (Repositories)**: Manejan el acceso a datos
- **Modelos (Models)**: Representan las entidades del dominio
- **Configuraciones (Config)**: Configuraciones de Spring y seguridad

## Estructura del Proyecto

```
src/main/java/com/udlaverso/metaudla/
├── MetaudlaApplication.java          # Clase principal
├── config/                           # Configuraciones
│   ├── SecurityConfig.java
│   └── ServiceConfig.java
├── controllers/                      # Controladores REST
│   ├── UsuarioController.java
│   └── IslaController.java
├── models/                           # Modelos de datos
│   ├── Usuario.java
│   ├── Isla.java
│   ├── Comentario.java
│   ├── Favorito.java
│   ├── MeGusta.java
│   ├── Puntuacion.java
│   └── ...
├── repositories/                     # Repositorios JPA
│   ├── UsuarioRepository.java
│   ├── IslaRepository.java
│   └── ...
├── servicies/                        # Servicios de negocio
│   ├── IUsuarioService.java
│   ├── IIslaService.java
│   ├── implementaciones/
│   │   ├── UsuarioService.java
│   │   └── IslaService.java
│   └── ...
└── enums/                            # Enumeraciones
    ├── EstadoBasico.java
    ├── EstadoModeracion.java
    ├── Rol.java
    └── TipoLike.java
```

## Modelos Principales

### Usuario
- Gestión de usuarios con roles (Estudiante, Administrador, etc.)
- Autenticación y autorización
- Estados de habilitación/deshabilitación
- Auditoría de creación y actualización

### Isla
- Representa mundos virtuales o recursos educativos
- Contiene imágenes, videos, enlaces de descarga
- Sistema de categorías y etiquetas
- Estadísticas de visitas e interacciones
- Puntuaciones promedio calculadas dinámicamente

### Interacciones
- **Comentarios**: Sistema jerárquico con respuestas
- **Me Gusta/No Me Gusta**: Likes positivos y negativos
- **Favoritos**: Marcado de islas favoritas
- **Puntuaciones**: Sistema de calificación numérica

## API Endpoints

### Usuarios (`/api/usuarios`)

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| POST | `/` | Crear nuevo usuario |
| GET | `/{id}` | Obtener usuario por ID |
| GET | `/` | Listar todos los usuarios |
| PUT | `/{id}` | Actualizar usuario |
| DELETE | `/{id}` | Eliminar usuario |
| POST | `/{id}/deshabilitar` | Deshabilitar usuario |
| POST | `/login` | Autenticación de usuario |
| GET | `/stats/activos` | Contar usuarios activos |

### Islas (`/api/islas`)

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| POST | `/` | Crear nueva isla |
| GET | `/{id}` | Obtener isla por ID |
| GET | `/{id}/stats` | Obtener isla con estadísticas |
| GET | `/` | Listar islas con paginación |
| GET | `/buscar` | Buscar islas por nombre |
| GET | `/populares` | Obtener islas más populares |
| GET | `/mejor-puntuadas` | Obtener islas mejor puntuadas |
| GET | `/recientes` | Obtener islas más recientes |
| PUT | `/{id}` | Actualizar isla |
| POST | `/{id}/visita` | Incrementar contador de visitas |
| POST | `/{islaId}/me-gusta` | Agregar me gusta/no me gusta |
| POST | `/{islaId}/comentario` | Agregar comentario |
| POST | `/{islaId}/favorito` | Agregar a favoritos |
| POST | `/{islaId}/puntuacion` | Agregar puntuación |
| GET | `/stats/globales` | Estadísticas globales |

## Configuración

### Base de Datos
Copia `src/main/resources/application.properties.example` a `application.properties` y configura:

```properties
spring.application.name=metaudla

# Configuración de la base de datos MySQL
spring.datasource.url=jdbc:mysql://localhost:3306/metaudla_db?useSSL=false&serverTimezone=UTC
spring.datasource.username=tu-usuario
spring.datasource.password=tu-contraseña
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# Configuración de JPA/Hibernate
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect
```

### Seguridad
La aplicación incluye configuración básica de Spring Security. Para producción, se recomienda:
- Implementar JWT para autenticación stateless
- Configurar CORS apropiadamente
- Usar HTTPS
- Implementar rate limiting

## Instalación y Ejecución

### Prerrequisitos
- Java 21 o superior
- Maven 3.6+
- MySQL 8.0+

### Pasos de Instalación

1. **Clonar el repositorio**
   ```bash
   git clone <url-del-repositorio>
   cd metaudla
   ```

2. **Configurar la base de datos**
   - Crear una base de datos MySQL llamada `metaudla_db`
   - Configurar las credenciales en `application.properties`

3. **Construir el proyecto**
   ```bash
   mvn clean install
   ```

4. **Ejecutar la aplicación**
   ```bash
   mvn spring-boot:run
   ```

   O usando el wrapper de Maven:
   ```bash
   ./mvnw spring-boot:run
   ```

La aplicación estará disponible en `http://localhost:8080`

## Características Principales

- **Gestión de Usuarios**: Registro, autenticación, roles y permisos
- **Sistema de Islas**: Creación y gestión de mundos virtuales
- **Interacciones Sociales**: Comentarios, likes, favoritos y puntuaciones
- **Estadísticas**: Métricas detalladas de uso e interacción
- **Paginación**: Soporte para grandes volúmenes de datos
- **Validación**: Validaciones robustas en todos los endpoints
- **Auditoría**: Seguimiento de creación y modificación de entidades
- **Arquitectura Escalable**: Diseño modular y extensible

## Desarrollo

### Convenciones de Código
- **Nombres de Clases**: PascalCase (ej: `UsuarioController`)
- **Nombres de Métodos/Variables**: camelCase (ej: `buscarPorId`)
- **Constantes**: ALL_CAPS (ej: `MAX_RETRY_ATTEMPTS`)
- **Paquetes**: lowercase (ej: `com.udlaverso.metaudla`)

### Testing
El proyecto incluye configuración para JUnit 5 y Spring Boot Test. Para ejecutar los tests:

```bash
mvn test
```

### Documentación API
Se recomienda usar Springdoc OpenAPI para documentación automática de la API. Agregar al `pom.xml`:

```xml
<dependency>
    <groupId>org.springdoc</groupId>
    <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
    <version>2.2.0</version>
</dependency>
```

La documentación estará disponible en `http://localhost:8080/swagger-ui.html`

## Contribución

1. Fork el proyecto
2. Crear una rama para tu feature (`git checkout -b feature/nueva-funcionalidad`)
3. Commit tus cambios (`git commit -am 'Agrega nueva funcionalidad'`)
4. Push a la rama (`git push origin feature/nueva-funcionalidad`)
5. Crear un Pull Request

## Licencia

Este proyecto está bajo la Licencia MIT. Ver el archivo `LICENSE` para más detalles.

## Contacto

Para preguntas o soporte, contactar al Conmigo en wpp.
