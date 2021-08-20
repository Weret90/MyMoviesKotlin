package com.umbrella.mymovieskotlin.model.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.umbrella.mymovieskotlin.model.Film

@Database(entities = [Film::class], version = 4, exportSchema = false)
abstract class MovieDatabaseRating : RoomDatabase() {

    companion object {
        private const val DB_NAME = "moviesRating.db"
        private var databaseRating: MovieDatabaseRating? = null
        private val MONITOR = Any()

        fun getInstance(context: Context): MovieDatabaseRating {
            synchronized(MONITOR) {
                if (databaseRating == null) {
                    databaseRating =
                        Room.databaseBuilder(context, MovieDatabaseRating::class.java, DB_NAME).fallbackToDestructiveMigration().build()
                }
                return databaseRating!!
            }
        }
    }

    abstract fun movieDao(): MovieDao
}