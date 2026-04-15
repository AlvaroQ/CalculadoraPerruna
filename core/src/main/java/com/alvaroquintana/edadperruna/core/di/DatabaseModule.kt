package com.alvaroquintana.edadperruna.core.di

import android.content.Context
import androidx.room.Room
import com.alvaroquintana.edadperruna.core.data.local.dao.DogDao
import com.alvaroquintana.edadperruna.core.data.local.db.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(context, AppDatabase::class.java, "edadperruna.db")
            .fallbackToDestructiveMigration()
            .build()

    @Provides
    fun provideDogDao(database: AppDatabase): DogDao = database.dogDao()
}
