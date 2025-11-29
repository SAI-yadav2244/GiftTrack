package uk.ac.tees.mad.gifttrack.di

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton
import uk.ac.tees.mad.gifttrack.data.local.dao.GiftDao
import uk.ac.tees.mad.gifttrack.data.repository.GiftRepositoryImpl
import uk.ac.tees.mad.gifttrack.domain.repository.GiftRepository

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Provides
    @Singleton
    fun provideGiftRepository(
        @ApplicationContext context: Context,
        dao: GiftDao,
        auth: FirebaseAuth,
        firestore: FirebaseFirestore
    ): GiftRepository = GiftRepositoryImpl(dao, auth, firestore, context)

}