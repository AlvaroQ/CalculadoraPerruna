package com.alvaroquintana.calculadoraperruna.base

import androidx.appcompat.app.AppCompatActivity
import com.alvaroquintana.calculadoraperruna.ui.components.CustomProgressDialog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext

abstract class BaseActivity(var uiContext: CoroutineContext = Dispatchers.Main) :
    AppCompatActivity(),
    BaseViewModel,
    CoroutineScope {

    private lateinit var job: Job
    private lateinit var customProgressAlertDialog: CustomProgressDialog

    override val coroutineContext: CoroutineContext
        get() = uiContext + job

    override fun showProgressDialog() {
        customProgressAlertDialog = CustomProgressDialog(this)
        customProgressAlertDialog.initialize()

        if (!customProgressAlertDialog.isShowing)
            customProgressAlertDialog.showDialog()
    }

    override fun hideProgressDialog() {
        if (customProgressAlertDialog.isShowing)
            customProgressAlertDialog.hideDialog()
    }
}