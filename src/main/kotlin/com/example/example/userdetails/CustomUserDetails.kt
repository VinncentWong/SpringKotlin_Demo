package com.example.example.userdetails

import com.example.example.entity.User
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

data class CustomUserDetails(
    var user: User?
) : UserDetails{
    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        return if(user == null)
            mutableListOf(SimpleGrantedAuthority("NO_ROLE"))
        else
            mutableListOf(SimpleGrantedAuthority("USER"))
    }

    override fun getPassword(): String {
        return this.user.password ?: ""
    }

    override fun getUsername(): String {
        return this.user.username ?: ""
    }

    override fun isAccountNonExpired(): Boolean {
        return true
    }

    override fun isAccountNonLocked(): Boolean {
        return true
    }

    override fun isCredentialsNonExpired(): Boolean {
        return true
    }

    override fun isEnabled(): Boolean {
        return true
    }
}
