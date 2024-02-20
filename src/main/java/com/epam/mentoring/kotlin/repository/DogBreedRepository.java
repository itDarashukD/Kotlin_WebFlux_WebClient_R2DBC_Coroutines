package com.epam.mentoring.kotlin.repository;

import com.epam.mentoring.kotlin.model.DogBreed;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.kotlin.CoroutineCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
//public interface DogBreedRepository extends CrudRepository<DogBreed, Long> {
public interface DogBreedRepository extends CoroutineCrudRepository<DogBreed, Long> {

}
