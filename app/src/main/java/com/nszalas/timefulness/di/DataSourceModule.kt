package com.nszalas.timefulness.di

import android.app.AlarmManager
import android.content.Context
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.FirebaseAuth
import com.nszalas.timefulness.infrastructure.local.LocalNotificationDataSource
import com.nszalas.timefulness.infrastructure.local.LocalSchedulingDataSource
import com.nszalas.timefulness.infrastructure.remote.RemoteFirebaseDataSource
import com.nszalas.timefulness.utils.DateTimeProvider
import com.nszalas.timefulness.utils.TimeFormatter
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
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

    @Provides
    @Singleton
    fun provideLocalNotificationDataSource(
        @ApplicationContext context: Context,
        dateTimeProvider: DateTimeProvider,
    ) = LocalNotificationDataSource(context, dateTimeProvider)

    @Provides
    @Singleton
    fun provideLocalSchedulingDataSource(
        @ApplicationContext context: Context,
        alarmManager: AlarmManager,
        timeFormatter: TimeFormatter,
    ) = LocalSchedulingDataSource(context, alarmManager, timeFormatter)
}