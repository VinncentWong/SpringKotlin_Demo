package com.example.example.security.filter

import com.example.example.entity.Response
import com.example.example.exception.CustomAuthenticationException
import com.example.example.security.authentication.NotAuthenticatedJwt
import com.example.example.security.manager.JwtManager
import com.fasterxml.jackson.databind.ObjectMapper
import io.jsonwebtoken.JwtException
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.http.HttpStatus
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.OncePerRequestFilter
import org.springframework.web.servlet.HandlerExceptionResolver

class JwtFilter(
    @Autowired
    private val manager: JwtManager,
    private val resolver: HandlerExceptionResolver
) : OncePerRequestFilter(){

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val bearerToken = request.getHeader("Authorization")
        if(bearerToken == null){
            filterChain.doFilter(request, response)
        } else {
            if (!bearerToken.startsWith("Bearer")){
                this.resolver.resolveException(request, response, null, JwtException("wrong token type"))
            } else {
                val token = bearerToken.substring(7)
                val authentication: Authentication = NotAuthenticatedJwt(token, null)
                val result = this.manager.authenticate(authentication)
                if(!result.isAuthenticated){
                    this.resolver.resolveException(request, response, null, JwtException(result.principal.toString()))
                } else {
                    SecurityContextHolder.getContext().authentication = result
                    filterChain.doFilter(request, response)
                }
            }
        }
    }
}