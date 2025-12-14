package uk.ac.tees.mad.gifttrack.di

import android.content.Context
//import com.cloudinary.Cloudinary
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import uk.ac.tees.mad.gifttrack.data.local.dao.GiftDao
import uk.ac.tees.mad.gifttrack.data.repository.GiftRepositoryImpl
import uk.ac.tees.mad.gifttrack.domain.repository.GiftRepository
import javax.inject.Singleton

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