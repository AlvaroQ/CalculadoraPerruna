package com.alvaroquintana.edadperruna.base

import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext

abstract class BaseActivity(var uiContext: CoroutineContext = Dispatchers.Main) :
    AppCompatActivity(),
    BaseViewModel,
    CoroutineScope {

    private lateinit var job: Job

    override val coroutineContext: CoroutineContext
        get() = uiContext + job
}