package com.alvaroquintana.edadperruna.core.di

import com.alvaroquintana.edadperruna.core.domain.age.DogAgeCalculator
import com.alvaroquintana.edadperruna.core.domain.age.LogarithmicDogAgeCalculator
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class DomainModule {

    @Binds
    abstract fun bindDogAgeCalculator(impl: LogarithmicDogAgeCalculator): DogAgeCalculator
}
