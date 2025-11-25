package uk.ac.tees.mad.gifttrack.ui.add_gift

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

class AddGiftViewModel @Inject constructor(): ViewModel() {
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

    private val _status = MutableStateFlow("Planned")
    val status = _status.asStateFlow()

    private val _imageUri = MutableStateFlow<String?>(null)
    val imageUri = _imageUri.asStateFlow()
    fun onImageCaptured(uri: String) { _imageUri.value = uri }


    fun onTitleChange(v: String) = run { _title.value = v }
    fun onRecipientChange(v: String) = run { _recipient.value = v }
    fun onOccasionChange(v: String) = run { _occasion.value = v }
    fun onNotesChange(v: String) = run { _notes.value = v }
    fun onPriceChange(v: String) = run { _price.value = v }
    fun onStatusChange(v: String) = run { _status.value = v }


}