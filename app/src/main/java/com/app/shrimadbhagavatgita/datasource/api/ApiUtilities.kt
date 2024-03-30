package com.app.shrimadbhagavatgita.datasource.api

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiUtilities {

    val headers = mapOf<String, String>(

        "Accept" to  "application/json",
        "X-RapidAPI-Key" to "aff04e2f71mshe284b841e2dde66p1291b8jsnf9bfa86e885d",
        "X-RapidAPI-Host" to "bhagavad-gita3.p.rapidapi.com"
    )
    val client = OkHttpClient.Builder().apply {
        addInterceptor{chain->
            val newRequest = chain.request().newBuilder().apply {
                headers.forEach{key, value -> addHeader(key, value)}
            }.build()

            chain.proceed(newRequest)
        }
    }.build()

    val api : ApiInterface by lazy {
        Retrofit.Builder()
            .baseUrl("https://bhagavad-gita3.p.rapidapi.com")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiInterface::class.java)
    }
}