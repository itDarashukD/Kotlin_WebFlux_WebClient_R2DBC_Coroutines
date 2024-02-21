package com.epam.mentoring.kotlin.controller

import com.epam.mentoring.kotlin.model.DogBreed
import com.epam.mentoring.kotlin.model.DogBreedResponse
import com.epam.mentoring.kotlin.service.DogBreedService
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Tag(name = "Dog Breed APIs", description = "Breed APIs for demo purpose")
@RestController
@RequestMapping("v1/breeds")
class DogBreedController(private val dogBreedService: DogBreedService) {

    @GetMapping
    suspend fun getAllDogBreeds(): List<DogBreedResponse> {
        val breeds = dogBreedService.getAllBreeds()        //findAll();

        val collect = breeds
            .map { dogBreed -> this.mapToDogBreedResponse(dogBreed) }
            .toList()
        return collect
        //                .toList();
    }

    @GetMapping
    suspend fun getAllSubBreeds(): List<String> {
        return dogBreedService.getAllSubBreeds()
    }

    @GetMapping(path = ["/{breed}"])
    suspend fun getAllSubBreedsByBreed(@PathVariable(value = "breed") breed: String): List<String> {
        return dogBreedService.getAllSubBreedsByBreed(breed)
    }

    @GetMapping
    suspend fun getAllBreedsWithoutSubBreeds(): List<DogBreedResponse> {
        var allBreedsWithoutSubBreeds = dogBreedService.getAllBreedsWithoutSubBreeds()

        val collect = allBreedsWithoutSubBreeds
            .map { dogBreed -> this.mapToDogBreedResponse(dogBreed) }
            .toList()
        return collect
    }

    @GetMapping(path = ["/{breed}"])
    suspend fun getImageByBreed(@PathVariable(value = "breed") breed: String): ByteArray? {
        return dogBreedService.getImageByBreed(breed)
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
