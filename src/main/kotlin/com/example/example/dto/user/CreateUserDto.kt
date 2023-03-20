package com.example.example.dto.user

import com.example.example.entity.User
import com.example.example.util.BcryptUtil
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import org.hibernate.validator.constraints.Length

data class CreateUserDto(
    @NotNull(message = "email tidak boleh null")
    @NotBlank(message = "email tidak boleh kosong")
    @Email(message = "format email tidak valid")
    var email: String,

    @NotNull(message = "password tidak boleh null")
    @NotBlank(message = "password tidak boleh kosong")
    @Length(min = 6, message = "minimal panjang password 6")
    var password: String,

    @NotNull(message = "email tidak boleh null")
    @NotBlank(message = "email tidak boleh kosong")
    var username: String,
){
    fun toUser(): User{
        val user = User()
        user.email = this.email
        user.password = BcryptUtil.encode(this.password)
        user.username = this.username
        return user
    }
}
