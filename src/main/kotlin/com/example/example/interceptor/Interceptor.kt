package com.example.example.interceptor

import com.example.example.entity.Response
import com.example.example.exception.UserNotFoundException
import com.example.example.util.ResponseUtil
import io.jsonwebtoken.JwtException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class Interceptor(){

    @ExceptionHandler(JwtException::class)
    fun handleException(ex: JwtException): ResponseEntity<Response>{
        return ResponseUtil.sendResponse(HttpStatus.FORBIDDEN.value(), ex.message ?: "jwt exception occured", false, null)
    }

    @ExceptionHandler(UserNotFoundException::class)
    fun handleException(ex: UserNotFoundException): ResponseEntity<Response>{
        return ResponseUtil.sendResponse(HttpStatus.NOT_FOUND.value(), ex.message ?: "user data not found", false, null)
    }
}