package com.epam.mentoring.kotlin.service

import com.epam.mentoring.kotlin.model.DogBreed
import com.epam.mentoring.kotlin.repository.DogBreedRepository
import com.epam.mentoring.kotlin.rest.DogBreedApiClient
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.toList
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service



@Service
open class DogBreedService(
    @Autowired private val dogBreedRepository: DogBreedRepository,
    val dogBreedApiClient: DogBreedApiClient,
) {

    companion object {
        private val log = LoggerFactory.getLogger(DogBreedService::class.java)
    }


    @Cacheable("breeds")
    open suspend fun getAllBreeds(): List<DogBreed> {
        return getBreeds()
    }

    open suspend fun getAllSubBreeds(): Set<String> {
        var allBreeds: List<DogBreed> = getBreeds()

        return allBreeds.map { dogBreed -> dogBreed.subBreed }
                        .filter { it.isNotEmpty() }
                        .toSet()
    }

    open suspend fun getAllBreedsWithoutSubBreeds(): List<DogBreed> {
        var allBreeds: List<DogBreed> = getBreeds()

        return allBreeds
            .filter { it.subBreed.isBlank() }
            .toList()
    }

    open suspend fun getAllSubBreedsByBreed(breed: String): Set<String> {
        var allBreeds: List<DogBreed> = getBreeds()

        return allBreeds.filter { it.breed == breed }
                        .map { it.subBreed }
                        .toSet()
    }

    open suspend fun saveAll(breeds: Map<String, List<String>>){
        val dogBreeds: List<DogBreed> = breeds.entries
                                                    .map(this::toDogBreed)
                                                    .toList()

        dogBreedRepository.saveAll(dogBreeds).collect();
    }

    open suspend fun getImageByBreed(breed: String): ByteArray? {
        val image: ByteArray? = dogBreedApiClient.getImage(breed)
        val dogBreed = DogBreed(image = image)

        if (isAlreadyInDb(dogBreed)) {
            return image
        } else {
            dogBreedRepository.save(dogBreed).also { return image }
        }
    }

    private suspend fun getBreeds(): List<DogBreed> {
        val allBreeds: List<DogBreed> = dogBreedRepository.findAll().toList()
        if (allBreeds.isEmpty()) {
            log.warn("AllBreads list is empty")
        }
        return allBreeds
    }

    private suspend fun isAlreadyInDb(dogBreed: DogBreed): Boolean {
        val allBreeds: List<DogBreed> = getBreeds()

        return allBreeds.filter { it.image.contentEquals(dogBreed.image) }.isNotEmpty()
    }

    private fun toDogBreed(entry: Map.Entry<String, List<String>>): DogBreed {
        val value: List<String> = entry.value
        val subBreeds: String = value.joinToString(",")

        return DogBreed(null, entry.key, subBreeds, null)
    }


}
