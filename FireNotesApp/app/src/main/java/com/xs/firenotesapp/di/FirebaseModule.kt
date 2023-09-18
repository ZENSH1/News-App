package com.xs.firenotesapp.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.xs.firenotesapp.data.auth.AuthRepository
import com.xs.firenotesapp.data.auth.AuthRepositoryImpl
import com.xs.firenotesapp.data.news.NewsDatabase
import com.xs.firenotesapp.data.news.NewsRepository
import com.xs.firenotesapp.data.news.NewsRepositoryImpl
import com.xs.firenotesapp.data.storage.StorageDatabase
import com.xs.firenotesapp.data.storage.StorageRepository
import com.xs.firenotesapp.data.storage.StorageRepositoryImpl
import com.xs.firenotesapp.data.users.UsersDatabase
import com.xs.firenotesapp.data.users.UsersRepository
import com.xs.firenotesapp.data.users.UsersRepositoryImpl
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
    fun providesFirebaseAuth():FirebaseAuth = FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun providesFirebaseStorage():FirebaseStorage = FirebaseStorage.getInstance()

    @Provides
    @Singleton
    fun providesStorageRepository(storageDatabase: StorageDatabase):StorageRepository = StorageRepositoryImpl(storageDatabase)

    @Provides
    @Singleton
    fun providesCurrentUser(mAuth: FirebaseAuth):FirebaseUser? = mAuth.currentUser

    @Provides
    @Singleton
    fun providesAuthRepository(impl: AuthRepositoryImpl): AuthRepository = impl

    @Provides
    @Singleton
    fun providesFirebaseFireStore():FirebaseFirestore = FirebaseFirestore.getInstance()

    @Provides
    @Singleton
    fun providesNewsFirestoreReference(firestore: FirebaseFirestore):CollectionReference = firestore.collection("News")

    @Provides
    @Singleton
    fun providesNewsRepository(newsDB : NewsDatabase): NewsRepository = NewsRepositoryImpl(newsDB)

    @Provides
    @Singleton
    fun providesUserRepository(usersDB:UsersDatabase):UsersRepository = UsersRepositoryImpl(usersDB)
}