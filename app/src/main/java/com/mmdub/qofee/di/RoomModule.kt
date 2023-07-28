package com.mmdub.qofee.di

import android.content.Context
import androidx.room.Room
import com.mmdub.qofee.data.room.QofeeDatabase
import com.mmdub.qofee.data.room.RoomSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RoomModule {
    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context:Context
    ) = Room.databaseBuilder(
        context,
        QofeeDatabase::class.java, "database-name"
    ).build()

    @Provides
    @Singleton
    fun provideRoomSource(
        db:QofeeDatabase
    ) = RoomSource(
        db = db
    )
}