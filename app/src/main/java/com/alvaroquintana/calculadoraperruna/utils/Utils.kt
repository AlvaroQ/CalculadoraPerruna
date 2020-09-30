package com.alvaroquintana.calculadoraperruna.utils

import android.app.Activity
import android.content.Context
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager


fun hideKeyboard(activity: Activity) {
    val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
    imm!!.hideSoftInputFromWindow(activity.currentFocus?.windowToken, 0)
    activity.currentFocus?.clearFocus()
    activity.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
}