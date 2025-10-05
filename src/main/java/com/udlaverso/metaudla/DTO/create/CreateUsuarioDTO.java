package com.udlaverso.metaudla.DTO.create;

import com.udlaverso.metaudla.enums.Rol;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
public class CreateUsuarioDTO {

    @NotBlank(message = "El nombre es obligatorio")
    String nombre;

    @NotBlank(message = "El username es obligatorio")
    String username;

    @NotBlank(message = "El correo es obligatorio")
    String correo;

    @NotBlank(message = "La contraseña es obligatoria")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$",
             message = "La contraseña debe tener al menos 8 caracteres, una mayúscula, una minúscula, un número y un carácter especial")
    String contrasena;
}