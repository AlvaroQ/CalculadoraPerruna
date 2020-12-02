package com.alvaroquintana.usecases

import com.alvaroquintana.data.repository.SharedPreferencesRepository


class SetPaymentDone(private val sharedPreferencesRepository: SharedPreferencesRepository) {
    operator fun invoke(value: Boolean) {
        sharedPreferencesRepository.paymentDone = value
    }
}
class GetPaymentDone(private val sharedPreferencesRepository: SharedPreferencesRepository) {
    operator fun invoke() = sharedPreferencesRepository.paymentDone
}