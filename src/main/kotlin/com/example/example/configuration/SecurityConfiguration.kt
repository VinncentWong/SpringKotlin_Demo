package com.example.example.configuration

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configurers.CorsConfigurer
import org.springframework.security.web.SecurityFilterChain
import org.springframework.web.cors.CorsConfiguration

@Configuration
class SecurityConfiguration {

    @Bean
    fun securityConfiguration(http: HttpSecurity): SecurityFilterChain{
        return http
            .csrf().disable()
            .formLogin().disable()
            .cors{
                c ->
                c.configurationSource {
                    val corsConfigurer = CorsConfiguration()
                    corsConfigurer.apply {
                        this.allowedHeaders = listOf("*")
                        this.allowedMethods = listOf("*")
                        this.allowedOrigins = listOf("*")
                    }
                }
            }
            .build()
    }
}