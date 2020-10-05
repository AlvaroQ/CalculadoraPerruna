package com.alvaroquintana.calculadoraperruna.application

import android.app.Application
import com.alvaroquintana.calculadoraperruna.ui.breedList.BreedListViewModel
import com.alvaroquintana.calculadoraperruna.ui.MainActivity
import com.alvaroquintana.calculadoraperruna.ui.MainViewModel
import com.alvaroquintana.calculadoraperruna.ui.breedList.BreedListFragment
import com.alvaroquintana.calculadoraperruna.ui.home.HomeFragment
import com.alvaroquintana.calculadoraperruna.ui.home.HomeViewModel
import com.alvaroquintana.calculadoraperruna.ui.result.ResultFragment
import com.alvaroquintana.calculadoraperruna.ui.result.ResultViewModel
import com.alvaroquintana.calculadoraperruna.utils.GetResources
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
            scopesModule
        ))
        koin.createRootScope()
    }
}

private val appModule = module {
    single {GetResources(get())}
}

private val scopesModule = module {
    scope(named<MainActivity>()) {
        viewModel { MainViewModel() }
    }

    scope(named<HomeFragment>()) {
        viewModel { HomeViewModel() }
    }

    scope(named<BreedListFragment>()) {
        viewModel { BreedListViewModel() }
    }

    scope(named<ResultFragment>()) {
        viewModel { ResultViewModel() }
    }

}
