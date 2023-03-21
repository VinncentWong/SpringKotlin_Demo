package com.example.example.app.user.service

import com.example.example.app.user.repository.IUserRepository
import com.example.example.dto.user.CreateUserDto
import com.example.example.dto.user.LoginUserDto
import com.example.example.entity.Response
import com.example.example.exception.UserNotFoundException
import com.example.example.util.BcryptUtil
import com.example.example.util.JwtUtil
import com.example.example.util.ResponseUtil
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

@Service
class UserService(
    @Autowired
    val userRepository: IUserRepository
): IUserService{

    val log: Logger = LoggerFactory.getLogger(UserService::class.java)

    override fun createUser(dto: CreateUserDto): ResponseEntity<Response> {
        val userDb = this.userRepository.findByEmail(dto.email)
        if(userDb != null){
            return ResponseUtil.sendResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "user with same email already exist", false, null)
        }
        val user = dto.toUser()
        val result = this.userRepository.save(user)
        return ResponseUtil.sendResponse(HttpStatus.CREATED.value(), "success create user", true, result)
    }

    override fun loginUser(dto: LoginUserDto): ResponseEntity<Map<String, Any>> {
        val user = this.userRepository.findByEmail(dto.email)
        user?.let {
            it.password?.let {
                p ->
                if (BcryptUtil.equalsPassword(dto.password, p)){
                    val token = JwtUtil.generateToken(it)
                    val response = mapOf(
                        "success" to true,
                        "message" to "success create user",
                        "data" to user,
                        "token" to token,
                    )
                    return ResponseEntity.status(HttpStatus.OK).body(response)
                }
            }
        }
        throw UserNotFoundException("user data not found")
    }

    override fun getUserById(id: String): ResponseEntity<Response> {
        val user = this.userRepository.findById(id)
        if(user.isEmpty) throw UserNotFoundException("user data not found")
        else return ResponseUtil.sendResponse(HttpStatus.OK.value(), "success get user data", true, user)
    }

    override fun deleteUserById(id: String): ResponseEntity<Response> {
        val user = this.userRepository.findById(id)
        if(user.isEmpty) throw UserNotFoundException("user data not found")
        else {
            this.userRepository.deleteById(id)
            return ResponseUtil.sendResponse(HttpStatus.OK.value(), "success delete user data", true, null)
        }
    }
}