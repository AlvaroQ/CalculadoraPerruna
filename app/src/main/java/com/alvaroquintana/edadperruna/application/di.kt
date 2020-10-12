package com.alvaroquintana.edadperruna.application

import android.app.Application
import com.alvaroquintana.edadperruna.datasource.DataBaseBaseSourceImpl
import com.alvaroquintana.edadperruna.ui.breedList.BreedListViewModel
import com.alvaroquintana.edadperruna.ui.MainActivity
import com.alvaroquintana.edadperruna.ui.MainViewModel
import com.alvaroquintana.edadperruna.ui.breedList.BreedListFragment
import com.alvaroquintana.edadperruna.ui.home.HomeFragment
import com.alvaroquintana.edadperruna.ui.home.HomeViewModel
import com.alvaroquintana.edadperruna.ui.result.ResultFragment
import com.alvaroquintana.edadperruna.ui.result.ResultViewModel
import com.alvaroquintana.edadperruna.utils.GetResources
import com.alvaroquintana.data.datasource.DataBaseSource
import com.alvaroquintana.data.repository.BreedListRepository
import com.alvaroquintana.usecases.GetBreedList
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
    factory<DataBaseSource> { DataBaseBaseSourceImpl() }
    single {GetResources(get())}
    single<CoroutineDispatcher> { Dispatchers.Main }
}

val dataModule = module {
    factory { BreedListRepository(get()) }
}

private val scopesModule = module {
    scope(named<MainActivity>()) {
        viewModel { MainViewModel() }
    }

    scope(named<HomeFragment>()) {
        viewModel { HomeViewModel() }
    }

    scope(named<BreedListFragment>()) {
        viewModel { BreedListViewModel(get()) }
        scoped { GetBreedList(get()) }
    }

    scope(named<ResultFragment>()) {
        viewModel { ResultViewModel() }
    }

}
