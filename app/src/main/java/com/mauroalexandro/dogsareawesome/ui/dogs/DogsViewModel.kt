package com.mauroalexandro.dogsareawesome.ui.dogs

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class DogsViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is dogs Fragment"
    }
    val text: LiveData<String> = _text
}