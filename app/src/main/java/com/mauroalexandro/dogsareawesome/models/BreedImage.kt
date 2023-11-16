package com.mauroalexandro.dogsareawesome.models

data class BreedImage(
    val breeds: List<Breed>,
    val height: Int,
    val id: String,
    val url: String,
    val width: Int
)