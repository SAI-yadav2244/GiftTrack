package uk.ac.tees.mad.gifttrack.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import uk.ac.tees.mad.gifttrack.domain.model.Gift
import uk.ac.tees.mad.gifttrack.domain.model.GiftStatus

@Composable
fun GiftCard(
    gift: Gift,
    modifier: Modifier = Modifier,
    onGiftClick: () -> Unit
) {
    val statusColor = when (gift.status) {
        GiftStatus.GIVEN -> MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)
        GiftStatus.PLANNED -> MaterialTheme.colorScheme.secondary.copy(alpha = 0.15f)
        GiftStatus.RECEIVED -> MaterialTheme.colorScheme.tertiary.copy(alpha = 0.15f)
    }
    Card(
        shape = RoundedCornerShape(14.dp),
        modifier = modifier.padding(horizontal = 12.dp, vertical = 6.dp)
            .clickable { onGiftClick() }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(statusColor)
                .padding(12.dp)
        ) {
            AsyncImage(
                model = gift.imageUrl ?: "https://via.placeholder.com/80",
                contentDescription = null,
                modifier = Modifier.size(60.dp)
            )
            Spacer(Modifier.width(12.dp))

            Column {
                Text(gift.recipientName, style = MaterialTheme.typography.titleMedium)
                Text(gift.occasion, style = MaterialTheme.typography.bodyMedium)
                Text(gift.date, style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}