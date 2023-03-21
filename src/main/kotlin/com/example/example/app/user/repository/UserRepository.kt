package com.example.example.app.user.repository

import com.example.example.entity.User
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.mongodb.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface IUserRepository : MongoRepository<User, String>{
    @Query("{ 'email' : ?0 }", )
    fun findByEmail(email: String): User?
}