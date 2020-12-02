package com.alvaroquintana.usecases

import com.alvaroquintana.data.repository.BreedRepository
import com.alvaroquintana.domain.Dog

class UpdateBreedDescription(private val breedRepository: BreedRepository) {

    suspend fun invoke() = breedRepository.updateBreeds()

}
