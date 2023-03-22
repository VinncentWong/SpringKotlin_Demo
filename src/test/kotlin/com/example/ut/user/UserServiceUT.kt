package com.example.ut.user

import com.example.example.app.user.repository.IUserRepository
import com.example.example.app.user.service.UserService
import com.example.example.dto.user.CreateUserDto
import com.example.example.entity.Response
import com.example.example.entity.User
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.params.provider.CsvSource
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.params.ParameterizedTest
import org.mockito.ArgumentMatchers.any
import org.mockito.ArgumentMatchers.anyString
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.verify
import org.springframework.http.HttpStatus

class UserServiceUT {

    @Mock
    var userRepository: IUserRepository? = null

    @InjectMocks
    lateinit var userService: UserService

    @ParameterizedTest
    @DisplayName("user should success register")
    @CsvSource(value = [
        "email1@gmail.com,111111111111,user1",
        "email2@gmail.com,222222222222,user2",
        "email3@gmail.com,333333333333,user3",
        "email4@gmail.com,444444444444,user4",
        "email5@gmail.com,555555555555,user5",
    ])
    fun testUserRegisterSuccess(email: String, password: String, username: String){
        val dto = CreateUserDto(email, password, username)
        this.userRepository?.let {
            repo ->
            Mockito.`when`(repo.findByEmail(anyString())).thenReturn(null)
            Mockito.`when`(repo.save(any())).thenReturn(dto.toUser())
            verify(repo).findByEmail(anyString())
            verify(repo).save(any())
            val result = this.userService.createUser(dto)
            val body = result.body
            assertThat(result.statusCode).isEqualTo(HttpStatus.CREATED).isNotNull
            body?.let{
                val data = body.data as? Response
                data?.let{
                        d ->
                    assertThat(data.message).isEqualTo("success create user").isNotNull
                    assertThat(data.success).isEqualTo(true).isNotNull
                    val contentData = d.data as? Map<*, *>
                    contentData?.let {
                            map ->
                        val user = map["data"] as? User
                        user?.let{
                                u ->
                            assertThat(u.username).isEqualTo(dto.username).isNotNull
                            assertThat(u.email).isEqualTo(dto.email).isNotNull
                        }
                    }
                }
            }
        }
    }
}