package com.alvaroquintana.data.repository

import com.alvaroquintana.data.source.SharedPreferencesLocalDataSource


class SharedPreferencesRepository(private val sharedPreferencesLocalDataSource: SharedPreferencesLocalDataSource)  {

    var paymentDone: Boolean
        get() = sharedPreferencesLocalDataSource.paymentDone
        set(value) {
            sharedPreferencesLocalDataSource.paymentDone = value
        }

    var nightTheme: Boolean
        get() = sharedPreferencesLocalDataSource.isNightTheme
        set(value) {
            sharedPreferencesLocalDataSource.isNightTheme = value
        }
}