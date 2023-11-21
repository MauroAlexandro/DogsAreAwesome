package com.mauroalexandro.dogsareawesome.network

import okhttp3.OkHttpClient
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
            val builder = OkHttpClient.Builder()
            builder.addInterceptor { chain ->
                val request = chain.request().newBuilder()
                val originalHttpUrl = chain.request().url()
                val url = originalHttpUrl.newBuilder()
                    .addQueryParameter("x-api-key", "live_tg2n3ob28c7PbvH0qO6sd9UsliAxLZq5NWnKFRm2pGtfiSu0NZhrjJ8ffuQ58IJh").build()
                request.url(url)
                chain.proceed(request.build())
            }
            val client = builder.build()

            retrofit = Retrofit.Builder()
                .baseUrl("https://api.thedogapi.com/v1/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
        }

        return retrofit.create(ApiEndpointInterface::class.java)
    }
}