package com.example.example.security.authentication

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.GrantedAuthority

class JwtAuthentication(
    principal: String?,
    credential: String?,
    authorities: List<GrantedAuthority>?
) : UsernamePasswordAuthenticationToken(principal, credential, authorities) {

    init{
        if(authorities == null){
            super.setAuthenticated(false)
        } else {
            super.setAuthenticated(true)
        }
    }
}