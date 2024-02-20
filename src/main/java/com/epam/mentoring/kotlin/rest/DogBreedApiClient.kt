package com.epam.mentoring.kotlin.rest

import com.epam.mentoring.kotlin.model.DogBreedApiResponse
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient

@Component
class DogBreedApiClient(webClientBuilder: WebClient.Builder) {

    //    private static final String DOG_BREED_API_URL = "https://dog.ceo/api/breeds/list/all";

    private val DOG_BREED_API_URL = "https://dog.ceo"
    private val webClient: WebClient = webClientBuilder.baseUrl(DOG_BREED_API_URL).build()

    @Throws(Exception::class)
    fun getBreeds(): Map<String, List<String>> {
//        ResponseEntity<DogBreedApiResponse> response = restTemplate.getForEntity(DOG_BREED_API_URL, DogBreedApiResponse.class);
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

}
