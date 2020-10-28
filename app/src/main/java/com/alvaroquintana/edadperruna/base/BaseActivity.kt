package com.alvaroquintana.edadperruna.base

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.alvaroquintana.edadperruna.utils.screenOrientationPortrait
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        screenOrientationPortrait()

        window.apply {
            addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            statusBarColor = Color.TRANSPARENT
            decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        }
    }
}