package com.epam.mentoring.kotlin.rest

import com.epam.mentoring.kotlin.model.DogBreedApiResponse
import com.epam.mentoring.kotlin.model.ImageUrlResponse
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient

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
        var imagesUrls: List<String> = getImagesUrls(breed)
        var image: ByteArray? = downloadImage(imagesUrls)

        return image
    }

    private fun getImagesUrls(breed: String): List<String> {
        val response = webClient.get()
            .uri("/api/breed/$breed/images")
            .retrieve()
            .toEntity(ImageUrlResponse::class.java)
            .block()

        if (200 != response.statusCode.value()) {
            throw Exception()
        }
        val body = response.body.urlsList
        return body
    }


    private fun downloadImage(imagesUrls: List<String>): ByteArray? {
//        var imageList: List<ByteArray?> = imagesUrls.map { url ->
//            webClient.get()
//                .uri(url)
//                .retrieve()
//                .toEntity(ByteArray::class.java)
//                .block()
//                .takeIf { it.statusCode.is2xxSuccessful }
//                ?.body
//        }

        var url: String? = imagesUrls.takeIf { it.isNullOrEmpty() }?.first()

        var image: ByteArray? = webClient.get()
            .uri(url)
            .retrieve()
            .toEntity(ByteArray::class.java)
            .block()
            .takeIf { it.statusCode.is2xxSuccessful }
            ?.body

        return image
    }

}
