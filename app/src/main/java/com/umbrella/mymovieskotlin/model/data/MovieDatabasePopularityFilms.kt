package com.umbrella.mymovieskotlin.model.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.umbrella.mymovieskotlin.model.Film

@Database(entities = [Film::class], version = 4, exportSchema = false)
abstract class MovieDatabasePopularityFilms : RoomDatabase() {

    companion object {
        private const val DB_NAME = "moviesPopularity.db"
        private var databasePopularityFilms: MovieDatabasePopularityFilms? = null
        private val MONITOR = Any()
        
        fun getInstance(context: Context): MovieDatabasePopularityFilms {
            synchronized(MONITOR) {
                if (databasePopularityFilms == null) {
                    databasePopularityFilms =
                        Room.databaseBuilder(context, MovieDatabasePopularityFilms::class.java, DB_NAME).fallbackToDestructiveMigration().build()
                }
                return databasePopularityFilms!!
            }
        }
    }

    abstract fun movieDao(): MovieDao
}