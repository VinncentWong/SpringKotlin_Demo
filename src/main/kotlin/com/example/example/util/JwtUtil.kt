package com.example.example.util

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import java.security.Key
import java.util.UUID

object JwtUtil {

    val secretKey: Key = Keys.secretKeyFor(SignatureAlgorithm.HS256)

    fun <T : IExtractable> generateToken(datas: T): String{
        val data = datas.getData()
        val claims = Jwts.claims()
        claims["createdAt"] = data["createdAt"]
        claims["username"] = data["username"]
        claims["email"] = data["email"]
        claims["id"] = data["id"]
        claims["token-unique"] = UUID.randomUUID()
        return Jwts.builder()
            .addClaims(claims)
            .signWith(secretKey)
            .compact()
    }
}