package com.xs.newsapp.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.xs.newsapp.data.firebase.FirebaseAuthRepository
import com.xs.newsapp.data.firebase.FirestoreNewsRepository
import com.xs.newsapp.data.firebase.FirestoreProfileRepository
import com.xs.newsapp.data.repository.AuthRepository
import com.xs.newsapp.data.repository.NewsRepository
import com.xs.newsapp.data.repository.ProfileRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/** `prod` flavor: Firebase. */
@Module
@InstallIn(SingletonComponent::class)
abstract class BackendModule {

    @Binds
    abstract fun bindAuthRepository(impl: FirebaseAuthRepository): AuthRepository

    @Binds
    abstract fun bindNewsRepository(impl: FirestoreNewsRepository): NewsRepository

    @Binds
    abstract fun bindProfileRepository(impl: FirestoreProfileRepository): ProfileRepository

    companion object {
        @Provides
        @Singleton
        fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

        @Provides
        @Singleton
        fun provideFirestore(): FirebaseFirestore = FirebaseFirestore.getInstance()
    }
}
