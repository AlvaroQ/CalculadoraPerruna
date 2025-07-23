package com.alvaroquintana.edadperruna.utils

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.Base64
import android.widget.ImageView
import com.alvaroquintana.edadperruna.R
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target


fun glideLoadURL(context: Context, url: String?, where: ImageView) {
    Glide.with(context)
        .setDefaultRequestOptions(RequestOptions().timeout(30000))
        .load(url)
        .error(getCircularProgressDrawable(context))
}

fun glideLoadBase64(context: Context, imageBytes: String?, where: ImageView) {
    val imageByteArray: ByteArray = Base64.decode(imageBytes, Base64.DEFAULT)

    Glide.with(context)
        .asBitmap()
        .load(imageByteArray)
        .transition(BitmapTransitionOptions.withCrossFade())
        .into(where)
}

fun glideLoadGif(context: Context, where: ImageView) {

    Glide.with(context)
        .asGif()
        .load(R.drawable.animated_dog)
        .into(where)
}