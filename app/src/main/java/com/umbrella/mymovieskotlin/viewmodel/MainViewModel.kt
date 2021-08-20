package com.umbrella.mymovieskotlin.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.umbrella.mymovieskotlin.model.AppState
import com.umbrella.mymovieskotlin.model.Film
import com.umbrella.mymovieskotlin.model.data.MovieDatabasePopularityFilms
import com.umbrella.mymovieskotlin.model.data.MovieDatabaseRating
import com.umbrella.mymovieskotlin.model.network.RetroInstance
import com.umbrella.mymovieskotlin.model.network.RetroService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val filmsFromServerFirstPageLiveData = MutableLiveData<AppState>()
    private val filmsFromServerNextPagesLiveData = MutableLiveData<AppState>()

    private val databasePopularityFilms: MovieDatabasePopularityFilms =
        MovieDatabasePopularityFilms.getInstance(application)
    private val databaseRatingFilms: MovieDatabaseRating =
        MovieDatabaseRating.getInstance(application)

    private val filmsPopularityFromDBLiveData = databasePopularityFilms.movieDao().getAllMovie()
    private val filmsRatingFromDBLiveData = databaseRatingFilms.movieDao().getAllMovie()

    fun getFilmsFromServerFirstPageLiveData(): LiveData<AppState> = filmsFromServerFirstPageLiveData

    fun getFilmsFromServerNextPagesLiveData(): LiveData<AppState> = filmsFromServerNextPagesLiveData

    fun getPopularityFilmsFromDBLiveData(): LiveData<List<Film>> {
        return filmsPopularityFromDBLiveData
    }

    fun getRatingFilmsFromDBLiveData(): LiveData<List<Film>> {
        return filmsRatingFromDBLiveData
    }

    fun downloadFilmsFromServer(sortBy: String, page: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                if (page == 1) {
                    filmsFromServerFirstPageLiveData.postValue(AppState.Loading)
                }
                val retroInstance =
                    RetroInstance.getRetroInstance().create(RetroService::class.java)
                val response = retroInstance.getDataFromApi(page.toString(), sortBy)
                if (page == 1) {
                    filmsFromServerFirstPageLiveData.postValue(AppState.Success(response))
                } else {
                    filmsFromServerNextPagesLiveData.postValue(AppState.Success(response))
                }
            } catch (e: Exception) {
                if (page == 1) {
                    filmsFromServerFirstPageLiveData.postValue(AppState.Error(e))
                } else {
                    filmsFromServerNextPagesLiveData.postValue(AppState.Error(e))
                }
            }
        }
    }

    private fun insertMovieIntoPopularityFilmsDB(movie: Film) {
        viewModelScope.launch(Dispatchers.IO) {
            databasePopularityFilms.movieDao().insertMovie(movie)
        }
    }

    private fun insertMovieIntoRatingFilmsDB(movie: Film) {
        viewModelScope.launch(Dispatchers.IO) {
            databaseRatingFilms.movieDao().insertMovie(movie)
        }
    }

    fun clearAllMoviesInPopularityFilmsDBAndInsertFreshData(films: List<Film>) {
        viewModelScope.launch(Dispatchers.IO) {
            launch { databasePopularityFilms.movieDao().deleteAllMovies() }.join()
            for (film in films) {
                insertMovieIntoPopularityFilmsDB(film)
            }
        }
    }

    fun clearAllMoviesInRatingFilmsDBAndInsertFreshData(films: List<Film>) {
        viewModelScope.launch(Dispatchers.IO) {
            launch { databaseRatingFilms.movieDao().deleteAllMovies() }.join()
            for (film in films) {
                insertMovieIntoRatingFilmsDB(film)
            }
        }
    }
}