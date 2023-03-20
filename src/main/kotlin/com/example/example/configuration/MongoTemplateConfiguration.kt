package com.example.example.configuration

import com.mongodb.client.MongoClients
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.mongodb.core.MongoTemplate

@Configuration
class MongoTemplateConfiguration {

    @Value("SPRING_DATA_MONGODB_DATABASE")
    private lateinit var databaseName: String

    @Value("SPRING_DATA_MONGODB_HOST")
    private lateinit var host: String

    @Value("SPRING_DATA_MONGODB_PORT")
    private lateinit var port: String

    @Bean
    fun mongoTemplate(): MongoTemplate{
        return MongoTemplate(MongoClients.create("mongodb://$host:$port"), databaseName)
    }
}