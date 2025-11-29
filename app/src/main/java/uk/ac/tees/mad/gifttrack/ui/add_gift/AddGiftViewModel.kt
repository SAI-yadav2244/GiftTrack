package uk.ac.tees.mad.gifttrack.ui.add_gift

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import uk.ac.tees.mad.gifttrack.domain.model.Gift
import uk.ac.tees.mad.gifttrack.domain.model.GiftStatus
import uk.ac.tees.mad.gifttrack.domain.repository.GiftRepository
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class AddGiftViewModel @Inject constructor(
    private val giftRepository: GiftRepository
) : ViewModel() {

    private val _title = MutableStateFlow("")
    val title = _title.asStateFlow()

    private val _recipient = MutableStateFlow("")
    val recipient = _recipient.asStateFlow()

    private val _occasion = MutableStateFlow("")
    val occasion = _occasion.asStateFlow()

//    private val _description = MutableStateFlow("")
//    val description = _description.asStateFlow()

    private val _price = MutableStateFlow("")
    val price = _price.asStateFlow()

    private val _notes = MutableStateFlow("")
    val notes = _notes.asStateFlow()

    private val _status = MutableStateFlow(GiftStatus.PLANNED)
    val status = _status.asStateFlow()

    private val _imageUri = MutableStateFlow<String?>(null)
    val imageUri = _imageUri.asStateFlow()
    fun onImageCaptured(uri: String) {
        _imageUri.value = uri
    }

    private val _isSaving = MutableStateFlow(false)
    val isSaving = _isSaving.asStateFlow()

    fun onTitleChange(v: String) = run { _title.value = v }
    fun onRecipientChange(v: String) = run { _recipient.value = v }
    fun onOccasionChange(v: String) = run { _occasion.value = v }
    fun onNotesChange(v: String) = run { _notes.value = v }
    fun onPriceChange(v: String) = run { _price.value = v }
    fun onStatusChange(v: GiftStatus) = run { _status.value = v }

    fun saveGift(
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                _isSaving.value = true
                println("Starting gift save...")

                if (_title.value.isBlank() || _recipient.value.isBlank()) {
                    onError("Title and recipient are required")
                    return@launch
                }
                val giftId = UUID.randomUUID().toString()
                var imageUrl: String? = null

                _imageUri.value?.let {
                    try {
                        println("Uploading image...")
                        imageUrl = giftRepository.uploadGiftImage(Uri.parse(it))
                        println("Image uploaded: $imageUrl")
                    } catch (e: Exception) {
                        onError("Image upload failed: ${e.message}")
                        return@launch
                    }
                }

                val gift = Gift(
                    id = giftId,
                    title = _title.value,
                    recipientName = _recipient.value,
                    occasion = _occasion.value,
                    price = _price.value,
                    notes = _notes.value,
                    status = _status.value,
                    imageUrl = imageUrl,
                    date = System.currentTimeMillis().toString(),
                )
                println("Uploading gift to Firestore...")
                giftRepository.addGiftToFirestore(gift)
                println("Gift saved successfully!")
                onSuccess()
            } catch (e: Exception) {
                e.printStackTrace()
                println("Error saving gift: ${e.message}")
                onError(e.message ?: "Failed to save gift")
            } finally {
                _isSaving.value = false
                println("Done saving gift.")
            }
        }

    }


}