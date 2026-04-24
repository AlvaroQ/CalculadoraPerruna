package com.alvaroquintana.edadperruna.core.di

import com.alvaroquintana.edadperruna.core.domain.age.DogAgeCalculator
import com.alvaroquintana.edadperruna.core.domain.age.LogarithmicDogAgeCalculator
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

// :core-domain-pure is JVM-only and intentionally has no DI annotations.
// Hilt wiring lives here, in the Android-aware composition root.
@Module
@InstallIn(SingletonComponent::class)
object DomainModule {

    @Provides
    @Singleton
    fun provideDogAgeCalculator(): DogAgeCalculator = LogarithmicDogAgeCalculator()
}
