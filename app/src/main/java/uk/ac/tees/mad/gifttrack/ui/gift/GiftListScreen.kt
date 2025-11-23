package uk.ac.tees.mad.gifttrack.ui.gift


//import androidx.compose.material.pullrefresh.PullRefreshIndicator
//import androidx.compose.material.pullrefresh.pullRefresh
//import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier


@Composable
fun GiftListScreen(
    onAddClick: () -> Unit,
    viewModel: GiftListViewModel,
    onCalendarClick: () -> Unit,
    onProfileClick: () -> Unit
) {
    val gifts = viewModel.gifts.collectAsState(initial = emptyList())
//    val refreshing = viewModel.refreshing.collectAsState()
//    val pullRefreshState = rememberPullRefreshState(
//        refreshing = refreshing.value,
//        onRefresh = { viewModel.refresh() }
//    )

    Box(
        modifier = Modifier.fillMaxSize()
//            .pullRefresh(pullRefreshState)
    ) {
        LazyColumn {
            items(gifts.value) { gift ->
                GiftCard(gift = gift)
            }
        }
//
//        PullRefreshIndicator(
//            refreshing = refreshing.value,
//            state = pullRefreshState,
//            modifier = Modifier.align(Alignment.TopCenter)
//        )
    }

}

//    SwipeRefresh(
//        state = rememberSwipeRefreshState(isRefreshing = false),
//        onRefresh = { /* TODO later */ }
//    ) {
//        LazyColumn {
//            items(gifts.value, key = { it.id }) { gift ->
//                GiftCard(gift = gift)
//            }
//        }
//    }
//}

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