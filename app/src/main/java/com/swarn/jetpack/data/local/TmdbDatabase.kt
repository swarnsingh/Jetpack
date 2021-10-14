package com.swarn.jetpack.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.swarn.jetpack.model.Movie
import io.buildwithnd.demotmdb.data.GenreConverters

@Database(entities = [Movie::class], version = 1, exportSchema = false)
@TypeConverters(GenreConverters::class)
abstract class TmdbDatabase : RoomDatabase() {
    abstract fun movieDao(): MovieDao
}