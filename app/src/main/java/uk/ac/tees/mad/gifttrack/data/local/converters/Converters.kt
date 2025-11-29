package uk.ac.tees.mad.gifttrack.data.local.converters

import androidx.room.TypeConverter
import uk.ac.tees.mad.gifttrack.domain.model.GiftStatus

class Converters {
    @TypeConverter
    fun fromStatusInt(value: Int): GiftStatus = when (value) {
        0 -> GiftStatus.GIVEN
        1 -> GiftStatus.PLANNED
        2 -> GiftStatus.RECEIVED
        else -> GiftStatus.PLANNED
    }
    @TypeConverter
    fun toStatusInt(status: GiftStatus): Int = when (status) {
        GiftStatus.GIVEN -> 0
        GiftStatus.PLANNED -> 1
        GiftStatus.RECEIVED -> 2
    }
}