package uk.ac.tees.mad.gifttrack.data.remote

import retrofit2.http.GET
import retrofit2.http.Query

interface EtsyApiService {
    @GET("v2/listings/active")
    suspend fun getTrendingGifts(
        @Query("api_key") apiKey: String = "jhs2877tzi2oq57uant2ejy6",
        @Query("limit") limit: Int = 5,
        @Query("includes") includes: String = "Images",
        @Query("sort_on") sortOn: String = "score",
        @Query("keywords") keywords: String = "gift"
    ): EtsyResponse
}