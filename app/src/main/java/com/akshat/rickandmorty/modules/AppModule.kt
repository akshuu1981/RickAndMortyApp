package com.akshat.rickandmorty.modules

import com.akshat.network.KtorClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class AppModule{

    @Provides
    fun providesKtorClient(): KtorClient{
        return KtorClient()

    }
}