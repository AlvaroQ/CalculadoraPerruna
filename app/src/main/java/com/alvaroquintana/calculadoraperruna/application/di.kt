package com.alvaroquintana.calculadoraperruna.application

import android.app.Application
import com.alvaroquintana.calculadoraperruna.ui.main.MainActivity
import com.alvaroquintana.calculadoraperruna.ui.main.PageViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.core.qualifier.named
import org.koin.dsl.module

fun Application.initDI() {
    startKoin {
        androidLogger()
        androidContext(this@initDI)
        modules(listOf(
                scopesModule
            )
        )
    }
}

private val scopesModule = module {
    scope(named<MainActivity>()) {
        viewModel { PageViewModel() }
    }
}
