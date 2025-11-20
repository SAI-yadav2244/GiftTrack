package uk.ac.tees.mad.gifttrack.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "gifts")
data class GiftEntity(
    @PrimaryKey val id: String,
    @ColumnInfo(name = "recipient_name") val recipientName: String,
    @ColumnInfo(name = "occasion") val occasion: String,
    @ColumnInfo(name = "image_url") val imageUrl: String?,
    @ColumnInfo(name = "date") val date: String,
    @ColumnInfo(name = "status") val status: Int
)
