package com.alvaroquintana.edadperruna.utils

import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.alvaroquintana.edadperruna.BuildConfig


fun hideKeyboard(activity: Activity) {
    val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
    imm!!.hideSoftInputFromWindow(activity.currentFocus?.windowToken, 0)
    activity.currentFocus?.clearFocus()
    activity.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
}
fun getCircularProgressDrawable(context: Context) : CircularProgressDrawable {
    return CircularProgressDrawable(context).apply {
        strokeWidth = 5f
        centerRadius = 30f
        start()
    }
}
fun log(tag:String?, msg:String?, error:Throwable? = null){
    if (BuildConfig.BUILD_TYPE != "release") {
        if (error != null){
            Log.e(tag, msg, error)
        } else {
            Log.d(tag, msg!!)
        }
    }
}