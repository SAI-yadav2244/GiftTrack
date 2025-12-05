package uk.ac.tees.mad.gifttrack.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import uk.ac.tees.mad.gifttrack.data.local.converters.Converters
import uk.ac.tees.mad.gifttrack.data.local.dao.GiftDao
import uk.ac.tees.mad.gifttrack.data.local.entity.GiftEntity
import uk.ac.tees.mad.gifttrack.data.local.entity.PendingGiftEntity

@Database(
    entities = [
        GiftEntity::class,
        PendingGiftEntity::class
    ],
    version = 3,
    exportSchema = false
)

@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun giftDao(): GiftDao
}