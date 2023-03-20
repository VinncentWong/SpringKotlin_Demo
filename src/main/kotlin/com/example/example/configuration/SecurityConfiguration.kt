package com.example.example.configuration

import com.example.example.security.filter.JwtFilter
import com.example.example.security.manager.JwtManager
import com.example.example.security.provider.JwtProvider
import com.example.example.security.provider.MongoDetailsService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configurers.CorsConfigurer
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.web.cors.CorsConfiguration

@Configuration
class SecurityConfiguration(
    @Autowired
    private val mongoDetailsService: MongoDetailsService
){

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
            .addFilterBefore(jwtFilter(), UsernamePasswordAuthenticationFilter::class.java)
            .authorizeHttpRequests {
                it.requestMatchers("/**/login", "/**/register")
                    .permitAll()
                    .anyRequest()
                    .authenticated()
            }
            .build()
    }

    @Bean
    fun jwtFilter(): JwtFilter{
        return JwtFilter(jwtManager())
    }

    @Bean
    fun jwtManager(): JwtManager{
        return JwtManager(jwtProvider())
    }

    @Bean
    fun jwtProvider(): JwtProvider{
        return JwtProvider(this.mongoDetailsService)
    }
}