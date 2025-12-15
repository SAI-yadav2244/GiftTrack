package uk.ac.tees.mad.gifttrack.ui.screens

import android.os.Build
import android.view.ViewGroup
import android.widget.CalendarView
import android.widget.TextView
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
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

    val gifts by giftListViewModel.gifts.collectAsState()

    val dateFormatter = remember { SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()) }

    val scrollState = rememberScrollState()

//    val backgroundColor = MaterialTheme.colorScheme.surface
//    val isDark = isSystemInDarkTheme()
//    val textColor = if (isDark) android.graphics.Color.WHITE else android.graphics.Color.BLACK

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
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
//                    setBackgroundColor(backgroundColor.toArgb())


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
                        if (giftsOnDate.isEmpty()) {
                            onAddGiftForDate(selectedDate)
                        } else {
                            onEditGiftForDate(giftsOnDate.first().id)
                            Toast.makeText(
                                context,
                                "Gifts on $selectedDate:\n${giftsOnDate.joinToString("\n") { it.title }}",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                }
            },
//           update = { calendarView ->
//                calendarView.setBackgroundColor(backgroundColor.toArgb())
//                // Reapply text color on theme switch
//                calendarView.post {
//                    try {
//                        val root = calendarView.getChildAt(0) as? ViewGroup
//                        root?.let {
//                            setCalendarTextColors(it, textColor)
//                        }
//                    } catch (_: Exception) {
//                    }
//                }
//            },
            modifier = Modifier
                .fillMaxWidth()
                .height(400.dp)
                .padding(16.dp)
                .background(Color.White)
        )

        Spacer(Modifier.height(16.dp))

        Card(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Column(Modifier.padding(16.dp)) {
                Text(
                    "Legend:",
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                )
                Spacer(Modifier.height(4.dp))
//                Text("• Dates with gifts are highlighted in your calendar color.")
                Text("• Tap on a date to view the gift saved for that occasion.")
            }
        }
    }
}
//
//private fun setCalendarTextColors(viewGroup: ViewGroup, color: Int) {
//    for (i in 0 until viewGroup.childCount) {
//        val child = viewGroup.getChildAt(i)
//        when (child) {
//            is TextView -> child.setTextColor(color)
//            is ViewGroup -> setCalendarTextColors(child, color)
//        }
//    }
//}

