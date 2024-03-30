package com.app.shrimadbhagavatgita.datasource.api

import com.app.shrimadbhagavatgita.models.ChaptersItem
import com.app.shrimadbhagavatgita.models.VersesItem
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path

interface ApiInterface {
//    @Headers(
//        "Accept: application/json",
//        "X-RapidAPI-Key: aff04e2f71mshe284b841e2dde66p1291b8jsnf9bfa86e885d",
//        "X-RapidAPI-Host: bhagavad-gita3.p.rapidapi.com"
//    )
    @GET("/v2/chapters/")
    fun getAllChapters(): Call<List<ChaptersItem>>


    @GET("/v2/chapters/{chapterNumber}/verses/")
    fun getVerses(@Path("chapterNumber") chapterNumber : Int): Call<List<VersesItem>>

    @GET("/v2/chapters/{chapterNum}/verses/{verseNum}/")
    fun getAParticularVerse(
        @Path("chapterNum") chapterNumber : Int,
        @Path("verseNum") verseNum : Int,
                            ): Call<VersesItem>
}



























