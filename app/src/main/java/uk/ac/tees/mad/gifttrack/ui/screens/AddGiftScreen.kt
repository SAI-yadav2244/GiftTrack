package uk.ac.tees.mad.gifttrack.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import uk.ac.tees.mad.gifttrack.data.remote.EtsyViewModel
import uk.ac.tees.mad.gifttrack.ui.components.GiftStatusDropdown
import uk.ac.tees.mad.gifttrack.ui.viewmodel.AddGiftViewModel
import uk.ac.tees.mad.gifttrack.ui.components.AppTextField
import uk.ac.tees.mad.gifttrack.ui.components.ProfileHeader

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddGiftScreen(
    onBack: () -> Unit,
    onSave: () -> Unit,
    onNavigateToCamera: () -> Unit,
    viewModel: AddGiftViewModel,
    etsyViewModel: EtsyViewModel
) {
    val imageUri by viewModel.imageUri.collectAsState()
    val title by viewModel.title.collectAsState()
    val recipient by viewModel.recipient.collectAsState()
    val occasion by viewModel.occasion.collectAsState()
    val price by viewModel.price.collectAsState()
    val notes by viewModel.notes.collectAsState()
    val status by viewModel.status.collectAsState()

    var statusMenuExpanded by remember { mutableStateOf(false) }

    val scrollState = rememberScrollState()

    val isSaving by viewModel.isSaving.collectAsState()

    val suggestions by etsyViewModel.suggestedGifts.collectAsState()

    LaunchedEffect(Unit) {
        etsyViewModel.loadTrendingGifts("gifts for ${recipient.ifBlank { "friends" }}")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(20.dp)
            .systemBarsPadding(),
//            .imePadding()
//            .navigationBarsPadding(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        ProfileHeader("Add Gift", onBackClick = onBack)
        if (imageUri != null) {
            Image(
                painter = rememberAsyncImagePainter(imageUri),
                contentDescription = "Gift Image",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            )
        }

        Button(
            onClick = { onNavigateToCamera() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Take Photo")
        }

        AppTextField(
            value = title,
            onValueChange = viewModel::onTitleChange,
            placeholder = "Title",
            modifier = Modifier.fillMaxWidth(),
        )

        AppTextField(
            value = recipient,
            onValueChange = viewModel::onRecipientChange,
            placeholder = "Recipient",
            modifier = Modifier.fillMaxWidth(),
        )

        AppTextField(
            value = occasion,
            onValueChange = viewModel::onOccasionChange,
            placeholder = "Occasion",
            modifier = Modifier.fillMaxWidth(),
        )

        AppTextField(
            value = price,
            onValueChange = viewModel::onPriceChange,
            placeholder = "Price",
            modifier = Modifier.fillMaxWidth(),
            keyboardType = KeyboardType.Number,
        )

        AppTextField(
            value = notes,
            onValueChange = viewModel::onNotesChange,
            placeholder = "Notes",
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp),
        )

        // Status Dropdown
        GiftStatusDropdown(
            status = status,
            onStatusChange = viewModel::onStatusChange
        )

        Spacer(Modifier.height(20.dp))

        Button(
            onClick = {
                viewModel.saveGift(
                    onSuccess = {
                        println("Gift saved successfully")
                        onSave()
                    },
                    onError = { error ->
                        //TODO: toast
                        println("Error saving gift: $error")
                        onSave() // navigate back even on failure
                    }
                )
            },
            enabled = !isSaving,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (isSaving) "Saving..." else "Save Gift")
        }
        if (suggestions.isNotEmpty()) {
            Text("Suggested Gifts:")
            suggestions.take(3).forEach {
                Text("• ${it.title}")
            }
        }
    }
}