package com.alvaroquintana.edadperruna.application

import android.app.Application
import com.alvaroquintana.data.repository.AppsRecommendedRepository
import com.alvaroquintana.data.repository.BreedRepository
import com.alvaroquintana.data.repository.SharedPreferencesRepository
import com.alvaroquintana.data.source.DataBaseSource
import com.alvaroquintana.data.source.LocalDataSource
import com.alvaroquintana.data.source.SharedPreferencesLocalDataSource
import com.alvaroquintana.edadperruna.data.database.DogDatabase
import com.alvaroquintana.edadperruna.data.database.RoomDataSource
import com.alvaroquintana.edadperruna.data.localfiles.FileLocalDb
import com.alvaroquintana.edadperruna.data.server.DataBaseBaseSourceImpl
import com.alvaroquintana.edadperruna.managers.SharedPrefsDataSource
import com.alvaroquintana.edadperruna.ui.MainActivity
import com.alvaroquintana.edadperruna.ui.MainViewModel
import com.alvaroquintana.edadperruna.ui.breedDescription.BreedDescriptionFragment
import com.alvaroquintana.edadperruna.ui.breedDescription.BreedDescriptionViewModel
import com.alvaroquintana.edadperruna.ui.breedList.BreedListFragment
import com.alvaroquintana.edadperruna.ui.breedList.BreedListViewModel
import com.alvaroquintana.edadperruna.ui.home.HomeFragment
import com.alvaroquintana.edadperruna.ui.home.HomeViewModel
import com.alvaroquintana.edadperruna.ui.moreApps.MoreAppsFragment
import com.alvaroquintana.edadperruna.ui.moreApps.MoreAppsViewModel
import com.alvaroquintana.edadperruna.ui.result.ResultFragment
import com.alvaroquintana.edadperruna.ui.result.ResultViewModel
import com.alvaroquintana.edadperruna.ui.settings.SettingsFragment
import com.alvaroquintana.edadperruna.ui.settings.SettingsViewModel
import com.alvaroquintana.edadperruna.utils.GetResources
import com.alvaroquintana.usecases.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.core.qualifier.named
import org.koin.dsl.module

fun Application.initDI() {
    startKoin {
        androidLogger()
        androidContext(this@initDI)
        koin.loadModules(listOf(
            appModule,
            dataModule,
            scopesModule
        ))
        koin.createRootScope()
    }
}

private val appModule = module {
    factory<DataBaseSource> { DataBaseBaseSourceImpl(get()) }
    single {FileLocalDb(get())}
    single {GetResources(get())}
    single<CoroutineDispatcher> { Dispatchers.Main }
    single { DogDatabase.build(get()) }
    factory<LocalDataSource> { RoomDataSource(get()) }
    factory<SharedPreferencesLocalDataSource> { SharedPrefsDataSource(get()) }
}

val dataModule = module {
    factory { BreedRepository(get(), get()) }
    factory { AppsRecommendedRepository(get()) }
    factory { SharedPreferencesRepository(get()) }
}

private val scopesModule = module {
    scope(named<MainActivity>()) {
        viewModel { MainViewModel() }
    }

    scope(named<HomeFragment>()) {
        viewModel { HomeViewModel(get()) }
        scoped { GetPaymentDone(get()) }
    }

    scope(named<BreedListFragment>()) {
        viewModel { BreedListViewModel(get(), get()) }
        scoped { GetPaymentDone(get()) }
        scoped { GetBreedList(get()) }
    }

    scope(named<BreedDescriptionFragment>()) {
        viewModel { BreedDescriptionViewModel(get(), get()) }
        scoped { GetPaymentDone(get()) }
        scoped { GetBreedsDescription(get()) }
    }

    scope(named<ResultFragment>()) {
        viewModel { ResultViewModel(get()) }
        scoped { GetPaymentDone(get()) }
    }

    scope(named<SettingsFragment>()) {
        viewModel { SettingsViewModel(get(), get(), get()) }
        scoped { GetPaymentDone(get()) }
        scoped { SetPaymentDone(get()) }
        scoped { UpdateBreedDescription(get()) }
    }

    scope(named<MoreAppsFragment>()) {
        viewModel { MoreAppsViewModel(get(), get()) }
        scoped { GetPaymentDone(get()) }
        scoped { GetAppsRecommended(get()) }
    }

}
