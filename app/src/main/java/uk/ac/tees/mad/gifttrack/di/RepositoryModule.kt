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

//    @Provides
//    @Singleton
//    fun provideCloudinary(): Cloudinary {
//        // ⚠️ Replace with your real Cloudinary credentials
//        return Cloudinary(
//            ObjectUtils.asMap(
//                "cloud_name", "YOUR_CLOUD_NAME",
//                "api_key", "YOUR_API_KEY",
//                "api_secret", "YOUR_API_SECRET"
//            )
//        )
//    }

    @Provides
    @Singleton
    fun provideGiftRepository(
        @ApplicationContext context: Context,
        dao: GiftDao,
        auth: FirebaseAuth,
        firestore: FirebaseFirestore
    ): GiftRepository = GiftRepositoryImpl(dao, auth, firestore, context)

}