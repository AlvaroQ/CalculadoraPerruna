package com.alvaroquintana.usecases

import com.alvaroquintana.data.repository.SharedPreferencesRepository

class SetScreenViewer(private val sharedPreferencesRepository: SharedPreferencesRepository) {
    operator fun invoke(value: Int) {
        sharedPreferencesRepository.screenViewer = value
    }
}
class GetScreenViewer(private val sharedPreferencesRepository: SharedPreferencesRepository) {
    operator fun invoke() = sharedPreferencesRepository.screenViewer
}