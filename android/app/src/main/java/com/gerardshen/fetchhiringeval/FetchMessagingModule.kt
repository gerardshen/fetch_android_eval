package com.gerardshen.fetchhiringeval

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Hilt module to provide the messaging service.
 */
@Module
@InstallIn(SingletonComponent::class)
class FetchMessagingModule {
    @Provides
    @Singleton
    fun provideFetchMessagingService(): FetchMessagingService {
        return FetchMessagingService()
    }
}