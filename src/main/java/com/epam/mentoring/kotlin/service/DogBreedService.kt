package com.epam.mentoring.kotlin.service

import com.epam.mentoring.kotlin.model.DogBreed
import com.epam.mentoring.kotlin.repository.DogBreedRepository
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.toList
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service

@Service
open class DogBreedService(@Autowired private val dogBreedRepository: DogBreedRepository)  {


    @Cacheable("breeds")
   open suspend fun getBreeds(): List<DogBreed> {
        var all: List<DogBreed> = dogBreedRepository.findAll().toList()
        return all;
    }

    open suspend fun save(breeds: Map<String, List<String>>): Unit {
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