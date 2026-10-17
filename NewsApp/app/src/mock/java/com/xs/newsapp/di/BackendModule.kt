package com.xs.newsapp.di

import com.xs.newsapp.data.fake.FakeAuthRepository
import com.xs.newsapp.data.fake.FakeNewsRepository
import com.xs.newsapp.data.fake.FakeProfileRepository
import com.xs.newsapp.data.repository.AuthRepository
import com.xs.newsapp.data.repository.NewsRepository
import com.xs.newsapp.data.repository.ProfileRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

/** `mock` flavor: the stores talk to an in-memory backend. Swap the flavor, not the code. */
@Module
@InstallIn(SingletonComponent::class)
abstract class BackendModule {

    @Binds
    abstract fun bindAuthRepository(impl: FakeAuthRepository): AuthRepository

    @Binds
    abstract fun bindNewsRepository(impl: FakeNewsRepository): NewsRepository

    @Binds
    abstract fun bindProfileRepository(impl: FakeProfileRepository): ProfileRepository
}
