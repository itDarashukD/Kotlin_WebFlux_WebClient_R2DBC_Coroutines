package com.epam.mentoring.kotlin.rest;

import com.epam.mentoring.kotlin.model.DogBreedApiResponse;
import java.util.List;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

@Component
public class DogBreedApiClient {
//    private static final String DOG_BREED_API_URL = "https://dog.ceo/api/breeds/list/all";
    private static final String DOG_BREED_API_URL = "https://dog.ceo";
//    private final RestTemplate restTemplate;
    private final WebClient webClient;

    //    public DogBreedApiClient(final RestTemplate restTemplate) {
//        this.restTemplate = restTemplate;
//    }
    public DogBreedApiClient(final WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl(DOG_BREED_API_URL).build();

    }

    public Map<String, List<String>> getBreeds() throws Exception {
//        ResponseEntity<DogBreedApiResponse> response = restTemplate.getForEntity(DOG_BREED_API_URL, DogBreedApiResponse.class);

        final ResponseEntity<DogBreedApiResponse> response =
                webClient.get()
                        .uri("/api/breeds/list/all")
                        .retrieve()
                        .toEntity(DogBreedApiResponse.class)
                        .block();


        if(200 != response.getStatusCode().value()) {
            throw new Exception();
        }
        final DogBreedApiResponse body = response.getBody();
        return body.message();  //Map<String, List<String>> message
    }


}
