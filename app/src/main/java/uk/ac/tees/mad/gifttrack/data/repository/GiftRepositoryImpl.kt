package uk.ac.tees.mad.gifttrack.data.repository

import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import uk.ac.tees.mad.gifttrack.data.Gift
import uk.ac.tees.mad.gifttrack.data.local.dao.GiftDao
import uk.ac.tees.mad.gifttrack.data.local.mappers.toDomain
import uk.ac.tees.mad.gifttrack.data.local.mappers.toEntity
import uk.ac.tees.mad.gifttrack.domain.repository.GiftRepository
import javax.inject.Inject

class GiftRepositoryImpl @Inject constructor(
    private val giftDao: GiftDao,
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) : GiftRepository {

    override fun getAllGifts(): Flow<List<Gift>> {
        return giftDao.getAllGifts().map { list ->
            list.map { it.toDomain() }
        }
    }

    override suspend fun insertGift(gift: Gift) {
        giftDao.insertGift(gift.toEntity())
    }

    override suspend fun deleteGift(id: String) {
        giftDao.deleteGiftById(id)
    }

    override suspend fun syncFromFirestore() {
        val uid = auth.currentUser?.uid ?: return
        val snapshot = firestore.collection("users")
            .document(uid)
            .collection("gifts")
            .get()
            .await()

        val gifts = snapshot.toObjects(Gift::class.java)
        giftDao.insertAll(gifts.map { it.toEntity() })
    }

}