package uk.ac.tees.mad.gifttrack.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import uk.ac.tees.mad.gifttrack.domain.model.Gift

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GiftDetailBottomSheet(
    gift: Gift,
    onEdit: (Gift) -> Unit,
    onDelete: (Gift) -> Unit,
    onDismiss: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = MaterialTheme.colorScheme.surface
    ) {
        Column(
            modifier = Modifier
                .padding(24.dp)
                .fillMaxWidth()
        ) {

            Text(
                text = gift.title,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.height(8.dp))

            Text("Recipient: ${gift.recipientName}")
            Text("Occasion: ${gift.occasion}")
            Text("Price: ₹${gift.price}")
            Text("Notes: ${gift.notes}")

            Spacer(Modifier.height(16.dp))

            if (gift.imageUrl != null) {
                AsyncImage(
                    model = gift.imageUrl,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                )
                Spacer(Modifier.height(16.dp))
            }

            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = { onEdit(gift) }
            ) {
                Text("Edit Gift")
            }

            Spacer(Modifier.height(8.dp))

            OutlinedButton(
                modifier = Modifier.fillMaxWidth(),
                onClick = { onDelete(gift) }
            ) {
                Text("Delete Gift")
            }
        }
    }
}
