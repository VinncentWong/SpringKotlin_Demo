package com.example.example.security.filter

import com.example.example.entity.Response
import com.example.example.security.authentication.JwtAuthentication
import com.example.example.security.manager.JwtManager
import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.OncePerRequestFilter

class JwtFilter(
    @Autowired
    private val manager: JwtManager,
    private val mapper: ObjectMapper = ObjectMapper()
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
            if (!bearerToken.startsWith("Bearer ")){
                this.sendErrorResponse(response, "wrong token type", false, HttpStatus.FORBIDDEN)
                return
            } else {
                val token = bearerToken.substring(7)
                val authentication: Authentication = JwtAuthentication(token, null, null)
                val result = this.manager.authenticate(authentication)
                if(!result.isAuthenticated){
                    this.sendErrorResponse(response, "jwt token is wrong", false, HttpStatus.FORBIDDEN)
                } else {
                    SecurityContextHolder.getContext().authentication = result
                    filterChain.doFilter(request, response)
                }
            }
        }
    }

    private fun sendErrorResponse(httpResponse: HttpServletResponse, message: String, success: Boolean, status: HttpStatus){
        val response = Response(null, success, message)
        httpResponse.contentType = "application/json"
        httpResponse.writer.write(this.mapper.writeValueAsString(response))
        httpResponse.status = status.value()
    }
}