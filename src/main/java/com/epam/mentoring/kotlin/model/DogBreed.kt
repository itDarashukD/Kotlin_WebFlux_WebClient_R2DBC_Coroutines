package com.epam.mentoring.kotlin.model

import org.springframework.data.relational.core.mapping.Table


@Table
data class DogBreed(
    var id: Long? = null,
    var breed: String = "",
    var subBreed: String = "",
    var image: ByteArray? = null
)


