package uk.ac.tees.mad.gifttrack.ui.gift


import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState

@Composable
fun GiftListScreen(
    onAddClick: () -> Unit,
    viewModel: GiftListViewModel,
    onCalendarClick: () -> Unit,
    onProfileClick: () -> Unit
) {
    val gifts = viewModel.gifts.collectAsState()

    SwipeRefresh(
        state = rememberSwipeRefreshState(isRefreshing = false),
        onRefresh = { /* TODO later */ }
    ) {
        LazyColumn {
            items(gifts.value, key = { it.id }) { gift ->
                GiftCard(gift = gift)
            }
        }
    }
}

//    Scaffold(
//        floatingActionButton = {
//            FloatingActionButton(onClick = {
//                onAddClick()
//            }) {
//                Icon(Icons.Filled.Add, contentDescription = "Add Gift")
//            }
//        }
//    ) { padding ->
//        SwipeRefresh(
//            state = rememberSwipeRefreshState(isRefreshing = false),
//            onRefresh = { /* Firestore sync will be added later */ },
//            modifier = Modifier.padding(padding)
//        ) {
//            LazyColumn(
//                contentPadding = PaddingValues(
//                    top = padding.calculateTopPadding(),
//                    bottom = padding.calculateBottomPadding() + 100.dp
//                )
//            ) {
//                items(gifts.value, key = { it.id }) { gift ->
//                    GiftCard(gift = gift)
//                }
//            }
//        }
//    }
//}