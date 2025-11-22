package uk.ac.tees.mad.gifttrack.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import uk.ac.tees.mad.gifttrack.data.repository.GiftRepositoryImpl
import uk.ac.tees.mad.gifttrack.domain.repository.GiftRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindGiftRepository(
        giftRepositoryImpl: GiftRepositoryImpl
    ): GiftRepository
}