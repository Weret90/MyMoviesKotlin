package com.umbrella.mymovieskotlin.model.network

import com.umbrella.mymovieskotlin.model.FilmsList
import retrofit2.http.GET
import retrofit2.http.Query

interface RetroService {
    @GET("movie?api_key=763ba67be242b6e85d4f7eda657d4059&sort_by=popularity.desc&language=ru-RU")
    suspend fun getDataFromApi(@Query("page") page: String, @Query("with_genres") genre: String): FilmsList
}