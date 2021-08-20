package com.umbrella.mymovieskotlin.model.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.umbrella.mymovieskotlin.model.Film

@Database(entities = [Film::class], version = 4, exportSchema = false)
abstract class MovieDatabaseFavouriteFilms : RoomDatabase() {

    companion object {
        private const val DB_NAME = "favouriteFilms.db"
        private var databaseFavouriteFilms: MovieDatabaseFavouriteFilms? = null
        private val MONITOR = Any()

        fun getInstance(context: Context): MovieDatabaseFavouriteFilms {
            synchronized(MONITOR) {
                if (databaseFavouriteFilms == null) {
                    databaseFavouriteFilms =
                        Room.databaseBuilder(context, MovieDatabaseFavouriteFilms::class.java, DB_NAME).fallbackToDestructiveMigration().build()
                }
                return databaseFavouriteFilms!!
            }
        }
    }

    abstract fun movieDao(): MovieDao
}