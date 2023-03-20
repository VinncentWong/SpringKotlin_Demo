package com.example.example.security.manager

import com.example.example.security.provider.JwtProvider
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component

class JwtManager(
    @Autowired
    val provider: JwtProvider
) : AuthenticationManager{
    override fun authenticate(authentication: Authentication?): Authentication {
        return provider.authenticate(authentication)
    }
}