package uk.ac.tees.mad.gifttrack.data

import androidx.compose.ui.graphics.vector.ImageVector

data class Gift (
    val id: String = "",
    val recipientName: String = "",
    val occasion: String = "",
    val imageUrl: String? = null,
    val date: String = "",
    val status: GiftStatus = GiftStatus.PLANNED
)

enum class GiftStatus {
    GIVEN,
    PLANNED,
    RECEIVED
}