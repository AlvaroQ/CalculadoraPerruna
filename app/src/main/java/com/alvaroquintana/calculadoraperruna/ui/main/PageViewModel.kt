package com.alvaroquintana.calculadoraperruna.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.alvaroquintana.calculadoraperruna.utils.Constants

class PageViewModel : ViewModel() {

    private val _index = MutableLiveData<Int>()

    val text: LiveData<String> = Transformations.map(_index) {
        if(it == Constants.SECTION_DOG) "Introduzca la edad y raza de su perro para traducirlo a la edad que tendría si fuese una persona."
        else "Introduzca su edad para traducirlo a la edad que tendría si fuese la raza seleccionada."
    }

    fun setIndex(index: Int) {
        _index.value = index
    }
}