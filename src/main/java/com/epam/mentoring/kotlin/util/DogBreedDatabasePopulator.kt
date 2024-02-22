package com.epam.mentoring.kotlin.util

import com.epam.mentoring.kotlin.rest.DogBreedApiClient
import com.epam.mentoring.kotlin.service.DogBreedService
import kotlinx.coroutines.runBlocking
import org.springframework.context.annotation.Profile
import org.springframework.context.event.ContextRefreshedEvent
import org.springframework.context.event.EventListener

import org.springframework.stereotype.Component

@Profile("dev")
@Component
open class DogBreedDatabasePopulator(private val dogBreedApiClient: DogBreedApiClient, val dogBreedService: DogBreedService) {

// to run initializeDatabase() only after all Beans were created
    @EventListener(ContextRefreshedEvent::class)
    fun onApplicationEvent(event: ContextRefreshedEvent) {
        runBlocking {
            initializeDatabase()
        }
    }

    @Throws(Exception::class)
    suspend fun initializeDatabase(): Unit {
        if (dogBreedService.getAllBreeds().isEmpty()) {
            val breeds: Map<String, List<String>> = dogBreedApiClient.getBreeds();

            dogBreedService.saveAll(breeds);
         }
    }
}