package com.example.example.app.user.service

import com.example.example.dto.user.CreateUserDto
import com.example.example.dto.user.LoginUserDto
import com.example.example.entity.Response
import org.springframework.http.ResponseEntity

interface IUserService{
    fun createUser(dto: CreateUserDto): ResponseEntity<Response>
    fun loginUser(dto: LoginUserDto): ResponseEntity<Map<String, Any>>
    fun getUserById(id: String): ResponseEntity<Response>
    fun deleteUserById(id: String): ResponseEntity<Response>
}