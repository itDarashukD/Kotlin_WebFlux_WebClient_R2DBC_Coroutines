package com.epam.mentoring.kotlin

import io.swagger.v3.oas.annotations.OpenAPIDefinition
import io.swagger.v3.oas.annotations.info.Info
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cache.annotation.EnableCaching
import org.springframework.context.annotation.Bean
import org.springframework.web.client.RestTemplate

@OpenAPIDefinition(
    info = Info(
        title = "Dog Breed Service",
        version = "1.0",
        description = "Template for Kotlin for Backend Developers mentoring programme"
    )
)
@EnableCaching
@SpringBootApplication
class Application {

    @Bean
    fun restTemplate(): RestTemplate {
        return RestTemplate()
    }

    fun main(args: Array<String>) {
        runApplication<Application>(*args)
    }

}
