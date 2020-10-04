package com.alvaroquintana.calculadoraperruna.ui.result

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.alvaroquintana.calculadoraperruna.common.ScopedViewModel

class ResultViewModel() : ViewModel() {


    private val mLoading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = mLoading

}