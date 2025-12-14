package uk.ac.tees.mad.gifttrack.data.remote

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import uk.ac.tees.mad.gifttrack.domain.repository.GiftRepository
import javax.inject.Inject

@HiltViewModel
class EtsyViewModel @Inject constructor(
    private val repository: EtsyRepository
) : ViewModel() {

    private val _suggestedGifts = MutableStateFlow<List<EtsyListing>>(emptyList())
    val suggestedGifts = _suggestedGifts.asStateFlow()

    fun loadTrendingGifts(keyword: String = "gift") {
        viewModelScope.launch {
//            viewModelScope.launch {
//                _suggestedGifts.value = repository.getTrendingGifts(keyword)
//            }
            try {
                _suggestedGifts.value = repository.getTrendingGifts(keyword)
            } catch (e: Exception) {
                e.printStackTrace()
                _suggestedGifts.value = emptyList()
            }
        }
    }
}
