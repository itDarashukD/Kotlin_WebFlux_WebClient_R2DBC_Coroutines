package com.epam.mentoring.kotlin.rest

import com.epam.mentoring.kotlin.model.DogBreedApiResponse
import com.epam.mentoring.kotlin.model.ImageUrlResponse
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.awaitBody

@Component
class DogBreedApiClient(webClientBuilder: WebClient.Builder) {

    private val DOG_BREED_API_URL = "https://dog.ceo"
    private val webClient: WebClient = webClientBuilder.baseUrl(DOG_BREED_API_URL).build()

    @Throws(Exception::class)
    suspend fun getBreeds(): Map<String, List<String>> {

        val response = webClient.get()
            .uri("/api/breeds/list/all")
            .retrieve()
            .toEntity(DogBreedApiResponse::class.java)
            .block()

        if (200 != response.statusCode.value()) {
            throw Exception()
        }
        val body = response.body
        return body.message             //Map<String, List<String>> message
    }

    suspend fun getImage(breed: String): ByteArray? {
        var imagesUrls: List<String>? = getImagesUrls(breed)
        var image: ByteArray? = downloadImage(imagesUrls)

        return image
    }

    private suspend fun getImagesUrls(breed: String): List<String>? {
        val response: ImageUrlResponse = webClient.get()
            .uri("/api/breed/$breed/images")
            .retrieve()
            .awaitBody<ImageUrlResponse>()

        if (response == null) {
            throw Exception()
        }
        return response.message
    }


    private suspend fun downloadImage(imagesUrls: List<String>?): ByteArray? {
        var url: String? = imagesUrls
                                    ?.filter { it.isNotBlank() }
                                    ?.first()

        var image: ByteArray? = webClient.get()
            .uri(url)
            .retrieve()
            .awaitBody<ByteArray>()

//            takeIf { it.statusCode.is2xxSuccessful }
//            ?.body

        if (image == null) {
            throw Exception()
        }

        return image
    }

}
