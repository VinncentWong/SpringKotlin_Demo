package com.example.example.security.authentication

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.GrantedAuthority

class NotAuthenticatedJwt(
    principal: String?,
    credential: String?,
) : UsernamePasswordAuthenticationToken(principal, credential) {}

class AuthenticatedJwt(
    principal: String?,
    credential: String?,
    authorities: List<GrantedAuthority>
) : UsernamePasswordAuthenticationToken(principal, credential, authorities){}