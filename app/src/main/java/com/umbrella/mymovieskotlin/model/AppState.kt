package com.umbrella.mymovieskotlin.model

sealed class AppState {
    data class Success(val response: FilmsList) : AppState()
    class Error(val throwable: Throwable) : AppState()
    object Loading : AppState()
}