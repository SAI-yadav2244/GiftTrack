package uk.ac.tees.mad.gifttrack.ui.gift

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import uk.ac.tees.mad.gifttrack.data.Gift
import uk.ac.tees.mad.gifttrack.data.GiftStatus
import uk.ac.tees.mad.gifttrack.domain.repository.GiftRepository
import javax.inject.Inject

@HiltViewModel
class GiftListViewModel @Inject constructor(
    private val giftRepository: GiftRepository
): ViewModel() {

    val gifts = giftRepository.getAllGifts()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    fun deleteGift(id: String) {
        viewModelScope.launch { giftRepository.deleteGift(id) }
    }

    private val _refreshing = MutableStateFlow(false)
    val refreshing = _refreshing.asStateFlow()

    fun refresh() {
        viewModelScope.launch {
            _refreshing.value = true
            giftRepository.syncFromFirestore()
            _refreshing.value = false
        }
    }

//    private val _gifts = MutableStateFlow(
//        listOf(
//            Gift("1", "Mom", "Birthday", null, "2025-01-01", GiftStatus.GIVEN),
//            Gift("2", "Arjun", "Wedding", null, "2025-02-14", GiftStatus.PLANNED),
//            Gift("3", "Sister", "Graduation", null, "2025-03-08", GiftStatus.RECEIVED)
//        )
//    )
//    val gifts: StateFlow<List<Gift>> = _gifts
}