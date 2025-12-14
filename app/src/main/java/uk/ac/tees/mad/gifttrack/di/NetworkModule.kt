package uk.ac.tees.mad.gifttrack.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import uk.ac.tees.mad.gifttrack.data.remote.EtsyApiService
import uk.ac.tees.mad.gifttrack.data.remote.EtsyApi
import uk.ac.tees.mad.gifttrack.data.remote.EtsyRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit = Retrofit.Builder()
        .baseUrl("https://openapi.etsy.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @Provides
    @Singleton
    fun provideEtsyApiService(retrofit: Retrofit): EtsyApiService =
        retrofit.create(EtsyApiService::class.java)

    @Provides
    @Singleton
    fun provideEtsyApi(service: EtsyApiService): EtsyApi = EtsyApi(service)

    @Provides
    @Singleton
    fun provideEtsyRepository(api: EtsyApi): EtsyRepository = EtsyRepository(api)
}
