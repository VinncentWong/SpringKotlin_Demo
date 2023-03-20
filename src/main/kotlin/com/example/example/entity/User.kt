package com.example.example.entity

import com.example.example.util.IExtractable
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
    var username: String?,
    var createdAt: Date,
    var updatedAt: Date?,
    var idLike: String?,
    @MongoId
    var id: String?
): IExtractable{
    constructor(): this(null, null, null,
        null, null, null,
        null, Date(), null, null, null
    )

    override fun getData(): Map<String, Any?> {
        val map = mapOf(
            "id" to this.id,
            "username" to this.username,
            "email" to this.email,
            "createdAt" to this.createdAt
        )
        return map
    }
}