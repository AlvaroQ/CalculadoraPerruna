package com.alvaroquintana.calculadoraperruna.ui.breedList

import com.alvaroquintana.calculadoraperruna.common.ScopedViewModel
import com.alvaroquintana.calculadoraperruna.ui.MainActivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class BreedListViewModel() : ScopedViewModel() {


    fun init(activity: MainActivity) {
        launch {
            activity.progressVisibility(true)
            delay(1000)
            activity.progressVisibility(false)
        }
    }
}