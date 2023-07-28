package com.mmdub.qofee.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.mmdub.qofee.data.room.dao.FavoriteDao
import com.mmdub.qofee.model.entity.FavoriteEntity

@Database(entities = [FavoriteEntity::class], version = 1)
abstract class QofeeDatabase : RoomDatabase() {
    abstract fun favoriteDao(): FavoriteDao
}