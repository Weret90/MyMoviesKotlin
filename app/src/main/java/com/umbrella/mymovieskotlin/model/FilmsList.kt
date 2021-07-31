package com.umbrella.mymovieskotlin.model


import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class FilmsList(
    @SerializedName("results")
    val films: List<Film>
)

@Entity(tableName = "movies")
data class Film(
//    @SerializedName("genre_ids")
//    val genreIds: List<Int>,
    @SerializedName("original_language")
    val originalLanguage: String,
    @SerializedName("backdrop_path")
    val backdropPath: String,

    @PrimaryKey
    @SerializedName("id")
    val id: Int,

    @SerializedName("original_title")
    val originalTitle: String,
    @SerializedName("overview")
    val overview: String,
    @SerializedName("poster_path")
    val posterPath: String,
    @SerializedName("release_date")
    val releaseDate: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("vote_average")
    val voteAverage: Double,
    @SerializedName("vote_count")
    val voteCount: Int
) : Serializable