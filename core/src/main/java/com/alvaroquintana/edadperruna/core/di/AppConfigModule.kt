package com.alvaroquintana.edadperruna.core.di

import android.content.Context
import com.alvaroquintana.edadperruna.core.data.repository.ApplicationId
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object AppConfigModule {

    @Provides
    @ApplicationId
    fun provideApplicationId(@ApplicationContext context: Context): String = context.packageName
}
