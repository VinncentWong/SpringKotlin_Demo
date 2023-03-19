package com.example.example.entity

import org.springframework.data.annotation.CreatedDate
import org.springframework.data.mongodb.core.mapping.MongoId
import java.util.*

data class User(
    var email: String?,
    var password: String?,
    var linkFotoProfil: String?,
    var kontakWhatsapp: String?,
    var kontakLine: String?,
    var kontakInstagram: String?,
    var createdAt: Date,
    var updatedAt: Date,
    var idLike: String?,
    @MongoId
    var id: String?
)
