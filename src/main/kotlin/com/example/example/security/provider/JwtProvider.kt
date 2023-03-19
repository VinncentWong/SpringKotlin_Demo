package com.example.example.security.provider

import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.MalformedJwtException
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.SignatureException
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component

@Component
class JwtProvider: AuthenticationProvider{

    @Value("SECRET_KEY")
    var secretKey: String? = null

    override fun authenticate(authentication: Authentication?): Authentication {
        val token = authentication?.principal as? String
        val result = try{
            token?.let {
                val parser = Jwts.parserBuilder()
                    .base64UrlDecodeWith(Decoders.BASE64)
                    .setSigningKey(Decoders.BASE64.decode(secretKey))
                    .build()
                val result = parser.parse(secretKey)

            }
        }
        catch(ex: MalformedJwtException){
            throw MalformedJwtException("jwt was malformed")
        }
        catch(ex: ExpiredJwtException){
            throw ExpiredJwtException(ex.header, ex.claims, "token was expired")
        }
        catch(ex: SignatureException){
            throw SignatureException("wrong signature, are you sure your signature is valid?")
        }
        catch(ex: IllegalArgumentException){
            throw MalformedJwtException("wrong argument type, please do some cross check")
        }

    }

    override fun supports(authentication: Class<*>?): Boolean {
        return authentication == JwtProvider::class.java
    }
}