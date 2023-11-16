package com.mauroalexandro.dogsareawesome.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiClient {
    private lateinit var retrofit: Retrofit

    companion object {
        private var INSTANCE: ApiClient? = null
        fun getInstance(): ApiClient {
            if (INSTANCE == null)
                INSTANCE = ApiClient()

            return INSTANCE as ApiClient
        }
    }

    fun getClient(): ApiEndpointInterface? {
        if (!this::retrofit.isInitialized) {
            retrofit = Retrofit.Builder()
                .baseUrl("https://api.thedogapi.com/v1/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }

        return retrofit.create(ApiEndpointInterface::class.java)
    }
}