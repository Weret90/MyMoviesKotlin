package com.umbrella.mymovieskotlin.model.network

import com.umbrella.mymovieskotlin.BuildConfig
import com.umbrella.mymovieskotlin.model.FilmsList
import com.umbrella.mymovieskotlin.model.ReviewList
import com.umbrella.mymovieskotlin.model.TrailerList
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

private const val apiKey = BuildConfig.TMDB_API_KEY

interface RetroService {
    @GET("discover/movie?api_key=$apiKey&language=ru-RU")
    suspend fun getDataFromApi(
        @Query("page") page: String,
        @Query("sort_by") sortBy: String
    ): FilmsList


    @GET("movie/{movie_id}/videos?api_key=$apiKey&language=ru-RU")
    suspend fun getTrailersFromAPi(
        @Path("movie_id") movieId: String
    ): TrailerList

    @GET("movie/{movie_id}/reviews?api_key=$apiKey")
    suspend fun getReviewsFromApi(
        @Path("movie_id") reviewId: String
    ): ReviewList
}