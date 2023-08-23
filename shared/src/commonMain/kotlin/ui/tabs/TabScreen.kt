package ui.tabs

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import ui.home.ProfileUiState

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TabScreen(
    modifier: Modifier = Modifier,
    state: ProfileUiState,
    hasMovedToLeft: () -> Unit = {},
    hasMovedToRight: () -> Unit = {},
    onScroll: (Int) -> Unit = {},
    animatedMainRotate: Float
) {
    val textTabs = listOf("Cursos em progresso", "Guias de estudo")
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
            if (page == 0) {
                CourseProgressesScreen(
                    state = state.profile.courseProgresses,
                    animatedMainRotate = animatedMainRotate,
                    onScroll = {
                        onScroll(it)
                    }
                )
            } else {
                GuidesScreen(
                    state = state.profile.guides,
                    onScroll = {
                        onScroll(it)
                    }
                )
            }
        }
    }
}


