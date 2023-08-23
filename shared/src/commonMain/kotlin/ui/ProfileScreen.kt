package ui

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

var horizontalPagerInAction by mutableStateOf(false)
var valueToBeChangedOnTheLeft by mutableStateOf(3f)
var valueToBeChangedOnTheRight by mutableStateOf(3f)
var rotate by mutableStateOf(0f)


@Composable
fun HomeScreen(viewModel: ViewModel) {
    val state by viewModel.profileUiState.collectAsState()
    val scope = rememberCoroutineScope()

    Box(
        Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.BottomCenter
    ) {
        var offsetWave by remember { mutableStateOf(0f) }

        LaunchedEffect(offsetWave) {
            // using "when" instead of "if" because  can be util to add more animations
            when (offsetWave) {
                0f -> {
                    // when offset is 0 (list is in its ideal point), give a little move on the wave
                    rotate += 2
                    delay(800)
                    rotate = 0f
                }
            }
        }

        Wave(
            modifier = Modifier
                .alpha(0.5f)
                .offset(y = -(offsetWave.dp) / 10) // smooth slide with paralax effect on the wave
        )

        TabScreen(
            modifier = Modifier,
            state = state,
            onScroll = {
                offsetWave = it.toFloat()
            },
            hasMovedToLeft = {
                horizontalPagerInAction = true
                scope.launch {
                    val originalValeuLeft = valueToBeChangedOnTheLeft
                    val originalValeuRight = valueToBeChangedOnTheRight
                    val targetValeuLeft = originalValeuLeft * 4f
                    val targetValeuRight = originalValeuRight / 2f

                    valueToBeChangedOnTheLeft = targetValeuLeft
                    valueToBeChangedOnTheRight = targetValeuRight

                    delay(700)
                    valueToBeChangedOnTheLeft = originalValeuLeft
                    valueToBeChangedOnTheRight = originalValeuRight
                    delay(700)
                    horizontalPagerInAction = false
                }
            },
            hasMovedToRight = {
                horizontalPagerInAction = true
                scope.launch {
                    val originalValeuLeft = valueToBeChangedOnTheLeft
                    val targetValeuLeft = originalValeuLeft / 2f

                    val originalValeuRight = valueToBeChangedOnTheRight
                    val targetValeuRight = originalValeuRight * 4f

                    valueToBeChangedOnTheLeft = targetValeuLeft
                    valueToBeChangedOnTheRight = targetValeuRight
                    delay(700)
                    valueToBeChangedOnTheLeft = originalValeuLeft
                    valueToBeChangedOnTheRight = originalValeuRight
                    delay(700)
                    horizontalPagerInAction = false
                }
            }
        )
    }
}

@Composable
fun Wave(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom
    ) {
        Box(
            modifier = Modifier
                .weight(2f)
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            var width: Float
            var height = 1080f

            val animatedOffset by animateFloatAsState(
                targetValue = rotate,
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioHighBouncy,
                    stiffness = Spring.StiffnessVeryLow
                )
            )

            val startHeight by animateFloatAsState(
                targetValue = height / valueToBeChangedOnTheLeft,
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioHighBouncy,
                    stiffness = Spring.StiffnessVeryLow
                )
            )

            val endHeight by animateFloatAsState(
                targetValue = height / valueToBeChangedOnTheRight,
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioHighBouncy,
                    stiffness = Spring.StiffnessVeryLow
                )
            )

            Canvas(
                modifier = Modifier
                    .size(800.dp)
                    .rotate(animatedOffset)
            ) {
                width = size.width
                height = size.height

                val peakHeight = height * 0.2f
                val dipHeight = height * 0.2f

                val path = Path().apply {
                    moveTo(-50f, startHeight + 100)
                    cubicTo(
                        width * 0.33f, peakHeight,
                        width * 0.66f, dipHeight,
                        width + 50, endHeight + 100
                    )
                    lineTo(
                        width + 50,
                        height + 1000
                    )
                    lineTo(
                        -50f,
                        height + 1000
                    )
                    close()
                }
                drawPath(path, color = Color(1, 8, 14), style = Fill) // wave color
            }
        }
    }
}