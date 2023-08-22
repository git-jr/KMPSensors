import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
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
import dev.icerock.moko.mvvm.compose.getViewModel
import dev.icerock.moko.mvvm.compose.viewModelFactory
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource

var horizontalPagerInAction by mutableStateOf(false)
var valueToBeChangedOnTheLeft by mutableStateOf(3f)
var valueToBeChangedOnTheRight by mutableStateOf(3f)
var rotate by mutableStateOf(0f)

@Composable
fun App() {
    MaterialTheme {
        val viewModel = getViewModel(Unit, viewModelFactory { ViewModel() })
        MainScreen(viewModel)
    }
}

@Composable
private fun MainScreen(viewModel: ViewModel) {

    val state by viewModel.profileUiState.collectAsState()
    val scope = rememberCoroutineScope()

    Box(
        Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.BottomCenter
    ) {
        var offset by remember { mutableStateOf(0f) }

        LaunchedEffect(offset) {
            println("offset: $offset")

            // using "when" instead of "if" because  can be util to add more animations
            when (offset) {
                0f -> {
                    // wheb offset is 0 (list is in its ideal point), give a little move on the wave
                    rotate += 2
                    delay(800)
                    rotate = 0f
                }
            }
        }

        // wave
        SimpleWave(
            modifier = Modifier
                .alpha(0.5f)
                .offset(y = -(offset.dp) / 10) // deslisamento suave com efeito de paralax na onda
        )

        // tabs
        TabScreen(
            modifier = Modifier,
            state = state,
            onScroll = {
                offset = it.toFloat()
            },
            hasMovedToLeft = {
                horizontalPagerInAction = true
                scope.launch {
                    val originalValeuLeft = valueToBeChangedOnTheLeft
                    val targetValeuLeft = originalValeuLeft * 4f

                    valueToBeChangedOnTheLeft = targetValeuLeft
                    delay(700)
                    valueToBeChangedOnTheLeft = originalValeuLeft
                    delay(700)
                    horizontalPagerInAction = false
                }
                scope.launch {
                    val originalValeuRight = valueToBeChangedOnTheRight
                    val targetValeuRight = originalValeuRight / 2f

                    valueToBeChangedOnTheRight = targetValeuRight
                    delay(700)
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

                    valueToBeChangedOnTheLeft = targetValeuLeft
                    delay(700)
                    valueToBeChangedOnTheLeft = originalValeuLeft
                    delay(700)
                    horizontalPagerInAction = false
                }
                scope.launch {
                    val originalValeuRight = valueToBeChangedOnTheRight
                    val targetValeuRight = originalValeuRight * 4f

                    valueToBeChangedOnTheRight = targetValeuRight
                    delay(700)
                    valueToBeChangedOnTheRight = originalValeuRight
                    delay(700)
                    horizontalPagerInAction = false
                }
            }
        )

    }
}

@Composable
private fun SimpleWave(
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
                    moveTo(-50f, startHeight + 100)  // Começa no canto esquerdo
                    cubicTo(
                        width * 0.33f, peakHeight,  // Primeiro ponto de controle
                        width * 0.66f, dipHeight,  // Segundo ponto de controle
                        width + 50, endHeight + 100  // Termina no canto direito
                    )
                    lineTo(
                        width + 50,
                        height + 1000
                    )  // Vai para a borda inferior direita + 1000 de profunidade para quanodo tela for deslisada para baixo
                    lineTo(
                        -50f,
                        height + 1000
                    )  // Vai para a borda inferior esquerda  + 1000 de profunidade para quanodo tela for deslisada para baixo
                    close()  // Fecha o Path, voltando ao ponto inicial
                }
                drawPath(path, color = Color(1, 8, 14), style = Fill)
            }
        }
    }
}

@OptIn(ExperimentalResourceApi::class)
@Composable
fun AppOld() {
    MaterialTheme {
        var greetingText by remember { mutableStateOf("Hello, World!") }
        var showImage by remember { mutableStateOf(false) }
        Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
            Button(onClick = {
                greetingText = "Hello, ${getPlatformName()}"
                showImage = !showImage
            }) {
                Text(greetingText)
            }
            AnimatedVisibility(
                enter = fadeIn() + expandVertically(
                    spring(
                        dampingRatio = Spring.DampingRatioHighBouncy,
                        stiffness = Spring.StiffnessLow
                    )
                ),
                visible = showImage
            ) {
                Image(
                    painterResource("compose-multiplatform.xml"),
                    null
                )
            }
        }
    }
}


expect fun getPlatformName(): String