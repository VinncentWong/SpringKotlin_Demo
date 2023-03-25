package com.example.ut.user

import com.example.example.app.user.repository.IUserRepository
import com.example.example.app.user.service.UserService
import com.example.example.dto.user.CreateUserDto
import com.example.example.dto.user.LoginUserDto
import com.example.example.entity.Response
import com.example.example.entity.User
import com.example.example.util.BcryptUtil
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.params.provider.CsvSource
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import org.mockito.ArgumentMatchers.any
import org.mockito.ArgumentMatchers.anyString
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.verify
import org.springframework.http.HttpStatus
import java.util.*

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

    @ParameterizedTest
    @DisplayName("user should success login")
    @CsvSource(value = [
        "email1@gmail.com, 11111111",
        "email2@gmail.com, 22222222",
        "email3@gmail.com, 33333333",
        "email4@gmail.com, 44444444",
        "email5@gmail.com, 55555555",
    ])
    fun testUserLoginSuccess(email: String, password: String){
        val dto = LoginUserDto(email, password)
        val user = User()
        user.username = "user ${password.substring(0,1)}"
        user.email = email
        user.password = BcryptUtil.encode(password)
        this.userRepository?.let {
            repo ->
            Mockito.`when`(repo.findByEmail(email)).thenReturn(user)
            val result = this.userService.loginUser(dto)
            verify(this.userRepository)?.findByEmail(email)
        }
    }

    @ParameterizedTest
    @DisplayName("get user by id should success")
    @CsvSource(value = [
        "1, 'User 1', user1@gmail.com",
        "2, 'User 2', user2@gmail.com",
        "3, 'User 3', user3@gmail.com",
        "4, 'User 4', user4@gmail.com",
        "5, 'User 5', user5@gmail.com",
    ])
    fun testGetUserByIdSuccess(ids: String, username: String, email: String){
        val user = User()
        user.id = ids
        user.username = username
        user.email = email
        this.userRepository?.let {
            Mockito.`when`(it.findById(ids)).thenReturn(Optional.of(user))
            val result = this.userService.getUserById(ids)
            assertThat(result.statusCode).isEqualTo(HttpStatus.OK)
            val body = result.body
            val response = body as? Response
            response?.let {
                r ->
                assertThat(response.success).isEqualTo(true)
                assertThat(response.message).isEqualTo("success get user data")
                val data = response.data as? User
                data?.let {
                    u ->
                    assertThat(u.email).isEqualTo(email)
                    assertThat(u.id).isEqualTo(ids)
                    assertThat(u.username).isEqualTo(username)
                }
            }
        }
    }

    @ParameterizedTest
    @DisplayName("delete user by id should success")
    @CsvSource(value = [
        "1, 'User 1', user1@gmail.com",
        "2, 'User 2', user2@gmail.com",
        "3, 'User 3', user3@gmail.com",
        "4, 'User 4', user4@gmail.com",
        "5, 'User 5', user5@gmail.com",
    ])
    fun testDeleteUserById(ids: String, username: String, email: String){
        val user = User()
        user.id = ids
        user.username = username
        user.email = email
        this.userRepository?.let {
            Mockito.`when`(it.findById(ids)).thenReturn(Optional.of(user))
            Mockito.doNothing().`when`(it).deleteById(ids)
            val result = this.userService.deleteUserById(ids)
            assertThat(result.statusCode).isEqualTo(HttpStatus.OK)
            val body = result.body as? Response
            body?.let {
                b ->
                assertThat(b.data).isNull()
                assertThat(b.message).isEqualTo("success delete user data")
                assertThat(b.success).isTrue()
            }
        }
    }
}