package uk.ac.tees.mad.gifttrack.domain.repository

import kotlinx.coroutines.flow.Flow
import uk.ac.tees.mad.gifttrack.data.Gift

interface GiftRepository {
    fun getAllGifts(): Flow<List<Gift>>
    suspend fun insertGift(gift: Gift)
    suspend fun deleteGift(id: String)
    suspend fun syncFromFirestore()
}