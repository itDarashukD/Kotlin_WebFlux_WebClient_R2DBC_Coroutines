package com.epam.mentoring.kotlin.service;

import com.epam.mentoring.kotlin.repository.DogBreedRepository;
import com.epam.mentoring.kotlin.model.DogBreed;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class DogBreedService {
    private final DogBreedRepository dogBreedRepository;

    public DogBreedService(final DogBreedRepository dogBreedRepository) {
        this.dogBreedRepository = dogBreedRepository;
    }

    @Cacheable("breeds")
    public List<DogBreed> getBreeds() {
        final List<DogBreed> all = (List<DogBreed>) dogBreedRepository.findAll();
        return all;
    }

    public void save(final Map<String, List<String>> breeds) {
        List<DogBreed> dogBreeds = breeds.entrySet()
                .stream()
                .map(this::toDogBreed)          //map to objects DogBreed
                .collect(Collectors.toList());
//                .toList();
        dogBreedRepository.saveAll(dogBreeds);
    }

    private DogBreed toDogBreed(final Map.Entry<String, List<String>> entry) {
        final List<String> value = entry.getValue();
        final String subBreeds = String.join(",", value);

        final DogBreed dogBreed = new DogBreed(null, entry.getKey(), subBreeds, null);
        return dogBreed;
    }
}
