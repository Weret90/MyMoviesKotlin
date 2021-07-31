package com.umbrella.mymovieskotlin.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.umbrella.mymovieskotlin.model.Film
import com.umbrella.mymovieskotlin.model.FilmsList
import com.umbrella.mymovieskotlin.model.data.MovieDatabase
import com.umbrella.mymovieskotlin.model.network.RetroInstance
import com.umbrella.mymovieskotlin.model.network.RetroService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val filmsLiveData = MutableLiveData<FilmsList>()
    private val database: MovieDatabase = MovieDatabase.getInstance(application)


    fun getData(): LiveData<FilmsList> {
        return filmsLiveData
    }

    fun makeApiCall(sortBy: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val retroInstance = RetroInstance.getRetroInstance().create(RetroService::class.java)
            val response = retroInstance.getDataFromApi("1", sortBy)
            filmsLiveData.postValue(response)
        }
    }

    fun getAllMoviesFromDB() {
        viewModelScope.launch(Dispatchers.IO) {
            database.movieDao().getAllMovie()
        }
    }

    fun deleteAllMovies() {
        viewModelScope.launch(Dispatchers.IO) {
            database.movieDao().deleteAllMovies()
        }
    }

    fun insertMovie(movie: Film) {
        viewModelScope.launch(Dispatchers.IO) {
            database.movieDao().insertMovie(movie)
        }
    }

    fun deleteMovie(movie: Film) {
        viewModelScope.launch(Dispatchers.IO) {
            database.movieDao().deleteMovie(movie)
        }
    }
}