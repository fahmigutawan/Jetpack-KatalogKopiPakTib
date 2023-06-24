package com.mmdub.katalogkopipaktib.di

import com.mmdub.katalogkopipaktib.data.firebase.FirebaseSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class FirebaseModule {
    @Provides
    @Singleton
    fun provideFirebaseSource() = FirebaseSource()
}