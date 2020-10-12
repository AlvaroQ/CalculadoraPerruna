package com.alvaroquintana.calculadoraperruna.ui.breedList

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.navigation.Navigation
import com.alvaroquintana.calculadoraperruna.common.ScopedViewModel
import com.alvaroquintana.domain.Dog
import com.alvaroquintana.usecases.GetBreedList
import kotlinx.coroutines.launch

class BreedListViewModel(private val getBreedList: GetBreedList) : ScopedViewModel() {

    private val _progress = MutableLiveData<Boolean>()
    val progress: LiveData<Boolean> = _progress

    private val _list = MutableLiveData<MutableList<Dog>>()
    val list: LiveData<MutableList<Dog>> = _list

    private val _navigation = MutableLiveData<Navigation>()
    val navigation: LiveData<Navigation> = _navigation

    fun init() {
        launch {
            _progress.value = true
            _list.value = getBreedList()
            _progress.value = false
        }
    }

    private suspend fun getBreedList(): MutableList<Dog> {
        return getBreedList.invoke()
    }


    fun onBackPressed() {
        _navigation.value = Navigation.Home(Dog("",0,""))
    }

    fun onDogClicked(dog: Dog) {
        _navigation.value = Navigation.Home(dog)
    }

    fun onDogLongClicked(dog: Dog) {
        _navigation.value = Navigation.Expand(dog.icon)
    }

    private fun <Dog> getSubList(list: MutableList<Dog>): MutableList<Dog> {
        val subList: MutableList<Dog> = ArrayList()
        for (i in 0..110) {
            subList.add(list[i])
        }
        return subList
    }

    sealed class Navigation {
        data class Home(val breed : Dog): Navigation()
        data class Expand(val image : String): Navigation()
    }
}