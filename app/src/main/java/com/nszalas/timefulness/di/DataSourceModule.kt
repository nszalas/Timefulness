package com.nszalas.timefulness.di

import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.FirebaseAuth
import com.nszalas.timefulness.infrastructure.remote.RemoteFirebaseDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataSourceModule {

    @Provides
    @Singleton
    fun provideFirebaseDataSource(
        firebaseAuth: FirebaseAuth,
        googleSignInClient: GoogleSignInClient,
    ) = RemoteFirebaseDataSource(firebaseAuth, googleSignInClient)
}