package com.mauroalexandro.dogsareawesome.network

import com.mauroalexandro.dogsareawesome.models.Breed
import com.mauroalexandro.dogsareawesome.models.BreedImage
import retrofit2.http.GET
import retrofit2.Call
import retrofit2.http.Path

interface ApiEndpointInterface {
    @GET("breeds/")
    fun getBreeds(): Call<ArrayList<Breed>>

    @GET("images/{id}")
    fun getBreedImageByID(@Path(value = "id", encoded = true) id: String): Call<BreedImage>
}