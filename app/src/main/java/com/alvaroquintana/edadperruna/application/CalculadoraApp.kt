package com.alvaroquintana.edadperruna.application

import android.app.Application

class CalculadoraApp : Application() {
    override fun onCreate() {
        super.onCreate()
        initDI()
    }
}