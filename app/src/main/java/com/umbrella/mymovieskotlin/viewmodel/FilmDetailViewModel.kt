package com.umbrella.mymovieskotlin.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.umbrella.mymovieskotlin.model.Film
import com.umbrella.mymovieskotlin.model.ReviewList
import com.umbrella.mymovieskotlin.model.TrailerList
import com.umbrella.mymovieskotlin.model.data.MovieDatabaseFavouriteFilms
import com.umbrella.mymovieskotlin.model.network.RetroInstance
import com.umbrella.mymovieskotlin.model.network.RetroService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FilmDetailViewModel(application: Application) : AndroidViewModel(application) {

    private val databaseFavouriteFilms = MovieDatabaseFavouriteFilms.getInstance(application)
    private val favouriteFilmsFromDBLiveData = databaseFavouriteFilms.movieDao().getAllMovie()

    private val trailersLiveData = MutableLiveData<TrailerList>()
    private val reviewsLiveData = MutableLiveData<ReviewList>()

    fun getTrailersLiveData() = trailersLiveData
    fun getReviewsLiveData() = reviewsLiveData

    fun getFavouriteFilmsFromDBLiveData(): LiveData<List<Film>> {
        return favouriteFilmsFromDBLiveData
    }

    fun downloadTrailers(movieId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val retroInstance =
                    RetroInstance.getRetroInstance().create(RetroService::class.java)
                val response = retroInstance.getTrailersFromAPi(movieId)
                trailersLiveData.postValue(response)
            } catch (e: Exception) {

            }
        }
    }

    fun downloadReviews(movieId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val retroInstance =
                    RetroInstance.getRetroInstance().create(RetroService::class.java)
                val response = retroInstance.getReviewsFromApi(movieId)
                reviewsLiveData.postValue(response)
            } catch (e: Exception) {

            }
        }
    }

    fun insertFavouriteMovieIntoDB(movie: Film) {
        viewModelScope.launch(Dispatchers.IO) {
            databaseFavouriteFilms.movieDao().insertMovie(movie)
        }
    }

    fun deleteFavouriteMovieFromDB(movie: Film) {
        viewModelScope.launch(Dispatchers.IO) {
            databaseFavouriteFilms.movieDao().deleteMovie(movie)
        }
    }

    fun getFavouriteMovieByIdFromDBLiveData(id: Int) =
        databaseFavouriteFilms.movieDao().getMovieById(id)
}