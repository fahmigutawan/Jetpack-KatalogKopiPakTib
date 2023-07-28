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

    fun getAll() = favoriteDao.getAll()

    fun getById(id:String) = favoriteDao.getById(id)

    fun insert(favoriteEntity: FavoriteEntity) = favoriteDao.insert(favoriteEntity)

    fun delete(favoriteEntity: FavoriteEntity) = favoriteDao.delete(favoriteEntity)
}