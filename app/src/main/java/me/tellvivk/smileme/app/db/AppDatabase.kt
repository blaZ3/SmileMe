package me.tellvivk.smileme.app.db

import androidx.room.Database
import androidx.room.RoomDatabase
import me.tellvivk.smileme.app.model.Image

@Database(entities = [Image::class], version = 2, exportSchema = false)
abstract class AppDatabase: RoomDatabase() {
    abstract fun getImagesDao(): ImageDao
}