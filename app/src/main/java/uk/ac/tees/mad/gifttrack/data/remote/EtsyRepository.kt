package uk.ac.tees.mad.gifttrack.data.remote

import javax.inject.Inject

class EtsyRepository @Inject constructor(
    private val api: EtsyApi
) {
    suspend fun getTrendingGifts(keyword: String): List<EtsyListing> {
        return try {
            api.service.getTrendingGifts(keywords = keyword).results
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }
}