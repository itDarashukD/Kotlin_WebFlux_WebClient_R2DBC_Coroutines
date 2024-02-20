package com.epam.mentoring.kotlin.controller

import com.epam.mentoring.kotlin.model.DogBreed
import com.epam.mentoring.kotlin.model.DogBreedResponse
import com.epam.mentoring.kotlin.service.DogBreedService
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Tag(name = "Dog Breed APIs", description = "Breed APIs for demo purpose")
@RestController
@RequestMapping("v1/breeds")
class DogBreedController(private val dogBreedService: DogBreedService) {

    @GetMapping
    suspend fun allDogBreeds(): List<DogBreedResponse> {
        val breeds = dogBreedService.getBreeds()        //findAll();

        val collect = breeds
                .map { dogBreed: DogBreed -> this.mapToDogBreedResponse(dogBreed) }
                .toList()
        return collect
        //                .toList();
    }

    private fun mapToDogBreedResponse(dogBreed: DogBreed): DogBreedResponse {

        val dogBreedList: List<String>? = convertSubBreeds(dogBreed.subBreed)

        return DogBreedResponse(dogBreed.breed, dogBreedList)
    }


    private fun convertSubBreeds(subBreed: String): List<String>? {
        if (subBreed.isNotBlank()) {
            return subBreed.split(",".toRegex())
        }
        return null
    }
}
