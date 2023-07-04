package com.mmdub.qofee.di

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.mmdub.qofee.data.firebase.FirebaseSource
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
    fun provideFirebaseSource(
        firestore:FirebaseFirestore
    ) = FirebaseSource(
        firestore = firestore
    )

    @Provides
    @Singleton
    fun provideFirebaseFirestore() = Firebase.firestore
}