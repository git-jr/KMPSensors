import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TabScreen(
    modifier: Modifier = Modifier,
    hasMovedToLeft: () -> Unit = {},
    hasMovedToRight: () -> Unit = {}
) {
    val textTabs = listOf("Page 1", "Page 2")
    val pagerState = rememberPagerState()

    LaunchedEffect(pagerState.currentPage) {
        when (pagerState.currentPage) {
            0 -> hasMovedToLeft()
            1 -> hasMovedToRight()
        }
    }



    HorizontalPager(state = pagerState, pageCount = textTabs.size) { page ->
        Box(
            modifier = modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = textTabs[page],
                textAlign = TextAlign.Center,
            )
        }
    }

}