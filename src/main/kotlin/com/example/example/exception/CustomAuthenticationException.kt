package com.example.example.exception

import javax.naming.AuthenticationException

class CustomAuthenticationException(message: String) : AuthenticationException(message){}