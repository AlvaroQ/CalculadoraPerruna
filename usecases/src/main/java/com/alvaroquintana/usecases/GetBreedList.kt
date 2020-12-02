package com.alvaroquintana.usecases

import com.alvaroquintana.data.repository.BreedRepository
import com.alvaroquintana.domain.Dog

class GetBreedList(private val breedRepository: BreedRepository) {

    suspend fun invoke(): MutableList<Dog> = breedRepository.getBreedList()

}
