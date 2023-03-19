package com.example.example.app.user

import com.example.example.entity.User
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface IUserRepository : MongoRepository<User, String>{}