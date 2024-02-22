package com.epam.mentoring.kotlin.controller

import com.epam.mentoring.kotlin.model.DogBreed
import com.epam.mentoring.kotlin.service.DogBreedService
import com.ninjasquad.springmockk.MockkBean
import io.mockk.coEvery
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.test.web.reactive.server.expectBody
import java.util.*



@ActiveProfiles(value = ["integration-test"])
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
class DogBreedControllerTest {


    @Autowired
    private lateinit var webTestClient: WebTestClient

    @MockkBean
    private lateinit var mockService: DogBreedService

    private val dogBreedId1 = DogBreed(1, "Labrador", "", byteArrayOf())
    private val dogBreedId2 = DogBreed(2, "Labrador2", "german", byteArrayOf(1, 2, 3))
    private var breedsListWithIds1And2 = listOf(dogBreedId1, dogBreedId2)
    private val subBreedsSet = mutableSetOf("german", "france")

    @Test
    fun getAllDogBreeds() = runTest {
        coEvery { mockService.getAllBreeds() } returns breedsListWithIds1And2

        webTestClient.get().uri("/v1/breeds")
            .exchange()
            .expectStatus().isOk
            .expectBody()
            .jsonPath("$[0].breed").isEqualTo("Labrador")
            .jsonPath("$[1].subBreeds[0]").isEqualTo("german")
            .jsonPath("$[1].breed").isEqualTo("Labrador2")
    }


    @Test
    fun getAllSubBreeds() = runTest {
        coEvery { mockService.getAllSubBreeds() } returns subBreedsSet

        webTestClient.get().uri("/v1/breeds/subBreeds")
            .exchange()
            .expectStatus().isOk
            .expectBody()
            .jsonPath("$[0]").isEqualTo("german")
            .jsonPath("$[1]").isEqualTo("france")
    }

    @Test
    fun getAllSubBreedsByBreed() = runTest {
        coEvery { mockService.getAllSubBreedsByBreed("Labrador") } returns subBreedsSet

        webTestClient.get().uri("/v1/breeds//{breed}",dogBreedId1.breed)
            .exchange()
            .expectStatus().isOk
            .expectBody()
            .jsonPath("$[0]").isEqualTo("german")
            .jsonPath("$[1]").isEqualTo("france")
    }

    @Test
    fun getAllBreedsWithoutSubBreeds() = runTest {
        coEvery { mockService.getAllBreedsWithoutSubBreeds() } returns breedsListWithIds1And2

        webTestClient.get().uri("/v1/breeds/breedsWithoutSubBreeds")
            .exchange()
            .expectStatus().isOk
            .expectBody()
            .jsonPath("$[0].breed").isEqualTo("Labrador")
    }


    @Test
    fun getImageByBreed() = runTest {
        val expectedBase64Image = Base64.getEncoder().encodeToString(byteArrayOf(1, 2, 3))

        coEvery { mockService.getImageByBreed(dogBreedId2.breed) } returns byteArrayOf(1, 2, 3)

        webTestClient.get().uri("/v1/breeds/image/{breed}", dogBreedId2.breed)
            .exchange()
            .expectStatus().isOk
            .expectBody<ByteArray>()
            .consumeWith { response ->
                val responseBody = response.responseBody!!
                val base64Response = Base64.getEncoder().encodeToString(responseBody)
                assertEquals(expectedBase64Image, base64Response)
            }
    }

}