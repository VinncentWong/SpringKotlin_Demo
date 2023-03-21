package com.example.example.security.provider

import com.example.example.security.authentication.AuthenticatedJwt
import com.example.example.security.authentication.NotAuthenticatedJwt
import com.example.example.util.JwtUtil
import io.jsonwebtoken.Claims
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.MalformedJwtException
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.SignatureException
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.stereotype.Component
import java.security.Key

class JwtProvider(
    @Autowired
    private val mongoDetailsService: MongoDetailsService
) : AuthenticationProvider{
    private val secretKey: Key = JwtUtil.secretKey
    override fun authenticate(authentication: Authentication?): Authentication {
        val token = authentication?.principal as? String
        val result = try{
            token?.let {
                val parser = Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                val result = parser.parseClaimsJws(it)
                val claims = result.body as? Claims
                claims?.let {
                    c ->
                    val id = c["id"] as? String
                    val userdetails = this.mongoDetailsService.loadUserByUsername(id)
                    if(userdetails.username.isBlank() || userdetails.username.isEmpty()){
                        NotAuthenticatedJwt("user not authenticated in database", null)
                    } else {
                        val authorities = ArrayList<GrantedAuthority>()
                        userdetails.authorities.forEach {
                            authorities.add(it)
                        }
                        AuthenticatedJwt(it, id, authorities)
                    }
                }
            }
        }
        catch(ex: MalformedJwtException){
            NotAuthenticatedJwt("token was malformed", null)
        }
        catch(ex: ExpiredJwtException){
            NotAuthenticatedJwt("token was expired", null)
        }
        catch(ex: SignatureException){
            NotAuthenticatedJwt("token signature was invalid", null)
        }
        catch(ex: IllegalArgumentException){
            NotAuthenticatedJwt(ex.message, null)
        }
        return result!!
    }

    override fun supports(authentication: Class<*>?): Boolean {
        return authentication == JwtProvider::class.java
    }
}