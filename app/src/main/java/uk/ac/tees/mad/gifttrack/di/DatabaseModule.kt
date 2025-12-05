package uk.ac.tees.mad.gifttrack.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton
import uk.ac.tees.mad.gifttrack.data.local.AppDatabase
import uk.ac.tees.mad.gifttrack.data.local.dao.GiftDao

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext appContext: Context): AppDatabase {
        return Room.databaseBuilder(
            appContext,
            AppDatabase::class.java,
            "gifttrack_db"
        ).fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun provideGiftDao(db: AppDatabase): GiftDao = db.giftDao()
}