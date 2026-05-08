package com.example.demo.users;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class usersDTO {

    @NotBlank
    @Pattern(regexp = ".*[A-Z].*", message = "El nombre debe tener al menos una letra mayuscula")
    private String firstName;

    @NotBlank
    @Pattern(regexp = ".*[A-Z].*", message = "El apellido debe tener al menos una letra mayuscula")
    private String lastName;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*\\d).{8,}$", message = "La contrasena debe tener minimo 8 caracteres, una mayuscula y un numero")
    private String password;
}
