package com.alvaroquintana.usecases

import com.alvaroquintana.data.repository.BreedListRepository
import com.alvaroquintana.domain.Dog

class GetBreedList(private val breedListRepository: BreedListRepository) {

    suspend fun invoke(): MutableList<Dog> = breedListRepository.getBreedList()

}
