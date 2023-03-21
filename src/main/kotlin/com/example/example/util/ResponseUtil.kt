package com.example.example.util

import com.example.example.entity.Response
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

object ResponseUtil{
    fun sendResponse(status: Int, message: String, success: Boolean, datas: Any?): ResponseEntity<Response>{
        if(success){
            val data = mapOf(
                "success" to success,
                "message" to message,
                "data" to datas
            )
            val response = Response(data, success, message)
            return ResponseEntity.status(status).body(response)
        } else {
            val response = Response(null, success, message)
            return ResponseEntity.status(status).body(response)
        }
    }
}