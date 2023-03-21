package com.example.example.configuration

import com.example.example.security.filter.JwtFilter
import com.example.example.security.manager.JwtManager
import com.example.example.security.provider.JwtProvider
import com.example.example.security.provider.MongoDetailsService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.servlet.HandlerExceptionResolver

@Configuration
@EnableWebSecurity
class SecurityConfiguration(
    @Autowired
    private val mongoDetailsService: MongoDetailsService,

    @Qualifier("handlerExceptionResolver")
    private val resolver: HandlerExceptionResolver
){

    val log: Logger = LoggerFactory.getLogger(SecurityConfiguration::class.java)
    @Bean
    fun securityConfigurationMethod(http: HttpSecurity): SecurityFilterChain{
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
                it.requestMatchers("/user/login", "/user/create")
                    .permitAll()
                    .requestMatchers("/user/**")
                    .hasRole("USER")
            }
            .build()
    }

    @Bean
    fun jwtFilter(): JwtFilter{
        return JwtFilter(jwtManager(), resolver)
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