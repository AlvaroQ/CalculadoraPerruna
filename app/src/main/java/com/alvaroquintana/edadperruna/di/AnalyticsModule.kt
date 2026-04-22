package com.alvaroquintana.edadperruna.di

import com.alvaroquintana.edadperruna.managers.Analytics
import com.alvaroquintana.edadperruna.managers.AnalyticsManager
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class AnalyticsModule {

    @Binds
    abstract fun bindAnalytics(impl: AnalyticsManager): Analytics
}
