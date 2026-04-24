package com.alvaroquintana.edadperruna.di

import com.alvaroquintana.edadperruna.managers.AdMobAdsClient
import com.alvaroquintana.edadperruna.managers.AdsClient
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class AdsModule {

    @Binds
    abstract fun bindAdsClient(impl: AdMobAdsClient): AdsClient
}
