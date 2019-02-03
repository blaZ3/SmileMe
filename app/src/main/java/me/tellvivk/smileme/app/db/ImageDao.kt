package me.tellvivk.smileme.app.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import me.tellvivk.smileme.app.model.Image

@Dao
interface ImageDao {

    @Query("SELECT * FROM image")
    fun getAll(): List<Image>

    @Query("SELECT * FROM image where id = :id")
    fun getById(id: String): Image

    @Insert
    fun insertAll(vararg images: Image)

    @Delete
    fun delete(image: Image)
}