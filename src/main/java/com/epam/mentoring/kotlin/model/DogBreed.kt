package com.epam.mentoring.kotlin.model

import jakarta.persistence.*
import lombok.AllArgsConstructor
import lombok.Data
import lombok.EqualsAndHashCode
import lombok.NoArgsConstructor


@Entity
@Table
data class DogBreed(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) var id: Long?,
    var breed: String,
    var subBreed: String,
    var image: ByteArray ?
)
