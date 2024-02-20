package com.epam.mentoring.kotlin

import io.r2dbc.spi.ConnectionFactory
import io.swagger.v3.oas.annotations.OpenAPIDefinition
import io.swagger.v3.oas.annotations.info.Info
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cache.annotation.EnableCaching
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.core.io.ClassPathResource
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate
import org.springframework.r2dbc.connection.init.CompositeDatabasePopulator
import org.springframework.r2dbc.connection.init.ConnectionFactoryInitializer
import org.springframework.r2dbc.connection.init.ResourceDatabasePopulator

@OpenAPIDefinition(
    info = Info(
        title = "Dog Breed Service",
        version = "1.0",
        description = "Template for Kotlin for Backend Developers mentoring programme"
    )
)
@EnableCaching
@ComponentScan(basePackages = ["com.epam.mentoring.kotlin","com.epam.mentoring.kotlin.repository.DogBreedRepository"])
@SpringBootApplication
open class Application {

    @Bean
    open fun initializer(connectionFactory: ConnectionFactory): ConnectionFactoryInitializer {
        val initializer = ConnectionFactoryInitializer()
        initializer.setConnectionFactory(connectionFactory)
        val populator = CompositeDatabasePopulator()
        populator.addPopulators(ResourceDatabasePopulator(ClassPathResource("./schema.sql")))
        initializer.setDatabasePopulator(populator)

        return initializer
    }

    @Bean
    open fun r2dbcEntityTemplate(connectionFactory: ConnectionFactory): R2dbcEntityTemplate {
        return R2dbcEntityTemplate(connectionFactory)
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            runApplication<Application>(*args)
        }
    }

}
