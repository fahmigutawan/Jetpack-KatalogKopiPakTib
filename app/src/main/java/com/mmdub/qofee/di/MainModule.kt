package com.mmdub.qofee.di

import com.mmdub.qofee.data.Repository
import com.mmdub.qofee.data.datastore.DatastoreSource
import com.mmdub.qofee.data.firebase.FirebaseSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module(
    includes = [
        DatastoreModule::class,
        FirebaseModule::class
    ]
)
@InstallIn(SingletonComponent::class)
object MainModule {
    @Provides
    @Singleton
    fun provideRepository(
        datastoreSource: DatastoreSource,
        firebaseSource: FirebaseSource
    ) = Repository(
        firebaseSource = firebaseSource,
        datastoreSource = datastoreSource
    )
}