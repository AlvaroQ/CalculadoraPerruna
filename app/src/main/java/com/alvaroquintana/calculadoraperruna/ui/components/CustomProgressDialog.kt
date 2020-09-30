package com.alvaroquintana.calculadoraperruna.ui.components

import android.app.Activity
import android.app.Dialog
import android.view.Window
import com.alvaroquintana.calculadoraperruna.R


class CustomProgressDialog(var activity: Activity) : Dialog(activity) {
    var dialog: Dialog? = null

    fun initialize(){
        dialog = Dialog(activity,R.style.ProgressDialog)
        dialog?.apply {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            setCancelable(false)
            setContentView(R.layout.layout_loading)
        }
    }

    fun showDialog() {
        dialog?.show()
    }

    fun hideDialog() {
        dialog?.dismiss()
    }
}