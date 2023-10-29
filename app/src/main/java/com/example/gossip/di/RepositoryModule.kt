package com.example.gossip.di

import com.example.gossip.repository.realtimedb.RealtimeDbRepository
import com.example.gossip.repository.realtimedb.RealtimeRepository
import com.example.gossip.repository.auth.AuthRepository
import com.example.gossip.firebaseauth.repository.AuthRepositoryImpl
import com.example.gossip.repository.firestoredb.FirestoreDbRepositoryImpl
import com.example.gossip.repository.firestoredb.FirestoreRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class RepositoryModule {
    @Binds
    abstract fun providesRealtimeRepository(
        repo: RealtimeDbRepository
    ): RealtimeRepository

    @Binds
    abstract fun providesFirestoreRepository(
        repo: FirestoreDbRepositoryImpl
    ): FirestoreRepository

    @Binds
    abstract fun providesFirebaseAuthRepository(
        repo: AuthRepositoryImpl
    ): AuthRepository
}