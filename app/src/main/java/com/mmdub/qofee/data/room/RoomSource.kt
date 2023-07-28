package com.mmdub.qofee.data.room

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.mmdub.qofee.model.entity.FavoriteEntity
import javax.inject.Inject

class RoomSource @Inject constructor(
    private val db:QofeeDatabase
) {
    private val favoriteDao = db.favoriteDao()

    suspend fun getAllFavorite() = favoriteDao.getAll()

    suspend fun getFavoriteById(id:String) = favoriteDao.getById(id)

    suspend fun insertFavorite(favoriteEntity: FavoriteEntity) = favoriteDao.insert(favoriteEntity)

    suspend fun deleteFavorite(favoriteEntity: FavoriteEntity) = favoriteDao.delete(favoriteEntity)
}