package com.example.example.configuration

import com.mongodb.client.MongoClients
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.mongodb.core.MongoTemplate

@Configuration
class MongoTemplateConfiguration {

    @Value("\${spring.data.mongodb.database}")
    private lateinit var databaseName: String

    @Value("\${spring.data.mongodb.host}")
    private lateinit var host: String

    @Value("\${spring.data.mongodb.port}")
    private lateinit var port: String

    val log: Logger = LoggerFactory.getLogger(SecurityConfiguration::class.java)

    @Bean
    fun mongoTemplate(): MongoTemplate{
        log.info("database = $databaseName")
        log.info("host = $host")
        log.info("port = $port")
        return MongoTemplate(MongoClients.create("mongodb://$host:$port"), databaseName)
    }
}