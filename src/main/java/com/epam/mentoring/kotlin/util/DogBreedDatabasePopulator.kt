package com.epam.mentoring.kotlin.util

import com.epam.mentoring.kotlin.rest.DogBreedApiClient
import com.epam.mentoring.kotlin.service.DogBreedService
import kotlinx.coroutines.runBlocking
import org.springframework.context.event.ContextRefreshedEvent
import org.springframework.context.event.EventListener

import org.springframework.stereotype.Component

@Component
class DogBreedDatabasePopulator(val dogBreedApiClient: DogBreedApiClient,val dogBreedService: DogBreedService) {

// to run initializeDatabase() only after all Beans were created
    @EventListener(ContextRefreshedEvent::class)
    fun onApplicationEvent(event: ContextRefreshedEvent) {
        runBlocking {
            initializeDatabase()
        }
    }

    @Throws(Exception::class)
    suspend fun initializeDatabase(): Unit {
        if (dogBreedService.getBreeds().isEmpty()) {  //findAll();
            val breeds: Map<String, List<String>> = dogBreedApiClient.getBreeds(); //call https://dog.ceo/api/breeds/list/all";

            dogBreedService.save(breeds);
         }
    }
}