package com.alvaroquintana.calculadoraperruna.common

import android.content.res.Resources
import androidx.annotation.StringRes

class ResourceProviderImpl(private var resources: Resources) : ResourceProvider {
    override fun getString(@StringRes stringResId: Int): String {
        return resources.getString(stringResId)
    }
}

interface ResourceProvider {
    fun getString(@StringRes stringResId: Int): String
}
