package com.alvaroquintana.calculadoraperruna.application

import android.app.Application

class CalculadoraApp : Application() {
    override fun onCreate() {
        super.onCreate()
        initDI()
    }
}