package com.epam.mentoring.kotlin.service

import com.epam.mentoring.kotlin.model.DogBreed
import com.epam.mentoring.kotlin.repository.DogBreedRepository
import com.epam.mentoring.kotlin.rest.DogBreedApiClient
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.toList
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service

@Service
open class DogBreedService(
    @Autowired private val dogBreedRepository: DogBreedRepository,
    val dogBreedApiClient: DogBreedApiClient,
) {


    @Cacheable("breeds")
    open suspend fun getAllBreeds(): List<DogBreed> {
        var allBreeds: List<DogBreed> = dogBreedRepository.findAll().toList()

        println("11111111111111 $allBreeds")

        if (allBreeds.isNotEmpty()) {
            return allBreeds
        }
        return emptyList()
    }

    open suspend fun getAllSubBreeds(): Set<String> {
        var allBreeds: List<DogBreed> = dogBreedRepository.findAll().toList()

        val collect = allBreeds.map { dogBreed -> dogBreed.subBreed }.toSet()
        println("11111111111111 $collect")

        if (collect.isNotEmpty()) {
            return collect
        }
        return emptySet()
    }

    open suspend fun getAllBreedsWithoutSubBreeds(): List<DogBreed> {
        var allBreeds: List<DogBreed> = dogBreedRepository.findAll().toList()

        val collect = allBreeds
            .filter { it.subBreed.isBlank() }
            .toList()
        println("11111111111111 $collect")

        if (collect.isNotEmpty()) {
            return collect
        }
        return emptyList()
    }

    open suspend fun getAllSubBreedsByBreed(breed: String): List<String> {
        var allBreeds: List<DogBreed> = dogBreedRepository.findAll().toList()

        val collect = allBreeds.filter { it.breed == breed }
                                        .map { it.subBreed }
                                        .toList()

        println("11111111111111 $collect")
        if (collect.isNotEmpty()) {
            return collect
        }
        return emptyList()

    }

    open suspend fun getImageByBreed(breed: String): ByteArray? {
        var image: ByteArray? = dogBreedApiClient.getImage(breed)
        val dogBreed = DogBreed(image = image)

        if (isAlreadyInDb(dogBreed)) {
            return image
        } else {
            dogBreedRepository.save(dogBreed).also { return image }
        }
    }

    private suspend fun isAlreadyInDb(dogBreed: DogBreed): Boolean {
        var allBreeds: List<DogBreed> = dogBreedRepository.findAll().toList()

        var filter: List<DogBreed> = allBreeds.filter { it.image.contentEquals(dogBreed.image) }
        if (filter.isNotEmpty()) {
            return true
        }
        return false
    }

    open suspend fun saveAll(breeds: Map<String, List<String>>): Unit {
        val dogBreeds: List<DogBreed> = breeds.entries
            .map(this::toDogBreed)          //map to objects DogBreed
            .toList()

        dogBreedRepository.saveAll(dogBreeds).collect();

    }

    private fun toDogBreed(entry: Map.Entry<String, List<String>>): DogBreed {
        val value: List<String> = entry.value
        val subBreeds: String = value.joinToString(",")

        val dogBreed: DogBreed = DogBreed(null, entry.key, subBreeds, null)
        return dogBreed;
    }
}