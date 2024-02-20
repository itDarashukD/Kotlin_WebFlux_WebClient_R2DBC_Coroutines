package com.epam.mentoring.kotlin.model

import org.springframework.data.relational.core.mapping.Table


@Table
data class DogBreed(
    var id: Long?,
    var breed: String,
    var subBreed: String,
    var image: ByteArray ?
)
