package com.alvaroquintana.usecases

import com.alvaroquintana.data.repository.BreedRepository
import com.alvaroquintana.domain.Dog

class GetBreedsDescription(private val breedRepository: BreedRepository) {

    suspend fun invoke(breedId: String): Dog? = breedRepository.getBreedDescription(breedId)

}
