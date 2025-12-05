package uk.ac.tees.mad.gifttrack.domain.repository

import android.net.Uri
import kotlinx.coroutines.flow.Flow
import uk.ac.tees.mad.gifttrack.domain.model.Gift

interface GiftRepository {
    fun getAllGifts(): Flow<List<Gift>>
    suspend fun insertGift(gift: Gift)
    suspend fun deleteGift(id: String)
    suspend fun addGiftToFirestore(gift: Gift)
    suspend fun updateGift(gift: Gift)
    suspend fun updateGiftInFirestore(gift: Gift)
    suspend fun uploadGiftImage(localUri: Uri): String
    suspend fun syncFromFirestore()
}