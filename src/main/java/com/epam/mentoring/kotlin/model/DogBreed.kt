package com.epam.mentoring.kotlin.model

import org.springframework.data.relational.core.mapping.Table


@Table
data class DogBreed(
    val id: Long? = null,
    val breed: String = "",
    val subBreed: String = "",
    val image: ByteArray? = null
)


