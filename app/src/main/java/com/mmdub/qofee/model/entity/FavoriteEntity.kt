package com.mmdub.qofee.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("favorite")
data class FavoriteEntity(
    @PrimaryKey val id:String,
    val name:String?,
    val category:String?,
    val thumbnail:String?
)
