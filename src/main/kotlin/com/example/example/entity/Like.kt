package com.example.example.entity

import org.springframework.data.mongodb.core.mapping.MongoId

data class Like(
    var produkId: String?,
    var userId: String?,
    @MongoId
    var id: String?,
){}
