package com.example.library.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class AuthenticationRequestDto {

    @NotEmpty
    @Pattern(regexp = ".+@.+\\..+")
    private String email;

    @NotEmpty
    @Size(min = 6)
    private String password;
}
