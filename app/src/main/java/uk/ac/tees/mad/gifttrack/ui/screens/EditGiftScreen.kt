package uk.ac.tees.mad.gifttrack.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import uk.ac.tees.mad.gifttrack.domain.model.Gift
import uk.ac.tees.mad.gifttrack.domain.model.GiftStatus
import uk.ac.tees.mad.gifttrack.ui.viewmodel.AddGiftViewModel

@Composable
fun EditGiftScreen(
    gift: Gift,
    viewModel: AddGiftViewModel,
    onBack: () -> Unit
) {
    var title by remember { mutableStateOf(gift.title) }
    var recipient by remember { mutableStateOf(gift.recipientName) }
    var occasion by remember { mutableStateOf(gift.occasion) }
    var price by remember { mutableStateOf(gift.price) }
    var notes by remember { mutableStateOf(gift.notes) }
    var status by remember { mutableStateOf(gift.status) }

    val context = LocalContext.current

    Column(Modifier.padding(16.dp)) {
        Text("Edit Gift", style = MaterialTheme.typography.titleLarge)
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(value = title, onValueChange = { title = it }, label = { Text("Title") })
        OutlinedTextField(value = recipient, onValueChange = { recipient = it }, label = { Text("Recipient") })
        OutlinedTextField(value = occasion, onValueChange = { occasion = it }, label = { Text("Occasion") })
        OutlinedTextField(value = price, onValueChange = { price = it }, label = { Text("Price") })
        OutlinedTextField(value = notes, onValueChange = { notes = it }, label = { Text("Notes") })

        Spacer(Modifier.height(8.dp))
        var expanded by remember { mutableStateOf(false) }

        Box {
            OutlinedButton(onClick = { expanded = true }) {
                Text("Status: ${status.name}")
            }
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                GiftStatus.values().forEach { s ->
                    DropdownMenuItem(
                        text = { Text(s.name) },
                        onClick = {
                            status = s
                            expanded = false
                        }
                    )
                }
            }
        }

        Spacer(Modifier.height(16.dp))
        Button(onClick = {
            val updatedGift = gift.copy(
                title = title,
                recipientName = recipient,
                occasion = occasion,
                price = price,
                notes = notes,
                status = status
            )
            viewModel.updateGift(
                updatedGift,
                onSuccess = onBack,
                onError = { Toast.makeText(context, it, Toast.LENGTH_SHORT).show() }
            )
        }) {
            Text("Save Changes")
        }
    }
}
