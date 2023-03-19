package com.example.example.entity

import org.springframework.data.mongodb.core.mapping.MongoId
import java.util.*

data class Produk(
    var namaProduk: String?,
    var detailProduk: String?,
    var linkGambarProduk: String?,
    var lokasiProduk: String?,
    var createdAt: Date,
    var updatedAt: Date?,
    var deletedAt: Date?,
    var likeId: String?,
    var userId: String?,
    @MongoId
    var id: String?,
){}
