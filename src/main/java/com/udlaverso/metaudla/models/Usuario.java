package com.udlaverso.metaudla.models;
import com.udlaverso.metaudla.enums.EstadoBasico;
import com.udlaverso.metaudla.enums.Rol;

import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "usuario")
public class Usuario {

    // Atributos de la clase
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Column(nullable = false)
    String nombre;
    @Column(nullable = false, unique = true)
    String username;
    @Column(nullable = false, unique = true)
    String correo;
    @Column(nullable = false)
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$",
             message = "La contraseña debe tener al menos 8 caracteres, una mayúscula, una minúscula, un número y un carácter especial")
    String contrasena;
    @Column
    String fotoPerfilUrl;

    // Auditoría
    @Column(nullable = false)
    LocalDateTime createdAt;

    @Column
    LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "created_by")
    Usuario createdBy;

    @ManyToOne
    @JoinColumn(name = "updated_by")
    Usuario updatedBy;

    //Enums
    @Enumerated(EnumType.STRING)
    EstadoBasico estado = EstadoBasico.HABILITADO;
    @Enumerated(EnumType.STRING)
    Rol rol = Rol.ESTUDIANTE;

    //relaciones
    @OneToMany(mappedBy = "usuario")
    List<Favorito> favoritos = new ArrayList<>();
    @OneToMany(mappedBy = "usuario")
    List<Puntuacion> puntuaciones = new ArrayList<>();
    @OneToMany(mappedBy = "usuario")
    List<MeGusta> meGustas = new ArrayList<>();
    @OneToMany(mappedBy = "usuario")
    List<Comentario> comentarios = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

}