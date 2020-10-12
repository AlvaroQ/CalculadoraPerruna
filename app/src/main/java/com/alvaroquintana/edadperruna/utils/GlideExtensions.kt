package com.alvaroquintana.edadperruna.utils

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.Base64
import android.util.Log
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

private fun initUrlGlide(context: Context, url: String?) =
    Glide.with(context)
        .setDefaultRequestOptions(RequestOptions().timeout(30000))
        .load(url)
        .error(getCircularProgressDrawable(context))
        .addListener(object : RequestListener<Drawable> {
            override fun onLoadFailed(
                e: GlideException?,
                model: Any?,
                target: Target<Drawable>?,
                isFirstResource: Boolean
            ): Boolean {
                Log.d("Glide", "onLoadFailed", e)
                e?.logRootCauses("GLIDE")
                return false
            }

            override fun onResourceReady(
                resource: Drawable?,
                model: Any?,
                target: Target<Drawable>?,
                dataSource: DataSource?,
                isFirstResource: Boolean
            ): Boolean {
                Log.d("Glide", "onResourceReady")
                return false
            }

        })

fun glideLoadURL(context: Context, url: String?, where: ImageView) {
    initUrlGlide(context, url)
        .placeholder(getCircularProgressDrawable(context))
        .transition(DrawableTransitionOptions.withCrossFade())
        .into(where)
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