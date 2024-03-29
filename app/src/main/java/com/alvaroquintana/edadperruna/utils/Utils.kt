package com.alvaroquintana.edadperruna.utils

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.res.Resources
import android.net.Uri
import android.os.Build
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.alvaroquintana.edadperruna.BuildConfig
import com.alvaroquintana.edadperruna.R
import com.alvaroquintana.edadperruna.ui.helpers.ImagePreviewer
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

inline fun <reified T> Gson.fromJson(json: String) = fromJson<T>(json, object: TypeToken<T>() {}.type)

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
fun log(tag: String?, msg: String?, error: Throwable? = null){
    if (BuildConfig.BUILD_TYPE != "release") {
        if (error != null){
            Log.e(tag, msg, error)
        } else {
            Log.d(tag, msg!!)
        }
    }
}

fun showBanner(show: Boolean, adView: AdView){
    if(show) {
        val adRequest = AdRequest.Builder().build()
        adView.loadAd(adRequest)
    } else {
        adView.visibility = View.GONE
    }
}
fun showBonificado(activity: Activity, show: Boolean, rewardedAd: RewardedAd?) {
    if(show) {
        rewardedAd?.let { ad ->
            ad.show(activity) { rewardItem ->
                Log.d("loadBonificado", "User earned the reward. rewardAmount=$rewardItem.amount, rewardType=$rewardItem.type")
            }
        } ?: run {
            Log.d("loadBonificado", "The rewarded ad wasn't ready yet.")
        }
    }
}

fun Activity.screenOrientationPortrait(){
    requestedOrientation = if (Build.VERSION.SDK_INT == 26) {
        ActivityInfo.SCREEN_ORIENTATION_BEHIND
    } else {
        ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    }
}

fun rateApp(context: Context) {
    val uri: Uri = Uri.parse("market://details?id=${BuildConfig.APPLICATION_ID}")
    val goToMarket = Intent(Intent.ACTION_VIEW, uri)
    goToMarket.addFlags(
        Intent.FLAG_ACTIVITY_NO_HISTORY or
                Intent.FLAG_ACTIVITY_NEW_DOCUMENT or
                Intent.FLAG_ACTIVITY_MULTIPLE_TASK
    )
    try {
        context.startActivity(goToMarket)
    } catch (e: ActivityNotFoundException) {
        context.startActivity(
            Intent(
                Intent.ACTION_VIEW,
                Uri.parse("http://play.google.com/store/apps/details?id=${BuildConfig.APPLICATION_ID}")
            )
        )
    }
}

fun shareApp(points: Int, context: Context) {
    try {
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "text/plain"
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, context.getString(R.string.app_name))

        var shareMessage =
            if(points < 0) context.resources.getString(R.string.share)
            else context.resources.getString(R.string.share_summary)

        shareMessage =
            """
                ${shareMessage} https://play.google.com/store/apps/details?id=${BuildConfig.APPLICATION_ID}
                """.trimIndent()
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage)
        context.startActivity(
            Intent.createChooser(
                shareIntent,
                context.getString(R.string.choose_one)
            )
        )
    } catch (e: Exception) {
        log(context.getString(R.string.share), e.toString())
    }
}

fun openAppOnPlayStore(context: Context, appPackageName: String) {
    try {
        context.startActivity(
            Intent(
                Intent.ACTION_VIEW,
                Uri.parse("market://details?id=$appPackageName")
            )
        )
    } catch (notFoundException: ActivityNotFoundException) {
        context.startActivity(
            Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://play.google.com/store/apps/details?id=$appPackageName")
            )
        )
    }
}

fun openURL(context: Context, url: String) {
    val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
    context.startActivity(browserIntent)
}

fun expandImage(activity: Activity, imageView: ImageView, icon: String) {
    ImagePreviewer().show(activity, imageView, icon)
}

fun getScreenHeight(): Float {
    return Resources.getSystem().displayMetrics.heightPixels.toFloat()
}

fun getScreenWidth(): Float {
    return Resources.getSystem().displayMetrics.widthPixels.toFloat()
}