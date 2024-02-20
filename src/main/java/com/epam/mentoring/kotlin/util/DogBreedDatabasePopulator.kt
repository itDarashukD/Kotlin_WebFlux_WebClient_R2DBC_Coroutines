package com.epam.mentoring.kotlin.util

import com.epam.mentoring.kotlin.rest.DogBreedApiClient
import com.epam.mentoring.kotlin.service.DogBreedService
import jakarta.annotation.PostConstruct
import org.springframework.stereotype.Component

@Component
class DogBreedDatabasePopulator(val dogBreedApiClient: DogBreedApiClient,val dogBreedService: DogBreedService) {

    @Throws(Exception::class)
    @PostConstruct
    fun initializeDatabase(): Unit {
        if (dogBreedService.getBreeds().isEmpty()) {  //findAll();
            val breeds: Map<String, List<String>> =
                dogBreedApiClient.getBreeds(); //call https://dog.ceo/api/breeds/list/all";

            dogBreedService.save(breeds);
        }
    }
}