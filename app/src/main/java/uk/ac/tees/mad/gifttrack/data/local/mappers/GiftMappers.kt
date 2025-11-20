package uk.ac.tees.mad.gifttrack.data.local.mappers

import uk.ac.tees.mad.gifttrack.data.Gift
import uk.ac.tees.mad.gifttrack.data.GiftStatus
import uk.ac.tees.mad.gifttrack.data.local.entity.GiftEntity

fun GiftEntity.toDomain(): Gift =
    Gift(
        id = id,
        recipientName = recipientName,
        occasion = occasion,
        imageUrl = imageUrl,
        date = date,
        status = when (status) {
            0 -> GiftStatus.GIVEN
            1 -> GiftStatus.PLANNED
            2 -> GiftStatus.RECEIVED
            else -> GiftStatus.PLANNED
        }
    )

fun Gift.toEntity(): GiftEntity =
    GiftEntity(
        id = id,
        recipientName = recipientName,
        occasion = occasion,
        imageUrl = imageUrl,
        date = date,
        status = when (status) {
            GiftStatus.GIVEN -> 0
            GiftStatus.PLANNED -> 1
            GiftStatus.RECEIVED -> 2
        }
    )