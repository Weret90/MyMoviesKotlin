package com.umbrella.mymovieskotlin.model.data

import androidx.lifecycle.LiveData
import androidx.room.*
import com.umbrella.mymovieskotlin.model.Film

@Dao
interface MovieDao {
    @Query("SELECT * FROM movies")
    fun getAllMovie(): LiveData<List<Film>>

    @Query("SELECT * FROM movies WHERE id == :movieId")
    fun getMovieById(movieId: Int): Film?

    @Query("DELETE FROM movies")
    fun deleteAllMovies()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMovie(movie: Film)

    @Delete
    fun deleteMovie(movie: Film)
}