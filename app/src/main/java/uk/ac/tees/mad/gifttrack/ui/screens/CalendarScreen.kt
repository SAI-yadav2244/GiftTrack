package uk.ac.tees.mad.gifttrack.ui.screens

import android.widget.CalendarView
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import uk.ac.tees.mad.gifttrack.ui.viewmodel.GiftListViewModel
import java.text.SimpleDateFormat
import java.util.*


@Composable
fun CalendarScreen(
    onBack: () -> Unit,
    giftListViewModel: GiftListViewModel,
    onAddGiftForDate: (String) -> Unit,
    onEditGiftForDate: (String) -> Unit
) {
    val context = LocalContext.current

//    val systemUi = rememberSystemUiController()
//    val color = MaterialTheme.colorScheme.primary
//
//    SideEffect {systemUi.setStatusBarColor(color)}

    val gifts by giftListViewModel.gifts.collectAsState()

    val dateFormatter = remember { SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()) }

    val scrollState = rememberScrollState()
    // Screen Content (NO SCAFFOLD)
    Column(
        modifier = Modifier
            .fillMaxSize()
//            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(scrollState)
    ) {

        // Top Bar (you'll integrate this in your main scaffold later)
        Row(
            modifier = Modifier
                .fillMaxWidth()
//                .background(MaterialTheme.colorScheme.primary)
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    tint = Color.White,
                    contentDescription = "Back"
                )
            }
            Text(
                "Calendar",
                color = Color.White,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(start = 8.dp)
            )
        }

        Text(
            text = "Tap on a date to view gifts",
            modifier = Modifier.padding(16.dp),
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Medium)
        )

        AndroidView(
            factory = { context ->
                CalendarView(context).apply {
                    date = System.currentTimeMillis()

                    setOnDateChangeListener { _, year, month, dayOfMonth ->
                        val selectedDate =
                            String.format("%04d-%02d-%02d", year, month + 1, dayOfMonth)

                        val giftsOnDate = gifts.filter { gift ->
                            try {
                                dateFormatter.format(Date(gift.date.toLong())) == selectedDate
                            } catch (e: Exception) {
                                false
                            }
                        }
//                        when {
//                            giftsOnDate.isEmpty() -> {
//                                onAddGiftForDate(selectedDate)
//                            }
//                            giftsOnDate.size == 1 -> {
//                                onEditGiftForDate(giftsOnDate.first().id)
//                            }
//                            else -> {
//                                 Toast.makeText(context, "Multiple gifts on this date.", Toast.LENGTH_SHORT).show()
//                                onEditGiftForDate(giftsOnDate.first().id)
//                            }
//                        }

                        if (giftsOnDate.isEmpty()) {
                            onAddGiftForDate(selectedDate)
                        } else {
                            Toast.makeText(
                                context,
                                "Gifts on $selectedDate:\n${giftsOnDate.joinToString("\n") { it.title }}",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(400.dp)
                .padding(16.dp)
//                .background(MaterialTheme.colorScheme.onSurface)
        )

        Spacer(Modifier.height(16.dp))

        Card(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
//            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Column(Modifier.padding(16.dp)) {
                Text(
                    "Legend:",
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                )
                Spacer(Modifier.height(4.dp))
                Text("• Dates with gifts are highlighted in your calendar color.")
                Text("• Tap on a date to view gifts saved for that occasion.")
            }
        }
    }
}

