package com.epam.mentoring.kotlin.repository

import com.epam.mentoring.kotlin.model.DogBreed
import org.springframework.data.repository.kotlin.CoroutineCrudRepository


interface DogBreedRepository : CoroutineCrudRepository<DogBreed, Long>
