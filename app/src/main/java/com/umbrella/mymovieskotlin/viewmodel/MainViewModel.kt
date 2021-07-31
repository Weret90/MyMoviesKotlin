package com.umbrella.mymovieskotlin.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umbrella.mymovieskotlin.model.FilmsList
import com.umbrella.mymovieskotlin.model.network.RetroInstance
import com.umbrella.mymovieskotlin.model.network.RetroService
import com.umbrella.mymovieskotlin.view.FilmsFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    private val horrorsLiveData = MutableLiveData<FilmsList>()
    private val actionsLiveData = MutableLiveData<FilmsList>()
    private val comediesLiveData = MutableLiveData<FilmsList>()
    private val loadingBarLiveData = MutableLiveData<String>()

    fun getData(genre: String): LiveData<FilmsList> {
        return when (genre) {
            FilmsFragment.ACTION -> actionsLiveData
            FilmsFragment.HORROR -> horrorsLiveData
            else -> comediesLiveData
        }
    }

    fun getLoadingBarLiveData(): LiveData<String> {
        return loadingBarLiveData
    }

    fun makeApiCall() {
        viewModelScope.launch(Dispatchers.IO) {
            val retroInstance = RetroInstance.getRetroInstance().create(RetroService::class.java)
            joinAll(
                launch {
                    val responseHorrors = retroInstance.getDataFromApi("1", FilmsFragment.HORROR)
                    horrorsLiveData.postValue(responseHorrors)
                },
                launch {
                    val responseComedies = retroInstance.getDataFromApi("1", FilmsFragment.COMEDY)
                    comediesLiveData.postValue(responseComedies)
                },
                launch {
                    val responseActions = retroInstance.getDataFromApi("1", FilmsFragment.ACTION)
                    actionsLiveData.postValue(responseActions)
                }
            )
            loadingBarLiveData.postValue("Download data is over")
        }
    }
}