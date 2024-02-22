package com.epam.mentoring.kotlin.service

import com.epam.mentoring.kotlin.model.DogBreed
import com.epam.mentoring.kotlin.repository.DogBreedRepository
import com.epam.mentoring.kotlin.rest.DogBreedApiClient
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertFailsWith

class DogBreedServiceTest {
    /*private val dogBreedRepository: DogBreedRepository = mockk()
    private val dogBreedService = DogBreedService(dogBreedRepository)

    @Test
    fun `test getAllBreeds returns expected breeds`() = runTest {
        // Given
        val expectedBreeds = listOf(DogBreed(1, "Labrador", "", byteArrayOf()))
        coEvery { dogBreedRepository.findAll() } returns expectedBreeds

        // When
        val result = dogBreedService.getAllBreeds()

        // Then
        assertEquals(expectedBreeds, result)
    }
}*/
    private val repo: DogBreedRepository = mockk()
    private val apiClient: DogBreedApiClient = mockk()
    private val mockService = DogBreedService(repo, apiClient)

    private val dogBreedId1 = DogBreed(1, "Labrador", "", byteArrayOf())
    private val dogBreedId2 = DogBreed(2, "Labrador2", "german", byteArrayOf(1, 2, 3))
    private val dogBreedId3 = DogBreed(null, "Labrador", "german,france", null)
    private val dogBreedId4 = DogBreed(null, "Labrador2", "australia,india", null)
    private val dogBreedId5 = DogBreed(5, "Labrador", "", byteArrayOf(5,5,5))

    private var breedsListWithIds1And2 = listOf(dogBreedId1, dogBreedId2)
    private var breedsListWithIds3And4 = listOf(dogBreedId3, dogBreedId4)
    private var breedsListWithExistSubBread = listOf(dogBreedId2)
    private var expectedBreedsEmptyList = emptyList<DogBreed>()
    private val breedMap: Map<String, List<String>> = mutableMapOf(
        "Labrador" to listOf("german", "france"),
        "Labrador2" to listOf("australia", "india")
    )


    @BeforeEach
    fun setUp() {
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun getAllBreeds_whenListIsNotEmptyShouldReturnListWithBreed_breadListReturned() = runTest {
        coEvery { repo.findAll() } returns breedsListWithIds1And2.asFlow()

        val result = mockService.getAllBreeds()

        assertEquals(breedsListWithIds1And2, result)
    }


    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun getAllSubBreeds_whenListBreadsIsEmptyShouldThrowException_illegalStateExceptionThrown() =
        runTest {
            coEvery { repo.findAll() } returns emptyList<DogBreed>().asFlow()

            val exception = assertFailsWith<IllegalStateException> {
                mockService.getAllBreeds()
            }

            assertEquals("All Breads list is empty", exception.message)
        }


    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun getAllSubBreeds_happyPassShouldReturnSubBreedList_subBreedListReturned() = runTest {
        coEvery { repo.findAll() } returns breedsListWithIds1And2.asFlow()

        val result: Set<String> = mockService.getAllSubBreeds()

        assertTrue(result.size == 1)
        assertTrue(result.contains("german"))
    }


    @Test
    fun getAllBreedsWithoutSubBreeds_whenListBreadsIsEmptyShouldeThrowException_illegalStateExceptionThrown() =
        runTest {
            coEvery { repo.findAll() } returns expectedBreedsEmptyList.asFlow()

            val exception = assertFailsWith<IllegalStateException> {
                mockService.getAllBreedsWithoutSubBreeds()
            }

            assertEquals("All Breads list is empty", exception.message)
        }

    @Test
    fun getAllBreedsWithoutSubBreeds__happyPassShouldReturnBreedsWithoutSubBreedsList_BreedsWithoutSubBreedsReturned() =
        runTest {
            coEvery { repo.findAll() } returns breedsListWithIds1And2.asFlow()

            val breedsWithoutSubBreeds: List<DogBreed> = mockService.getAllBreedsWithoutSubBreeds()

            assertTrue(breedsWithoutSubBreeds.size == 1)
            assertTrue(breedsWithoutSubBreeds.first() == dogBreedId1)
        }

    @Test
    fun getAllBreedsWithoutSubBreeds__whenFoundNothingShouldReturnEmptyList_emptyListReturned() =
        runTest {
            coEvery { repo.findAll() } returns breedsListWithExistSubBread.asFlow()

            val breedsWithoutSubBreeds: List<DogBreed> = mockService.getAllBreedsWithoutSubBreeds()

            assertTrue(breedsWithoutSubBreeds.isEmpty())
        }


    @Test
    fun getAllSubBreedsByBreed_whenListBreadsIsEmptyShouldThrowException_illegalStateExceptionThrown() {
        runTest {
            coEvery { repo.findAll() } returns expectedBreedsEmptyList.asFlow()

            val exception = assertFailsWith<IllegalStateException> {
                mockService.getAllSubBreedsByBreed("dummyBreed")
            }
            assertEquals("All Breads list is empty", exception.message)
        }
    }

    @Test
    fun getAllSubBreedsByBreed__happyPassShouldReturnAllSubBreeds_allSubBreedsReturned() = runTest {
        coEvery { repo.findAll() } returns breedsListWithIds1And2.asFlow()

        var allSubBreedsByBreed: Set<String> = mockService.getAllSubBreedsByBreed(dogBreedId2.breed)

        assertTrue(allSubBreedsByBreed.size == 1)
        assertTrue(allSubBreedsByBreed.contains(dogBreedId2.subBreed))
    }

    @Test
    fun getAllSubBreedsByBreed__whenNoBreedsFoundShouldReturnEmptySet_emptySetReturned() = runTest {
        coEvery { repo.findAll() } returns breedsListWithIds1And2.asFlow()

        var allSubBreedsByBreed: Set<String> = mockService.getAllSubBreedsByBreed("notExistsBreed")

        assertTrue(allSubBreedsByBreed.isEmpty())
    }


    @Test
    fun saveAll_happyPassShouldInvokeRepositorySaveAll_invoked() = runTest {
        coEvery { repo.saveAll(breedsListWithIds3And4) } returns breedsListWithIds1And2.asFlow()

        mockService.saveAll(breedMap)

        verify { repo.saveAll(breedsListWithIds3And4) }
    }

    @Test
    fun getImageByBreed_whenListBreadsIsEmptyShouldThrowException_illegalStateExceptionThrown() =
        runTest {
            coEvery { repo.findAll() } returns expectedBreedsEmptyList.asFlow()
            coEvery { apiClient.getImage(match { it.isNotEmpty() }) } returns byteArrayOf(1, 2, 3)

            val exception = assertFailsWith<IllegalStateException> {
                mockService.getImageByBreed("dummyBreed")
            }
            assertEquals("All Breads list is empty", exception.message)
        }

    @Test
    fun getImageByBreed_whenBreadAlredyInDbShouldNotCallSave_saveNotInvoked() {
        runTest {
            coEvery { repo.findAll() } returns breedsListWithIds1And2.asFlow()
            coEvery { apiClient.getImage(match { it.isNotEmpty() }) } returns dogBreedId2.image

            var imageByBreed: ByteArray? = mockService.getImageByBreed("dummyBreed")

            assertEquals(imageByBreed, dogBreedId2.image)
            coVerify(exactly = 0) { repo.save(dogBreedId2) }
        }
    }

    @Test
    fun getImageByBreed_whenBreadNotInDbShouldCallSave_saveInvoked() {
        runTest {
            coEvery { repo.save(any()) } returns  dogBreedId5
            coEvery { repo.findAll() } returns breedsListWithIds1And2.asFlow()
            coEvery { apiClient.getImage(match { it.isNotEmpty() }) } returns dogBreedId5.image

            var imageByBreed: ByteArray? = mockService.getImageByBreed("dummyBreed")

            assertEquals(imageByBreed, dogBreedId5.image)
            coVerify(exactly = 1) { repo.save(any()) }
        }
    }


}