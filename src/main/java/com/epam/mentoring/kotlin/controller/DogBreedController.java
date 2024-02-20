package com.epam.mentoring.kotlin.controller;

import com.epam.mentoring.kotlin.model.DogBreed;
import com.epam.mentoring.kotlin.model.DogBreedResponse;
import com.epam.mentoring.kotlin.service.DogBreedService;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Dog Breed APIs", description = "Breed APIs for demo purpose")
@RestController()
@RequestMapping("v1/breeds")
public class DogBreedController {

    private final DogBreedService dogBreedService;

    public DogBreedController(final DogBreedService dogBreedService) {
        this.dogBreedService = dogBreedService;
    }

    @GetMapping()
    public List<DogBreedResponse> getAllDogBreeds() {
        final List<DogBreed> breeds = dogBreedService.getBreeds(); //findAll();

        final List<DogBreedResponse> collect =
                breeds.stream()
                        .map(this::mapToDogBreedResponse)
                        .collect(Collectors.toList());
        return collect;
//                .toList();
    }

    private DogBreedResponse mapToDogBreedResponse(DogBreed dogBreed) {
       return new DogBreedResponse(dogBreed.getBreed(),convertSubBreeds(dogBreed.getSubBreed()));

    }

    private List<String> convertSubBreeds(final String subBreed) {
        if (StringUtils.isNotBlank(subBreed)) {
            return Arrays.asList(subBreed.split(","));
        }
        return null;
    }
}
