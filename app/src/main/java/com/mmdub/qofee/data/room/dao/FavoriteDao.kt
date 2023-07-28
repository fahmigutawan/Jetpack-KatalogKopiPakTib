package com.mmdub.qofee.data.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.mmdub.qofee.model.entity.FavoriteEntity

@Dao
interface FavoriteDao {
    @Query("SELECT * FROM favorite")
    fun getAll(): List<FavoriteEntity>

    @Query("SELECT * FROM favorite WHERE id=(:id)")
    fun getById(id:String):List<FavoriteEntity>

    @Insert
    fun insert(favoriteEntity: FavoriteEntity)

    @Delete
    fun delete(favoriteEntity: FavoriteEntity)
}