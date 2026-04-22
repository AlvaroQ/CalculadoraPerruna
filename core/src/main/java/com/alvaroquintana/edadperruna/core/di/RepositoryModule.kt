package com.alvaroquintana.edadperruna.core.di

import com.alvaroquintana.edadperruna.core.data.repository.BreedRepositoryImpl
import com.alvaroquintana.edadperruna.core.data.repository.PreferencesRepositoryImpl
import com.alvaroquintana.edadperruna.core.domain.repository.BreedRepository
import com.alvaroquintana.edadperruna.core.domain.repository.PreferencesRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindBreedRepository(impl: BreedRepositoryImpl): BreedRepository

    @Binds
    abstract fun bindPreferencesRepository(impl: PreferencesRepositoryImpl): PreferencesRepository
}
