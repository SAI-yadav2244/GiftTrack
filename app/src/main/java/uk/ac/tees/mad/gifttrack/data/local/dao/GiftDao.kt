package uk.ac.tees.mad.gifttrack.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import uk.ac.tees.mad.gifttrack.data.local.entity.GiftEntity
import uk.ac.tees.mad.gifttrack.data.local.entity.PendingGiftEntity

@Dao
interface GiftDao {

    @Query("SELECT * FROM gifts ORDER BY date ASC")
    fun getAllGifts(): Flow<List<GiftEntity>>

    @Query("SELECT * FROM gifts WHERE id = :id LIMIT 1")
    suspend fun getGiftById(id: String): GiftEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGift(gift: GiftEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(gifts: List<GiftEntity>)

    @Update
    suspend fun updateGift(gift: GiftEntity)

    @Query("DELETE FROM gifts WHERE id = :id")
    suspend fun deleteGiftById(id: String)

    @Query("DELETE FROM gifts")
    suspend fun clearAll()

    @Insert
    suspend fun insertPendingGift(gift: PendingGiftEntity)

    @Query("SELECT * FROM pending_gifts")
    suspend fun getAllPendingGifts(): List<PendingGiftEntity>

    @Query("DELETE FROM pending_gifts WHERE localId = :id")
    suspend fun deletePendingGift(id: Int)
}