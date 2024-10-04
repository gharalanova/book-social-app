package com.projectgaby.book_social_app.auth;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Builder
public class RegistrationRequest {

    @NotBlank(message = "Please enter your first name.")
    private String firstname;

    @NotBlank(message = "Please enter your last name.")
    private String lastname;

    @Email(message = "Please enter a valid email address.")
    @NotBlank(message = "Please enter your email.")
    private String email;

    @Size(min = 6, message = "Password must contains at least 6 characters.")
    @NotBlank(message = "Please enter your password")
    private String password;


}
