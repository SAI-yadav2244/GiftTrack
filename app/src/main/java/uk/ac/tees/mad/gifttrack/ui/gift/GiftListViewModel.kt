package uk.ac.tees.mad.gifttrack.ui.gift

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import uk.ac.tees.mad.gifttrack.data.Gift
import uk.ac.tees.mad.gifttrack.data.GiftStatus

class GiftListViewModel: ViewModel() {
    private val _gifts = MutableStateFlow(
        listOf(
            Gift("1", "Mom", "Birthday", null, "2025-01-01", GiftStatus.GIVEN),
            Gift("2", "Arjun", "Wedding", null, "2025-02-14", GiftStatus.PLANNED),
            Gift("3", "Sister", "Graduation", null, "2025-03-08", GiftStatus.RECEIVED)
        )
    )
    val gifts: StateFlow<List<Gift>> = _gifts
}