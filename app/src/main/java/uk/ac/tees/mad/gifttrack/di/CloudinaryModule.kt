package uk.ac.tees.mad.gifttrack.di

import com.cloudinary.Cloudinary
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CloudinaryModule {

    @Provides
    @Singleton
    fun provideCloudinary(): Cloudinary {
        return Cloudinary(
            mapOf(
                "cloud_name" to "dmgwqrc5b"
            )
        )
    }
}