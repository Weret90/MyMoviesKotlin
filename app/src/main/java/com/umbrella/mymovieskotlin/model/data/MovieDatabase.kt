package com.umbrella.mymovieskotlin.model.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.umbrella.mymovieskotlin.model.Film

@Database(entities = [Film::class], version = 1, exportSchema = false)
abstract class MovieDatabase : RoomDatabase() {

    companion object {
        private const val DB_NAME = "movies.db"
        private var database: MovieDatabase? = null
        private val MONITOR = Any()
        fun getInstance(context: Context): MovieDatabase {
            synchronized(MONITOR) {
                if (database == null) {
                    database =
                        Room.databaseBuilder(context, MovieDatabase::class.java, DB_NAME).build()
                }
            }
            return database!!
        }
    }

    abstract fun movieDao(): MovieDao
}