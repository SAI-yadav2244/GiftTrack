package uk.ac.tees.mad.gifttrack.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pending_gifts")
data class PendingGiftEntity(
    @PrimaryKey(autoGenerate = true) val localId: Int = 0,
    val title: String,
    val recipientName: String,
    val occasion: String,
    val price: String,
    val notes: String,
    val imageUri: String?, // local image path
    val date: String,
    val status: Int,
)