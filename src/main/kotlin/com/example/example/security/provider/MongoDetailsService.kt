package com.example.example.security.provider

import com.example.example.app.user.IUserRepository
import com.example.example.userdetails.CustomUserDetails
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Component

@Component
class MongoDetailsService(
    @Autowired
    private val userRepository: IUserRepository
) : UserDetailsService {

    override fun loadUserByUsername(username: String?): UserDetails {
        val customUserDetails = CustomUserDetails(null)
        username?.let {
            val result = this.userRepository.findById(username)
            if(result.isPresent){
                val user = result.get()
                customUserDetails.user = user
                return customUserDetails
            }
        }
        return customUserDetails
    }
}