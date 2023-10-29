package com.example.gossip.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FirebaseModule {
    @Provides
    @Singleton
    fun providesRealtimeDb():DatabaseReference =
        Firebase.database.reference.child("userchats")

    @Provides
    @Singleton
    fun providesFirestoreDb():FirebaseFirestore = Firebase.firestore

    @Provides
    @Singleton
    fun providesFirebaseAuth():FirebaseAuth = Firebase.auth

    @Provides
    @Singleton
    fun providesFirebaseStorage():StorageReference = Firebase.storage.reference
}