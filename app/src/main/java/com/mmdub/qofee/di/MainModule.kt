package com.mmdub.qofee.di

import com.mmdub.qofee.data.Repository
import com.mmdub.qofee.data.datastore.DatastoreSource
import com.mmdub.qofee.data.firebase.FirebaseSource
import com.mmdub.qofee.data.room.RoomSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module(
    includes = [
        DatastoreModule::class,
        FirebaseModule::class,
        RoomModule::class
    ]
)
@InstallIn(SingletonComponent::class)
object MainModule {
    @Provides
    @Singleton
    fun provideRepository(
        datastoreSource: DatastoreSource,
        firebaseSource: FirebaseSource,
        roomSource:RoomSource
    ) = Repository(
        firebaseSource = firebaseSource,
        datastoreSource = datastoreSource,
        roomSource = roomSource
    )
}