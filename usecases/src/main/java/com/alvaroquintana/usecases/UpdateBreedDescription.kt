package com.alvaroquintana.usecases

import com.alvaroquintana.data.repository.BreedRepository

class UpdateBreedDescription(private val breedRepository: BreedRepository) {

    suspend fun invoke() = breedRepository.updateBreeds()

}
