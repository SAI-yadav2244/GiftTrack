package uk.ac.tees.mad.gifttrack.ui.screens


//import androidx.compose.material.pullrefresh.PullRefreshIndicator
//import androidx.compose.material.pullrefresh.pullRefresh
//import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import uk.ac.tees.mad.gifttrack.domain.model.Gift
import uk.ac.tees.mad.gifttrack.ui.components.GiftCard
import uk.ac.tees.mad.gifttrack.ui.viewmodel.GiftListViewModel


@Composable
fun GiftListScreen(
    viewModel: GiftListViewModel,
    onGiftClick: (String) -> Unit,
) {
    val gifts = viewModel.gifts.collectAsState(initial = emptyList())
    var selectedGift by remember { mutableStateOf<Gift?>(null) }

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
                GiftCard(
                    gift = gift, onGiftClick = { selectedGift = gift },
                )
            }
        }
    }
    if (selectedGift != null) {
        GiftDetailBottomSheet(
            gift = selectedGift!!,
            onEdit = { gift ->
                selectedGift = null
                onGiftClick(gift.id) // navigate to EditGift
            },
            onDelete = { gift ->
                viewModel.deleteGift(gift.id)
                selectedGift = null
            },
            onDismiss = { selectedGift = null }
        )
    }

}