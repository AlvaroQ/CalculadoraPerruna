package com.alvaroquintana.edadperruna.application

import android.app.Application
import com.alvaroquintana.data.repository.AppsRecommendedRepository
import com.alvaroquintana.data.repository.BreedRepository
import com.alvaroquintana.data.repository.SharedPreferencesRepository
import com.alvaroquintana.data.source.DataBaseSource
import com.alvaroquintana.data.source.SharedPreferencesLocalDataSource
import com.alvaroquintana.edadperruna.data.server.DataBaseBaseSourceImpl
import com.alvaroquintana.edadperruna.managers.SharedPrefsDataSource
import com.alvaroquintana.edadperruna.ui.breedDescription.BreedDescriptionViewModel
import com.alvaroquintana.edadperruna.ui.breedList.BreedListViewModel
import com.alvaroquintana.edadperruna.ui.home.HomeViewModel
import com.alvaroquintana.edadperruna.ui.moreApps.MoreAppsViewModel
import com.alvaroquintana.edadperruna.ui.result.ResultViewModel
import com.alvaroquintana.edadperruna.ui.settings.SettingsViewModel
import com.alvaroquintana.edadperruna.utils.GetResources
import com.alvaroquintana.usecases.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

fun Application.initDI() {
    startKoin {
        androidLogger()
        androidContext(this@initDI)
        modules(appModule, dataModule, scopesModule)
    }
}

private val appModule = module {
    factory<DataBaseSource> { DataBaseBaseSourceImpl() }
    single { GetResources(get()) }
    single<CoroutineDispatcher> { Dispatchers.Main }
    factory<SharedPreferencesLocalDataSource> { SharedPrefsDataSource(get()) }
}

val dataModule = module {
    factoryOf(::BreedRepository)
    factoryOf(::AppsRecommendedRepository)
    factoryOf(::SharedPreferencesRepository)
}

private val scopesModule = module {
    viewModel { HomeViewModel() }
    viewModel { BreedListViewModel(get(), get(), get()) }
    viewModel { BreedDescriptionViewModel(get(), get(), get()) }
    viewModel { ResultViewModel() }
    viewModel { SettingsViewModel() }
    viewModel { MoreAppsViewModel(get()) }

    factory { GetBreedList(get()) }
    factory { GetScreenViewer(get()) }
    factory { SetScreenViewer(get()) }
    factory { GetBreedsDescription(get()) }
    factory { GetAppsRecommended(get()) }
}
