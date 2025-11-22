package uk.ac.tees.mad.gifttrack.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import uk.ac.tees.mad.gifttrack.data.Gift
import uk.ac.tees.mad.gifttrack.data.local.dao.GiftDao
import uk.ac.tees.mad.gifttrack.data.local.mappers.toDomain
import uk.ac.tees.mad.gifttrack.data.local.mappers.toEntity
import uk.ac.tees.mad.gifttrack.domain.repository.GiftRepository
import javax.inject.Inject

class GiftRepositoryImpl @Inject constructor(
    private val giftDao: GiftDao
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
}