package uk.ac.tees.mad.gifttrack.domain.model

data class Gift (
    val id: String = "",
    val title: String = "",
    val recipientName: String = "",
    val occasion: String = "",
    val price: String = "",
    val notes: String = "",
    val imageUrl: String? = null,
    val date: String = "",
    val status: GiftStatus = GiftStatus.PLANNED
)

enum class GiftStatus {
    GIVEN,
    PLANNED,
    RECEIVED
}