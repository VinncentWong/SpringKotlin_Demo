package com.example.example.security.provider

import com.example.example.security.authentication.JwtAuthentication
import io.jsonwebtoken.Claims
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.MalformedJwtException
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.SignatureException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.stereotype.Component

@Component
class JwtProvider(
    @Autowired
    private val mongoDetailsService: MongoDetailsService
) : AuthenticationProvider{

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
                val claims = result.body as? Claims
                claims?.let {
                    c ->
                    val id = c["id"] as? String
                    val userdetails = this.mongoDetailsService.loadUserByUsername(id)
                    if(userdetails.username.isBlank() || userdetails.username.isEmpty()){
                        JwtAuthentication("user not authenticated in database", null, null)
                    } else {
                        val authorities = ArrayList<GrantedAuthority>()
                        userdetails.authorities.forEach {
                            authorities.add(it)
                        }
                        JwtAuthentication(it, id, authorities)
                    }
                }
            }
            JwtAuthentication("token not exist", null, null)
        }
        catch(ex: MalformedJwtException){
            JwtAuthentication("jwt was malformed", null, null)
        }
        catch(ex: ExpiredJwtException){
            JwtAuthentication("token was expired", null, null)
        }
        catch(ex: SignatureException){
            JwtAuthentication("wrong signature, are you sure your signature is valid?", null, null)
        }
        catch(ex: IllegalArgumentException){
            JwtAuthentication("wrong argument type, please do some cross check", null, null)
        }
        return result
    }

    override fun supports(authentication: Class<*>?): Boolean {
        return authentication == JwtProvider::class.java
    }
}