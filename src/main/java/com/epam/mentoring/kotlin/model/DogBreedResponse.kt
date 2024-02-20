package com.epam.mentoring.kotlin.model

import com.fasterxml.jackson.annotation.JsonInclude

@JvmRecord
@JsonInclude(JsonInclude.Include.NON_NULL)
data class DogBreedResponse(val breed: String,val  subBreeds: List<String>?)

