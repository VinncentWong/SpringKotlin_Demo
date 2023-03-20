package com.example.example.dto.user

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

data class LoginUserDto(
    @NotNull(message = "email tidak boleh null")
    @NotBlank(message = "email tidak boleh kosong")
    var email: String,

    @NotNull(message = "password tidak boleh null")
    @NotBlank(message = "password tidak boleh kosong")
    var password: String,
){}
