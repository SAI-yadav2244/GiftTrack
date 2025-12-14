package uk.ac.tees.mad.gifttrack.data.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EtsyApi @Inject constructor(
    val service: EtsyApiService
){
    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://openapi.etsy.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}
