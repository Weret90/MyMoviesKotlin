package com.umbrella.mymovieskotlin.model.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.umbrella.mymovieskotlin.model.Film

@Dao
interface MovieDao {
    @Query("SELECT * FROM movies")
    fun getAllMovie(): LiveData<List<Film>>

    @Query("SELECT * FROM movies WHERE id == :movieId")
    fun getMovieById(movieId: Int): Film

    @Query("DELETE FROM movies")
    fun deleteAllMovies()

    @Insert
    fun insertMovie(movie: Film)

    @Delete
    fun deleteMovie(movie: Film)
}