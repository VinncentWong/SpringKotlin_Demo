package com.example.example.app.user.controller

import com.example.example.app.user.service.UserService
import com.example.example.dto.user.CreateUserDto
import com.example.example.dto.user.LoginUserDto
import com.example.example.entity.Response
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/user")
class UserController(
    private val service: UserService
) : IUserController{

    @PostMapping("/create")
    override fun createUser(@Valid dto: CreateUserDto): ResponseEntity<Response> {
        return this.service.createUser(dto)
    }

    @PostMapping("/login")
    override fun loginUser(@Valid dto: LoginUserDto): ResponseEntity<Map<String, Any>> {
        return this.service.loginUser(dto)
    }

    @GetMapping("/get/{id}")
    override fun getUserById(@PathVariable("id") id: String): ResponseEntity<Response> {
        return this.getUserById(id)
    }

    @DeleteMapping("/delete/{id}")
    override fun deleteUserById(@PathVariable("id") id: String): ResponseEntity<Response> {
        return this.deleteUserById(id)
    }

}