package com.alvaroquintana.edadperruna.utils

import android.app.Application

class GetResources(private val context: Application){
    fun getString(idString: Int) = context.getString(idString)
    fun getDrawable(idDrawable: Int) = context.getDrawable(idDrawable)

}