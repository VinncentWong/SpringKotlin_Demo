package com.example.example.util

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

object BcryptUtil {

    private val bCryptPasswordEncoder: BCryptPasswordEncoder = BCryptPasswordEncoder()

    fun encode(rawPassword: String): String = this.bCryptPasswordEncoder.encode(rawPassword)

    fun equalsPassword(rawPassword: String, hashPassword: String): Boolean = this.bCryptPasswordEncoder.matches(rawPassword, hashPassword)
}